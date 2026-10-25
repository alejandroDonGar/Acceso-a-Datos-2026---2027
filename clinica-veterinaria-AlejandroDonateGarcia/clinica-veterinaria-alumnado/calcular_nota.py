#!/usr/bin/env python3

from pathlib import Path
import subprocess
import sys
import xml.etree.ElementTree as ET

CLASES = ['ClienteDaoTest', 'MascotaDaoTest', 'VeterinarioDaoTest',
          'ConsultaDaoTest', 'TratamientoDaoTest', 'IntegracionDaoTest']
INFORMES = Path('target/surefire-reports')


def main():
    subprocess.run('mvn -q clean test -Dmaven.test.failure.ignore=true', shell=True)

    total_ok = total = 0
    print('\nNOTA ORIENTATIVA - Clinica veterinaria')
    for nombre in CLASES:
        informe = INFORMES / f'TEST-es.ejemplo.clinica.{nombre}.xml'
        if not informe.is_file():
            print(f'  {nombre}: SIN INFORME (no compila o no se ha ejecutado)')
            continue
        casos = ET.parse(informe).getroot().findall('.//testcase')
        suspensas = [c.get('name') for c in casos
                     if any(c.find(t) is not None for t in ('failure', 'error', 'skipped'))]
        ok = len(casos) - len(suspensas)
        total_ok += ok
        total += len(casos)
        print(f'  {nombre}: {ok}/{len(casos)}')
        for s in suspensas:
            print(f'      - {s}')

    nota = 10 * total_ok / 90
    print(f'Pruebas superadas: {total_ok}/90 (ejecutadas: {total})')
    print(f'NOTA: {nota:.2f}/10')


if __name__ == '__main__':
    sys.exit(main())
