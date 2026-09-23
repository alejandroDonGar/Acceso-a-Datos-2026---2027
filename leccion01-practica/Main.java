cat >> PracticaMaven1/README.md << 'EOF'
## Lección 2 · Instalar JDK 21 y Maven

El JDK 21 y Maven ya estaban instalados en la terminal WSL (Ubuntu) usada para la práctica. Se verificó el entorno tal como pide la lección.

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

`java`, `javac` y `mvn` coinciden en Java 21, y Maven usa el mismo JDK (`/usr/lib/jvm/java-21-openjdk-amd64`). Nota: el gestor de paquetes de esta Ubuntu trae Maven 3.8.7 en vez del 3.9.x que sugiere la lección; es una diferencia menor de versión de Maven (no de Java) y no afecta a esta práctica.

EOF
echo OK
public class Main {
    public static void main(String[] args) {
        System.out.println("Segunda tarea: entender el ciclo compilar-ejecutar");
    }
}
