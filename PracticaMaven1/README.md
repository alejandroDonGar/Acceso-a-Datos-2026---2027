# Práctica Maven — Gestor de tareas

Registro de la práctica guiada de Maven (CodeLearn Academy), lección a lección.
Cada bloque muestra el comando ejecutado en la terminal (WSL Ubuntu) y el resultado real que devolvió.

## Lección 1 · Qué problema resuelve Maven

Ejercicio realizado fuera del proyecto final, en una carpeta de práctica (`leccion01-practica/`), tal como pide el enunciado.

**Comando**
```bash
javac Main.java
java Main
```
**Resultado**
```
Una tarea: aprender Maven
```

**Ejercicio — cambiar el mensaje sin recompilar**

Se cambió el `System.out.println(...)` y se ejecutó `java Main` **antes** de recompilar:

```bash
$ java Main
Una tarea: aprender Maven
```

Sigue apareciendo el mensaje anterior porque `java` ejecuta el bytecode ya compilado en `Main.class`; el `.java` había cambiado pero el `.class` todavía no. Tras recompilar con `javac Main.java`, el resultado cambia:

```bash
$ javac Main.java && java Main
Segunda tarea: entender el ciclo compilar-ejecutar
```

## Lección 2 · Instalar JDK 21 y Maven

**Comando**
```bash
java -version
javac -version
mvn -version
```
**Resultado**
```
openjdk version "21.0.12" 2026-07-21
OpenJDK Runtime Environment (build 21.0.12+8-1-24.04-Ubuntu)
OpenJDK 64-Bit Server VM (build 21.0.12+8-1-24.04-Ubuntu, mixed mode, sharing)
javac 21.0.12
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 21.0.12, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.18.33.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"
```

## Lección 3 · Crear el primer proyecto Maven

Se guardó `pom.xml` (coordenadas `com.codelearn:gestor-tareas:1.0.0-SNAPSHOT`) y `src/main/java/com/codelearn/tareas/Main.java` como indica la lección.

**Comando**
```bash
mvn validate
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

**Ejercicio** — se cambió `artifactId` a `gestor-tareas-test`, se ejecutó `mvn validate` (BUILD SUCCESS de nuevo) y se devolvió a `gestor-tareas`.

## Lección 4 · Compilar y entender los archivos generados

**Comando**
```bash
mvn compile
java -cp target/classes com.codelearn.tareas.Main
```
**Resultado**
```
[INFO] BUILD SUCCESS
Gestor de tareas preparado
```

**Comando**
```bash
find target/classes -type f
```
**Resultado**
```
target/classes/com/codelearn/tareas/Main.class
```

**Comprobación de errores** — se quitó a propósito el `;` final del `System.out.println(...)`:

**Comando**
```bash
mvn compile
```
**Resultado**
```
[ERROR] .../Main.java:[5,57] ';' expected
[ERROR] BUILD FAILURE
```

Se corrigió el `;` y se repitió el ejercicio final de la lección:

**Comando**
```bash
mvn clean
mvn compile
```
**Resultado**
```
[INFO] BUILD SUCCESS   (mvn clean, borra target/)
[INFO] BUILD SUCCESS   (mvn compile, lo reconstruye)
```

## Lección 5 · Ciclos de vida, fases y goals

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] Building jar: .../target/gestor-tareas-1.0.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn clean verify
```
**Resultado** (fases y plugins recorridos)
```
[INFO] --- maven-clean-plugin:2.5:clean ---
[INFO] --- maven-resources-plugin:2.6:resources ---
[INFO] --- maven-compiler-plugin:3.13.0:compile ---
[INFO] --- maven-resources-plugin:2.6:testResources ---
[INFO] --- maven-compiler-plugin:3.13.0:testCompile ---
[INFO] --- maven-surefire-plugin:3.5.2:test ---
[INFO] --- maven-jar-plugin:3.4.2:jar ---
[INFO] BUILD SUCCESS
```

### Práctica integradora (Fase 1 — identificar usuario y entorno)

**Comando**
```bash
whoami
pwd
java -version
javac -version
mvn -version
echo "$JAVA_HOME"
echo "$PATH"
```
**Resultado**
```
alejandro
/mnt/e/2º DAM/AED - Acceso a datos/Acceso-a-Datos-2026---2027/PracticaMaven1
openjdk version "21.0.12" 2026-07-21
javac 21.0.12
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 21.0.12, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
```
`JAVA_HOME` no estaba definida como variable propia; Maven usa el JDK 21 que encuentra en el PATH.

### Fases 2 a 5 (instalar y alternar JDK 17/21) — no realizadas

El paso pide `sudo apt install openjdk-17-jdk` y `sudo update-alternatives --config java/javac`. En esta terminal `sudo` exige contraseña de usuario:

```bash
$ sudo -n apt update
sudo: a password is required
```

Como no se dispone de esa contraseña, esta parte de la práctica (instalar JDK 17, alternar entre JDK 17/21 y comprobar el fallo de build con JDK 17) se deja sin hacer. El resto de la lección (fases del ciclo de vida, `mvn clean verify` con JDK 21) sí queda completado arriba.

## Lección 6 · Añadir y utilizar una dependencia

Se añadió a `pom.xml` la dependencia Gson y se actualizó `Main.java` para usarla (código en el repositorio).

**Comando**
```bash
mvn compile
mvn dependency:tree
```
**Resultado**
```
[INFO] BUILD SUCCESS
...
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
\- com.google.code.gson:gson:jar:2.11.0:compile
   \- com.google.errorprone:error_prone_annotations:jar:2.27.0:compile
```

**Comprobación** — ejecutar solo con `java`, sin las dependencias de Maven en el classpath:

**Comando**
```bash
java -cp target/classes com.codelearn.tareas.Main
```
**Resultado**
```
Exception in thread "main" java.lang.NoClassDefFoundError: com/google/gson/Gson
Caused by: java.lang.ClassNotFoundException: com.google.gson.Gson
```

**Ejercicio** — se cambió el título y se recompiló (BUILD SUCCESS). Después se retiró temporalmente el bloque `<dependencies>` del pom:

**Comando**
```bash
mvn compile
```
**Resultado**
```
[ERROR] package com.google.gson does not exist
[ERROR] cannot find symbol class Gson
[ERROR] BUILD FAILURE
```

Se restauró la dependencia y `mvn compile` volvió a terminar en `BUILD SUCCESS`.

## Lección 7 · Maven Central y el repositorio local

**Comando**
```bash
find ~/.m2/repository/com/google/code/gson/gson -type f
```
**Resultado**
```
/home/alejandro/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar
/home/alejandro/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.pom
/home/alejandro/.m2/repository/com/google/code/gson/gson/2.11.0/_remote.repositories
```

**Comando**
```bash
mvn install
```
**Resultado**
```
[INFO] Installing .../target/gestor-tareas-1.0.0-SNAPSHOT.jar to /home/alejandro/.m2/repository/com/codelearn/gestor-tareas/1.0.0-SNAPSHOT/gestor-tareas-1.0.0-SNAPSHOT.jar
[INFO] Installing .../pom.xml to /home/alejandro/.m2/repository/com/codelearn/gestor-tareas/1.0.0-SNAPSHOT/gestor-tareas-1.0.0-SNAPSHOT.pom
[INFO] BUILD SUCCESS
```

**Comando**
```bash
find ~/.m2/repository/com/codelearn/gestor-tareas -type f
```
**Resultado**
```
/home/alejandro/.m2/repository/com/codelearn/gestor-tareas/1.0.0-SNAPSHOT/gestor-tareas-1.0.0-SNAPSHOT.jar
/home/alejandro/.m2/repository/com/codelearn/gestor-tareas/1.0.0-SNAPSHOT/gestor-tareas-1.0.0-SNAPSHOT.pom
```

**Comando**
```bash
mvn -o package
```
**Resultado**
```
[INFO] BUILD SUCCESS   (modo offline: todo ya estaba en el repositorio local)
```

**Ejercicio** — `mvn install` no publica el proyecto para otras personas porque solo copia el artefacto y su POM al repositorio *local* (`~/.m2/repository`), que vive únicamente en esta máquina. Para que otros lo usen hace falta `mvn deploy` contra un repositorio remoto configurado (ver lección 8).

## Lección 8 · Repositorios externos y settings.xml

Se creó `config/settings-publico.xml` con un perfil `repositorio-publico` que declara explícitamente Maven Central (contenido en el repositorio).

**Comando**
```bash
mvn -s config/settings-publico.xml -Prepositorio-publico help:active-profiles
```
**Resultado**
```
Active Profiles for Project 'com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT':

The following profiles are active:

 - repositorio-publico (source: external)

[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn -s config/settings-publico.xml -Prepositorio-publico compile
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

**Ejercicio** — comprobación sin `-Prepositorio-publico`:

**Comando**
```bash
mvn -s config/settings-publico.xml help:active-profiles
```
**Resultado**
```
Active Profiles for Project 'com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT':

The following profiles are active:

[INFO] BUILD SUCCESS
```

Sin `-P` no aparece ningún perfil activo (la lista sale vacía): el perfil `repositorio-publico` está declarado en `settings.xml` pero no se activa por defecto, así que ese repositorio extra no se añade. El proyecto sigue pudiendo descargar de Central de todas formas porque Central está disponible por defecto en Maven, independientemente de este perfil.

## Lección 9 · Repositorios privados, mirrors y proxy

Esta lección requiere un servidor Nexus/Artifactory real con una cuenta autorizada; las URLs `.example` que aparecen en la página son marcadores de posición, no servicios reales. Al no disponer de un repositorio privado, se sigue la propia indicación del ejercicio: **"Si no dispones de servicio privado, analiza el XML y deja esta práctica de conexión pendiente hasta tener acceso."**

**Análisis del `settings-empresa.xml` de ejemplo (fuera de Git):**

- Un bloque `<server>` con credenciales que NO se escriben en claro, sino mediante variables de entorno: `${env.MAVEN_REPO_USER}` y `${env.MAVEN_REPO_TOKEN}`.
- Un `<mirror mirrorOf="*">` que redirige todas las peticiones a un repositorio único de la empresa (mirror del 100% del tráfico).
- Se ejecutaría con:

```bash
mvn -s settings-empresa.xml clean verify
```

- Opcionalmente, un bloque `<proxies>` para pasar por un proxy corporativo.
- Códigos de error a vigilar: `401` (credenciales inválidas), `403` (sin permiso), `404` (repositorio/artefacto no encontrado), errores de certificado (repositorio con certificado no confiable).

**Estado:** práctica de conexión dejada pendiente hasta disponer de un servicio privado real, tal y como permite el propio enunciado del ejercicio. No se ha creado ningún `settings-empresa.xml` con credenciales de ejemplo para evitar dejar secretos (aunque sean falsos) en el repositorio.

## Lección 10 · Profiles: activar configuraciones de Maven

Se añadió al `pom.xml`, bajo `<project>` y después de `<build>`, un bloque `<profiles>` con dos perfiles: `distribucion` (empaqueta un JAR con dependencias mediante `maven-shade-plugin`) e `informe` (activa avisos de compilación).

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] BUILD SUCCESS
```
(genera el JAR normal `target/gestor-tareas-1.0.0-SNAPSHOT.jar`, sin el perfil `distribucion` activo)

**Comando**
```bash
mvn -Pdistribucion clean package
```
**Resultado**
```
[INFO] Attaching shaded artifact.
[INFO] BUILD SUCCESS
```
(genera además `target/gestor-tareas-1.0.0-SNAPSHOT-all.jar`, con las dependencias incluidas)

**Comando**
```bash
java -jar target/gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
{"completada":false,"titulo":"Aprender Maven a fondo"}
```

**Comando**
```bash
mvn -Pdistribucion,informe clean verify
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn -Pdistribucion help:active-profiles
```
**Resultado**
```
Active Profiles for Project 'com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT':

The following profiles are active:

 - distribucion (source: com.codelearn:gestor-tareas:1.0.0-SNAPSHOT)

[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn -Pdistribucion help:effective-pom
```
**Resultado**
```
[INFO] BUILD SUCCESS
```
(el modelo efectivo muestra el perfil `distribucion` ya fusionado con el resto del POM)

**Ejercicio** — se añadió la activación por propiedad al perfil `informe`:

```xml
<activation>
  <property>
    <name>informe</name>
    <value>true</value>
  </property>
</activation>
```

**Comando**
```bash
mvn -Dinforme=true help:active-profiles
```
**Resultado**
```
Active Profiles for Project 'com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT':

The following profiles are active:

 - informe (source: com.codelearn:gestor-tareas:1.0.0-SNAPSHOT)

[INFO] BUILD SUCCESS
```

El perfil `informe` se activa solo cuando se pasa `-Dinforme=true`; `distribucion` sigue siendo opcional y se activa explícitamente con `-P`.

## Lección 11 · Dependencias transitivas, scopes y conflictos

**Comando**
```bash
mvn dependency:tree
```
**Resultado**
```
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
\- com.google.code.gson:gson:jar:2.11.0:compile
   \- com.google.errorprone:error_prone_annotations:jar:2.27.0:compile

[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn dependency:tree -Dverbose
```
**Resultado**
```
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
\- com.google.code.gson:gson:jar:2.11.0:compile
   \- com.google.errorprone:error_prone_annotations:jar:2.27.0:compile

[INFO] BUILD SUCCESS
```

Se añadieron temporalmente `org.apache.commons:commons-text:1.12.0` y `org.apache.commons:commons-lang3:3.14.0` a `<dependencies>` para observar un caso de mediación de conflictos.

**Comando**
```bash
mvn dependency:tree -Dverbose -Dincludes=org.apache.commons
```
**Resultado**
```
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
+- org.apache.commons:commons-text:jar:1.12.0:compile
|  \- (org.apache.commons:commons-lang3:jar:3.14.0:compile - omitted for duplicate)
\- org.apache.commons:commons-lang3:jar:3.14.0:compile

[INFO] BUILD SUCCESS
```

`commons-text` trae `commons-lang3` de forma transitiva, pero como también hay una declaración directa de `commons-lang3` (menor profundidad), esa es la que gana la mediación; la transitiva aparece "omitted for duplicate".

**Ejercicio** — se retiró la declaración directa de `commons-lang3` para comparar:

**Comando**
```bash
mvn dependency:tree -Dverbose -Dincludes=org.apache.commons
```
**Resultado**
```
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
\- org.apache.commons:commons-text:jar:1.12.0:compile
   \- org.apache.commons:commons-lang3:jar:3.14.0:compile

[INFO] BUILD SUCCESS
```

Sin la declaración directa, `commons-lang3` pasa a aparecer solo como dependencia transitiva de `commons-text` (indentada, ya no "omitted"). Después se restauró la declaración directa y, tal como pide el ejercicio, se retiraron ambas dependencias de práctica (`commons-text` y `commons-lang3`) para dejar el proyecto solo con Gson.

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] BUILD SUCCESS
```
(confirma que el proyecto queda otra vez limpio, solo con Gson)

## Lección 12 · Propiedades y gestión de versiones

Se añadieron a `<properties>` las versiones de Gson, JUnit y de todos los plugins usados (`gson.version`, `junit.version`, `maven-compiler-plugin.version`, `maven-surefire-plugin.version`, `maven-jar-plugin.version`, `maven-shade-plugin.version`, `maven-dependency-plugin.version`, `maven-wrapper-plugin.version`), y la dependencia de Gson pasó a usar `<version>${gson.version}</version>`.

**Comando**
```bash
mvn compile
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

A continuación se añadió `<dependencyManagement>` bajo `<project>` con Gson (fijando su versión mediante la propiedad) y el BOM `junit-bom` (importado con `scope=import` y `type=pom`), y se retiró `<version>` de la declaración de Gson en `<dependencies>` — ahora la aporta `dependencyManagement`. También se sustituyeron las versiones de `maven-compiler-plugin`, `maven-surefire-plugin`, `maven-jar-plugin` y `maven-shade-plugin` (este último dentro del perfil `distribucion`) por sus propiedades, y se añadieron `maven-dependency-plugin` y `maven-wrapper-plugin` a `<build><plugins>` con sus propias versiones gestionadas por propiedad.

**Comando**
```bash
mvn dependency:tree
```
**Resultado**
```
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
\- com.google.code.gson:gson:jar:2.11.0:compile
   \- com.google.errorprone:error_prone_annotations:jar:2.27.0:compile

[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn help:effective-pom -Doutput=target/pom-efectivo.xml
```
**Resultado**
```
[INFO] Effective-POM written to: .../PracticaMaven1/target/pom-efectivo.xml
[INFO] BUILD SUCCESS
```

**Ejercicio** — revisando `target/pom-efectivo.xml` (sin copiarlo sobre el POM original, es solo diagnóstico): la versión de Gson (`2.11.0`) sale de la propiedad `gson.version` a través del bloque `<dependencyManagement>`; las versiones de JUnit (`5.11.0` para los artefactos `junit-jupiter-*`, `1.11.0` para los `junit-platform-*`) salen del BOM `junit-bom` importado, que alinea automáticamente cada artefacto del ecosistema JUnit 5 a la versión correspondiente de su propia política de versiones.

## Lección 13 · Añadir y ejecutar pruebas con JUnit

Se añadió a `<dependencies>` `junit-jupiter` con `scope=test` (la versión la aporta el BOM de la lección 12). Se crearon `GestorTareas.java` (clase con `anadir()` y `listar()`) y `GestorTareasTest.java` con dos pruebas iniciales.

**Comando**
```bash
mvn test
```
**Resultado**
```
[INFO] Running com.codelearn.tareas.GestorTareasTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.615 s -- in com.codelearn.tareas.GestorTareasTest
[INFO] Results:
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn -Dtest=GestorTareasTest test
```
**Resultado**
```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Comprobación** — se cambió temporalmente el tamaño esperado en `anadeUnaTarea()` de `1` a `2`:

**Comando**
```bash
mvn test
```
**Resultado**
```
[ERROR] Failures:
[ERROR]   GestorTareasTest.anadeUnaTarea:11 expected: <2> but was: <1>
[ERROR] Tests run: 2, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

Se restauró el valor a `1`.

**Ejercicio** — se añadieron dos pruebas más: una para un título `null` y otra que comprueba que `listar()` no permite modificar el estado interno (la lista devuelta es inmutable, gracias a `List.copyOf`).

**Comando**
```bash
mvn test
```
**Resultado**
```
[INFO] Running com.codelearn.tareas.GestorTareasTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.092 s -- in com.codelearn.tareas.GestorTareasTest
[INFO] Results:
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Lección 14 · El build como comprobación de calidad

**Comando**
```bash
mvn clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Building jar: .../PracticaMaven1/target/gestor-tareas-1.0.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
```

**Comprobación** — se eliminó temporalmente `titulos.add(titulo);` de `GestorTareas.anadir`:

**Comando**
```bash
mvn clean verify
```
**Resultado**
```
[ERROR] Failures:
[ERROR]   GestorTareasTest.anadeUnaTarea:11 expected: <1> but was: <0>
[ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

Al haber ejecutado `clean`, no queda ningún JAR anterior en `target/` que pudiera confundirse con uno recién construido — el build se detuvo antes de llegar a la fase de empaquetado. Se restauró la línea y se repitió:

**Comando**
```bash
mvn clean verify
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

**Comprobación del código de salida:**

**Comando**
```bash
mvn clean verify ; echo $?
```
**Resultado**
```
[INFO] BUILD SUCCESS
0
```

**Ejercicio** — se provocaron ambos tipos de fallo por separado:

1) Fallo de **compilación** (se quitó el `;` final de `titulos.add(titulo)`):

**Comando**
```bash
mvn clean verify
```
**Resultado**
```
[ERROR] COMPILATION ERROR :
[ERROR] .../GestorTareas.java:[13,28] ';' expected
[INFO] BUILD FAILURE
```
Causa identificada: error de sintaxis (falta un `;`) en `GestorTareas.java` línea 13. Corregido restaurando el `;`.

2) Fallo de **aserción** (se cambió el texto esperado en el test a uno incorrecto):

**Comando**
```bash
mvn clean verify
```
**Resultado**
```
[ERROR] Failures:
[ERROR]   GestorTareasTest.anadeUnaTarea:12 expected: <Otro texto incorrecto> but was: <Aprender Maven>
[INFO] BUILD FAILURE
```
Causa identificada: la aserción del test esperaba un valor que no coincide con el comportamiento real de `GestorTareas`. Corregido restaurando el valor esperado (`"Aprender Maven"`).

**Comando**
```bash
mvn clean verify ; echo $?
```
**Resultado**
```
[INFO] BUILD SUCCESS
0
```

## Lección 15 · Recursos y configuración de la aplicación

Se creó `src/main/resources/aplicacion.properties` con `nombre=Gestor de tareas`, y `Main.java` se sustituyó por una versión que lo carga del classpath con `getResourceAsStream("/aplicacion.properties")` y lo imprime como JSON con Gson.

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

**Comando**
```bash
jar tf target/gestor-tareas-1.0.0-SNAPSHOT.jar
```
**Resultado**
```
META-INF/
META-INF/MANIFEST.MF
com/
com/codelearn/
com/codelearn/tareas/
META-INF/maven/
META-INF/maven/com.codelearn/
META-INF/maven/com.codelearn/gestor-tareas/
aplicacion.properties
com/codelearn/tareas/GestorTareas.class
com/codelearn/tareas/Main.class
META-INF/maven/com.codelearn/gestor-tareas/pom.xml
META-INF/maven/com.codelearn/gestor-tareas/pom.properties
```
`aplicacion.properties` aparece en la raíz del JAR, junto a `target/classes`.

**Comando**
```bash
mvn -Pdistribucion package
```
**Resultado**
```
[INFO] Attaching shaded artifact.
[INFO] BUILD SUCCESS
```

**Comando**
```bash
java -jar target/gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
{"nombre":"Gestor de tareas"}
```

**Ejercicio** — se cambió el valor de `nombre` en `aplicacion.properties` y se reconstruyó:

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

Comprobación del recurso actualizado dentro del artefacto:
```
nombre=Gestor de tareas 2o DAM
```

A continuación se retiró temporalmente `aplicacion.properties` y se reconstruyó con el perfil `distribucion`:

**Comando**
```bash
mvn -Pdistribucion clean package
java -jar target/gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
[INFO] BUILD SUCCESS
Exception in thread "main" java.io.IOException: Falta aplicacion.properties en el classpath
	at com.codelearn.tareas.Main.main(Main.java:13)
```
(el build sí genera el JAR porque los recursos no son un paso verificado por defecto; el error aparece al ejecutar, como esperaba el propio `Main.java`)

Se restauró `aplicacion.properties` y se comprobó de nuevo:

**Comando**
```bash
mvn clean package
```
**Resultado**
```
[INFO] BUILD SUCCESS
```

## Lección 16 · Empaquetar y ejecutar la aplicación

Se añadió al `maven-jar-plugin` existente un bloque `<configuration><archive><manifest>` con `<mainClass>com.codelearn.tareas.Main</mainClass>`.

**Comando**
```bash
mvn clean package
jar tf target/gestor-tareas-1.0.0-SNAPSHOT.jar
```
**Resultado**
```
META-INF/
META-INF/MANIFEST.MF
com/
com/codelearn/
com/codelearn/tareas/
META-INF/maven/
META-INF/maven/com.codelearn/
META-INF/maven/com.codelearn/gestor-tareas/
aplicacion.properties
com/codelearn/tareas/GestorTareas.class
com/codelearn/tareas/Main.class
META-INF/maven/com.codelearn/gestor-tareas/pom.xml
META-INF/maven/com.codelearn/gestor-tareas/pom.properties

[INFO] BUILD SUCCESS
```

**Comando**
```bash
mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib
```
**Resultado**
```
[INFO] BUILD SUCCESS
```
(copia `gson-2.11.0.jar` y `error_prone_annotations-2.27.0.jar` a `target/lib`)

**Comando** (macOS/Linux)
```bash
java -cp "target/gestor-tareas-1.0.0-SNAPSHOT.jar:target/lib/*" com.codelearn.tareas.Main
```
**Resultado**
```
{"nombre":"Gestor de tareas 2o DAM"}
```

**Manifiesto y dependencias** — se comprobó que `java -jar` sin classpath falla porque `Main-Class` no incluye Gson:

**Comando**
```bash
java -jar target/gestor-tareas-1.0.0-SNAPSHOT.jar
```
**Resultado**
```
Exception in thread "main" java.lang.NoClassDefFoundError: com/google/gson/Gson
	at com.codelearn.tareas.Main.main(Main.java:17)
Caused by: java.lang.ClassNotFoundException: com.google.gson.Gson
	... 1 more
```

**Comando**
```bash
mvn -Pdistribucion clean package
java -jar target/gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
[INFO] BUILD SUCCESS
{"nombre":"Gestor de tareas 2o DAM"}
```
(el JAR `-all` sí incluye las bibliotecas)

**Ejercicio** — se copiaron el JAR convencional junto con `target/lib/` a otra carpeta, y por separado el JAR `-all` a otra, y se ejecutaron ambos desde fuera del proyecto:

**Comando**
```bash
java -cp "gestor-tareas-1.0.0-SNAPSHOT.jar:lib/*" com.codelearn.tareas.Main
```
**Resultado**
```
{"nombre":"Gestor de tareas 2o DAM"}
```

**Comando**
```bash
java -jar gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
{"nombre":"Gestor de tareas 2o DAM"}
```

Ambas formas funcionan igual fuera del proyecto: lo importante es llevar siempre el JAR junto con sus dependencias (bien como carpeta `lib/` + classpath, bien como JAR `-all` autocontenido).

## Lección 17 · Crear y utilizar Maven Wrapper

**Comando**
```bash
mvn wrapper:wrapper -Dmaven=3.9.11 -Dtype=only-script
```
**Resultado**
```
[INFO] --- wrapper:3.3.2:wrapper (default-cli) @ gestor-tareas ---
[INFO] Unpacked only-script type wrapper distribution org.apache.maven.wrapper:maven-wrapper-distribution:zip:only-script:3.3.2
[INFO] Configuring .mvn/wrapper/maven-wrapper.properties to use Maven 3.9.11 and download from https://repo.maven.apache.org/maven2
[INFO] BUILD SUCCESS
```
Se crearon `mvnw`, `mvnw.cmd` y `.mvn/wrapper/maven-wrapper.properties` en la raíz del proyecto.

**Comando**
```bash
chmod +x mvnw
./mvnw --version
```
**Resultado**
```
Apache Maven 3.9.11 (3e54c93a704997b63ee3494413a2b544fd3d828b)
Maven home: /home/alejandro/.m2/wrapper/dists/apache-maven-3.9.11/a2d47e15
Java version: 21.0.12, vendor: Eclipse Adoptium, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: es_ES, platform encoding: UTF-8
OS name: "linux", version: "6.18.33.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"
```

**Comando**
```bash
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Ejercicio** — se ejecutó el Wrapper en una terminal nueva (PowerShell, en Windows) y se comparó con el Maven global de esa terminal:

**Comando** (PowerShell)
```powershell
.\mvnw.cmd --version
```
**Resultado**
```
Apache Maven 3.9.11 (3e54c93a704997b63ee3494413a2b544fd3d828b)
Maven home: C:\Users\AlexA\.m2\wrapper\dists\apache-maven-3.9.11\a2d47e15
Java version: 17.0.19, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-17.0.19.18-hotspot
Default locale: es_ES, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

**Comando** (PowerShell)
```powershell
mvn -version
```
**Resultado**
```
Apache Maven 3.9.16 (2bd0ffdddedd35eb98d0e807ea73fd029a51d5)
Maven home: C:\Tools\apache-maven-3.9.16
Java version: 17.0.19, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-17.0.19.18-hotspot
Default locale: es_ES, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

**Qué controla cada parte:** el Wrapper fija y descarga su **propia** distribución de Maven (3.9.11, en `.m2/wrapper/dists/`, independiente de cualquier Maven instalado globalmente); por eso `mvnw.cmd` usa 3.9.11 mientras que el `mvn` global de esa misma máquina resulta ser 3.9.16 (instalado en `C:\Tools`). En cambio, el **JDK** (`17.0.19` en ambos casos en esa terminal Windows, `21.0.12` en la terminal WSL) sigue dependiendo del entorno — el Wrapper no lo fija ni lo descarga, usa el que encuentre disponible en el sistema donde se ejecuta.

## Lección 18 · Maven dentro del flujo Git

El proyecto ya formaba parte de un repositorio Git existente (la carpeta raíz de este curso), así que no hizo falta `git init`. Se creó `.gitignore`:

```
target/
.idea/
*.iml
.vscode/
tareas.json
settings-empresa.xml
```

**Comando**
```bash
git add pom.xml src .gitignore mvnw mvnw.cmd .mvn
```

**Comprobación** — `git status --short` mostraba archivos de `target/` como ya versionados de antes (arrastrados de un commit anterior), justo el caso que avisa la propia lección: *"Un archivo ya versionado no deja de estarlo por añadirlo después a .gitignore"*. Se corrigió retirándolos del índice sin borrarlos del disco:

**Comando**
```bash
git rm -r --cached target
```
**Resultado**
```
(sin salida; target/ deja de estar rastreado y target/ ya no aparece en git status --short)
```

**Comando**
```bash
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Comando**
```bash
git diff --cached
```
**Resultado**
```
15 files changed, 648 insertions(+), 34 deletions(-)
```

**Comando**
```bash
git commit -m "Crea proyecto Maven verificable"
```
**Resultado**
```
[main eab876b] Crea proyecto Maven verificable
 15 files changed, 648 insertions(+), 34 deletions(-)
```

**Comprobación** — el Wrapper está incluido en el repositorio:

**Comando**
```bash
git ls-files .mvn mvnw mvnw.cmd
```
**Resultado**
```
.mvn/wrapper/maven-wrapper.properties
mvnw
mvnw.cmd
```

**Clon independiente** — como `PracticaMaven1` es una carpeta dentro de un repositorio más grande (no un repositorio propio), se clonó el repositorio completo en otra carpeta:

**Comando**
```bash
git clone . ../gestor-tareas-clon
cd ../gestor-tareas-clon/PracticaMaven1
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
El clon limpio reconstruye el proyecto sin depender de nada fuera de lo versionado.

**Ejercicio** — se creó una rama, se añadió una prueba, se verificó y se hizo commit:

**Comando**
```bash
git checkout -b feature/prueba-titulo
```
**Resultado**
```
Switched to a new branch 'feature/prueba-titulo'
```

Se añadió la prueba `anadeVariasTareasManteniendoElOrden()` (comprueba que varias tareas añadidas seguidas mantienen su orden).

**Comando**
```bash
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Comando**
```bash
git add src/test/java/com/codelearn/tareas/GestorTareasTest.java
git commit -m "Anade prueba: varias tareas mantienen el orden"
```
**Resultado**
```
[feature/prueba-titulo b814407] Anade prueba: varias tareas mantienen el orden
 1 file changed, 12 insertions(+)
```

**Cómo reproducir el build desde un clon:** clonar el repositorio, entrar en la carpeta `PracticaMaven1` y ejecutar `./mvnw clean verify` (o `.\mvnw.cmd clean verify` en Windows) — no hace falta tener Maven instalado globalmente, solo un JDK compatible; el propio Wrapper descarga la distribución de Maven exacta que fija el proyecto.

## Lección 19 · Automatizar el build en CI

El repositorio ya usa GitHub como remoto, así que se configuró GitHub Actions. Se creó `.github/workflows/maven.yml` (en la raíz del repositorio) con JDK 21 (Temurin), caché de Maven, permisos de ejecución del Wrapper, `./mvnw -Pdistribucion clean verify` y subida del JAR `-all` como artefacto:

```yaml
name: Maven CI
on:
  push:
  pull_request:
jobs:
  build:
    runs-on: ubuntu-latest
    defaults:
      run:
        working-directory: PracticaMaven1
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
      - run: chmod +x mvnw
      - run: ./mvnw -Pdistribucion clean verify
      - uses: actions/upload-artifact@v4
        with:
          name: gestor-tareas
          path: PracticaMaven1/target/*-all.jar
          if-no-files-found: error
```

(`working-directory: PracticaMaven1` porque el proyecto Maven vive en una subcarpeta del repositorio, no en la raíz.)

**Comando**
```bash
git add .github/workflows/maven.yml
git commit -m "Añade workflow de CI con GitHub Actions"
```
**Resultado**
```
[main 683122c] Añade workflow de CI con GitHub Actions
 1 file changed, 24 insertions(+)
 create mode 100644 .github/workflows/maven.yml
```

El job se ejecutará en GitHub Actions en el repositorio `alejandroDonar/Acceso-a-Datos-2026---2027` en el próximo `git push`. No se ha configurado acceso a repositorios privados (sección opcional de la lección) por no ser necesario para este proyecto.

## Lección 20 · Diagnosticar y reparar problemas de Maven

Se trabajó en una rama de práctica (`practica/diagnostico`) partiendo de un `./mvnw clean verify` correcto. Se provocó un problema cada vez, se investigó con los comandos que indica la página y se restauró antes de pasar al siguiente.

**Comando**
```bash
git checkout -b practica/diagnostico
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 1. Etiqueta del POM sin cerrar

Se quitó el cierre de `</artifactId>` en el `pom.xml`.

**Comando**
```bash
./mvnw -e test
```
**Resultado (señal: POM no legible)**
```
[ERROR] The project (.../PracticaMaven1/pom.xml) has 1 error
[ERROR]     Non-parseable POM .../PracticaMaven1/pom.xml: TEXT must be immediately
followed by END_TAG and not START_TAG (position: START_TAG seen
...<artifactId>gestor-tareas\n  <version>... @7:12)  @ line 7, column 12
org.apache.maven.model.io.ModelParseException: TEXT must be immediately followed
by END_TAG and not START_TAG (position: START_TAG seen ...<artifactId>gestor-tareas\n
<version>... @7:12)
```
**Causa:** etiqueta `<artifactId>` sin cerrar, línea 7 del `pom.xml`. **Corrección:** cerrar la etiqueta y volver a compilar.

### 2. Versión de Gson inexistente

Se cambió `<gson.version>` a `2.999.999`, una versión que no existe en Maven Central.

**Comando**
```bash
./mvnw dependency:tree -Dverbose
```
**Resultado**
```
[WARNING] Downloading from central: https://repo.maven.apache.org/maven2/com/google/code/gson/gson/2.999.999/gson-2.999.999.pom
[WARNING] The POM for com.google.code.gson:gson:jar:2.999.999 is missing, no dependency information available
com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
+- com.google.code.gson:gson:jar:2.999.999:compile
```
El árbol se genera igualmente (solo usa las coordenadas declaradas), pero avisa de que el POM del artefacto no se pudo descargar. El fallo real aparece al compilar/testear:

**Comando**
```bash
./mvnw -e test
```
**Resultado (señal: no se resuelve el artefacto)**
```
[ERROR] Failed to execute goal on project gestor-tareas: Could not resolve dependencies
for project com.codelearn:gestor-tareas:jar:1.0.0-SNAPSHOT
[ERROR] dependency: com.google.code.gson:gson:jar:2.999.999 (compile)
[ERROR]         Could not find artifact com.google.code.gson:gson:jar:2.999.999 in
central (https://repo.maven.apache.org/maven2)
```
**Causa:** coordenadas de dependencia inexistentes en el repositorio. **Corrección:** volver la propiedad `gson.version` a `2.11.0`.

### 3. Aserción incorrecta

En `GestorTareasTest.anadeUnaTarea()` se cambió `assertEquals(1, gestor.listar().size())` por `assertEquals(2, ...)`.

**Comando**
```bash
./mvnw -e test
```
**Resultado (señal: fallo de Surefire)**
```
[ERROR] Failures:
[ERROR]   GestorTareasTest.anadeUnaTarea:11 expected: <2> but was: <1>
[INFO] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
[ERROR] See .../PracticaMaven1/target/surefire-reports for the individual test results.
```
**Causa:** aserción incorrecta en el test (esperaba 2 tareas cuando solo se añade 1). **Corrección:** devolver la aserción a `assertEquals(1, ...)`.

### 4. Test con nombre no reconocido

Se renombró `GestorTareasTest.java` a `GestorTareasComprobaciones.java` y también la clase dentro (`class GestorTareasComprobaciones`), un nombre que no encaja con los patrones que busca Surefire (`*Test`, `Test*`, `*Tests`, `*TestCase`).

**Comando**
```bash
./mvnw test
```
**Resultado (señal: cero pruebas, sin avisos)**
```
[INFO] --- surefire:3.5.2:test (default-test) @ gestor-tareas ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```
No aparece ninguna línea `Running ...` ni `Tests run:`: Surefire no encontró ninguna clase de test que coincidiera con sus patrones de nombre, y el build termina en éxito aunque no se ejecutó ninguna prueba. **Causa:** el nombre de archivo y de clase no coincide con los patrones de descubrimiento de Surefire. **Corrección:** devolver el archivo y la clase a `GestorTareasTest`.

### 5. Ejecutar el JAR convencional sin Gson

**Comando**
```bash
./mvnw clean package
java -cp target/gestor-tareas-1.0.0-SNAPSHOT.jar com.codelearn.tareas.Main
```
**Resultado (señal: NoClassDefFoundError)**
```
Exception in thread "main" java.lang.NoClassDefFoundError: com/google/gson/Gson
        at com.codelearn.tareas.Main.main(Main.java:17)
Caused by: java.lang.ClassNotFoundException: com.google.gson.Gson
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526)
```
**Causa:** el JAR normal (`maven-jar-plugin`) no incluye las dependencias; Gson no está en el classpath. **Corrección:** usar el artefacto de distribución (perfil `distribucion`, JAR `-all` generado por `maven-shade-plugin`):

**Comando**
```bash
./mvnw -Pdistribucion clean package
java -jar target/gestor-tareas-1.0.0-SNAPSHOT-all.jar
```
**Resultado**
```
[INFO] BUILD SUCCESS
{"nombre":"Gestor de tareas 2o DAM"}
```

### Vuelta a la rama principal

**Comando**
```bash
git checkout main
```
**Resultado**
```
Switched to branch 'main'
Your branch is ahead of 'origin/main' by 3 commits.
```

### Ejercicio: informe de un fallo

**Comando:** `./mvnw -e test`

**Señal observada:**
```
[ERROR] Failures:
[ERROR]   GestorTareasTest.anadeUnaTarea:11 expected: <2> but was: <1>
[INFO] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

**Causa:** el test `anadeUnaTarea()` comprobaba `assertEquals(2, gestor.listar().size())` tras añadir una sola tarea con `gestor.anadir("Aprender Maven")`. El propio `GestorTareas` funciona bien; la aserción del test estaba mal escrita a propósito para provocar el fallo.

**Corrección:** devolver la aserción a `assertEquals(1, gestor.listar().size())`, que es lo que realmente produce el código, y volver a ejecutar las pruebas:

**Comando**
```bash
./mvnw clean verify
```
**Resultado**
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Lección 21 · Proyecto final: gestor de tareas

Se amplió el proyecto para integrar persistencia, pruebas y distribución en una entrega construible desde un clon limpio.

### Requisitos funcionales implementados

La consola permite **añadir**, **listar**, **completar** y **eliminar** tareas. Cada tarea tiene identificador, título y estado (`Tarea.java`). Un título vacío y un identificador inexistente se rechazan con `IllegalArgumentException`, capturada en `Main` como `Error: ...`. Las tareas se guardan en un JSON externo (`RepositorioTareas.java`, con Gson) y se recuperan al iniciar; si el archivo no existe se empieza con una lista vacía, y si el JSON está dañado se informa del problema sin sobrescribir el archivo.

### Diseño

```
src/main/java/com/codelearn/tareas/
├── Main.java                # interacción de consola (comandos)
├── Tarea.java                # identificador, título y estado
├── GestorTareas.java          # operaciones del dominio
└── RepositorioTareas.java    # lectura/escritura JSON
src/test/java/com/codelearn/tareas/
├── GestorTareasTest.java
└── RepositorioTareasTest.java
```

Se eligió un intérprete de **comandos** (no menú numérico): `añadir <título>`, `completar <id>`, `eliminar <id>`, `listar`, `salir`. La ruta del archivo de datos se puede pasar como primer argumento; si no se indica, se usa `tareas.json` en el directorio desde el que se ejecuta el JAR (fuera de `src/`, y excluido de git por el `.gitignore`).

**Comando**
```bash
./mvnw clean verify
```
**Resultado**
```
[INFO] Running com.codelearn.tareas.GestorTareasTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.codelearn.tareas.RepositorioTareasTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

`GestorTareasTest` cubre añadir, completar, eliminar y buscar un identificador inexistente. `RepositorioTareasTest` usa un directorio temporal (`@TempDir`) para probar guardar-y-cargar (ida y vuelta), un archivo inexistente (devuelve lista vacía) y un JSON inválido (lanza excepción sin tocar el archivo original).

### Prueba manual de la consola

**Comando**
```bash
java -jar target/gestor-tareas-1.0.0-all.jar
```
**Resultado**
```
Gestor de tareas. Comandos: añadir <titulo>, completar <id>, eliminar <id>, listar, salir
> añadir Aprender Maven
Tarea 1 creada
> completar 1
Tarea 1 completada
> listar
1 [completada] Aprender Maven
> salir
```

Al volver a ejecutar el JAR sobre el mismo `tareas.json`, `listar` devuelve `1 [completada] Aprender Maven`: la tarea se recupera correctamente entre ejecuciones.

### Versión 1.0.0 y etiqueta

Se cambió la versión del `pom.xml` de `1.0.0-SNAPSHOT` a `1.0.0`, se verificó el build y se creó la etiqueta `v1.0.0`.

**Comando**
```bash
./mvnw -Pdistribucion clean verify
git tag v1.0.0
```
**Resultado**
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] Building jar: .../target/gestor-tareas-1.0.0.jar
[INFO] Attaching shaded artifact.
[INFO] BUILD SUCCESS
```

### Comprobación de entrega desde un clon limpio

**Comando**
```bash
git clone . ../gestor-tareas-entrega
cd ../gestor-tareas-entrega/PracticaMaven1
./mvnw -Pdistribucion clean verify
java -jar target/gestor-tareas-1.0.0-all.jar
```
**Resultado**
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
Gestor de tareas. Comandos: añadir <titulo>, completar <id>, eliminar <id>, listar, salir
> listar
> salir
```

Con el JSON aún no creado en el clon, `listar` no muestra nada (lista vacía), tal y como se espera.

### Ejercicio de revisión

Al seguir **solo** las instrucciones del README desde la raíz del repositorio, el primer punto ambiguo fue no indicar que hay que entrar antes en la carpeta `PracticaMaven1`:

**Comando (desde la raíz del repositorio, tal y como estaba escrito antes)**
```bash
./mvnw -Pdistribucion clean verify
```
**Resultado**
```
bash: ./mvnw: No such file or directory
```

**Corrección:** se añadió explícitamente el paso `cd PracticaMaven1` antes de cualquier comando `./mvnw`, tanto en esta sección como en la de comprobación de entrega, porque el proyecto Maven vive en una subcarpeta del repositorio y no en su raíz.

Se comprobó también que git no contiene `target/`, el fichero mutable `tareas.json` ni credenciales:

**Comando**
```bash
git ls-files | grep -iE "target/|tareas.json|secret|password|credencial"
```
**Resultado**
```
(sin resultados)
```

### Requisitos externos

No se requiere ninguna configuración externa obligatoria para construir y ejecutar el proyecto: `./mvnw` descarga la distribución de Maven que fija el Wrapper, y todas las dependencias (Gson, JUnit) están en Maven Central. Los archivos `config/settings-publico.xml` y `settings-empresa.xml` de las lecciones 8-9 son solo para el escenario opcional de repositorios privados/empresariales y no son necesarios para esta entrega; no contienen credenciales.

## Simplificación del código

Tras terminar el proyecto final se revisó el código de `Main.java`, `GestorTareas.java`, `RepositorioTareas.java` y los tests: se quitó `var` (tipos explícitos en su lugar), el uso de streams (`buscar()` ahora usa un bucle `for` normal) y `NoSuchElementException` (ahora `buscar()` lanza `IllegalArgumentException`, igual que la validación del título). El `switch` con flechas de `Main.java` se cambió por un `if/else` clásico. El comportamiento no cambió: las mismas 11 pruebas siguen pasando y la consola da los mismos resultados.

**Comando**
```bash
./mvnw -Pdistribucion clean verify
```
**Resultado**
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Resumen final

Las 21 lecciones de la ruta *Maven y construcción de proyectos Java* están completas en `PracticaMaven1`: desde el primer proyecto Maven hasta un gestor de tareas con persistencia JSON, pruebas JUnit, Maven Wrapper, integración con Git/GitHub Actions y una entrega verificable con la etiqueta `v1.0.0` desde un clon limpio.

**Comando de comprobación final**
```bash
./mvnw -Pdistribucion clean verify
```
**Resultado**
```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
