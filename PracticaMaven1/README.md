Paso 6
- mvn clean compile
- resultado 
```
AlejandroDonate@debian-des:~/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1$ mvn clean compile
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------< com.codelearn:gestor-tareas >---------------------
[INFO] Building gestor-tareas 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ gestor-tareas ---
[INFO] Deleting /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ gestor-tareas ---
[INFO] skip non existing resourceDirectory /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/src/main/resources
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ gestor-tareas ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 1 source file with javac [debug release 21] to target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.418 s
[INFO] Finished at: 2026-09-22T19:23:48+01:00
[INFO] ------------------------------------------------------------------------
```
- mvn clean verify
- resultado
```
lejandroDonate@debian-des:~/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1$ mvn clean verify
[INFO] Scanning for projects...
[INFO] 
[INFO] --------------------< com.codelearn:gestor-tareas >---------------------
[INFO] Building gestor-tareas 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ gestor-tareas ---
[INFO] Deleting /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ gestor-tareas ---
[INFO] skip non existing resourceDirectory /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/src/main/resources
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ gestor-tareas ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 1 source file with javac [debug release 21] to target/classes
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ gestor-tareas ---
[INFO] skip non existing resourceDirectory /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ gestor-tareas ---
[INFO] No sources to compile
[INFO] 
[INFO] --- surefire:3.5.2:test (default-test) @ gestor-tareas ---
[INFO] No tests to run.
[INFO] 
[INFO] --- jar:3.4.2:jar (default-jar) @ gestor-tareas ---
[INFO] Building jar: /home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1/target/gestor-tareas-1.0.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.784 s
[INFO] Finished at: 2026-09-22T19:25:50+01:00
[INFO] ------------------------------------------------------------------------
```
- comprobar quien soy

```
whoami
pwd
java -version
javac -version
mvn -version
echo "$JAVA_HOME"
echo "$PATH"

```
- reusltado
```
pwd
java -version
javac -version
mvn -version
echo "$JAVA_HOME"
echo "$PATH"
AlejandroDonate
/home/AlejandroDonate/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1
openjdk version "21.0.12.1" 2026-08-18
OpenJDK Runtime Environment (build 21.0.12.1+1-1-deb13u1-Debian)
OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-deb13u1-Debian, mixed mode, sharing)
javac 21.0.12.1
Apache Maven 3.9.9
Maven home: /usr/share/maven
Java version: 21.0.12.1, vendor: Debian, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: es_ES, platform encoding: UTF-8
OS name: "linux", version: "6.12.107+deb13-amd64", arch: "amd64", family: "unix"
/usr/lib/jvm/java-1.21.0-openjdk-amd64
/usr/lib/jvm/java-1.21.0-openjdk-amd64/bin:/usr/local/bin:/usr/bin:/bin:/usr/local/games:/usr/games
```
Selecciona temporalmente JDK 17 para java y javac:


```
sudo update-alternatives --config java
sudo update-alternatives --config javac
```
- resultado 
```
AlejandroDonate@debian-des:~/Escritorio/Acceso-a-Datos-2026---2027/PracticaMaven1$ sudo update-alternatives --config java
sudo update-alternatives --config javac
Existe 1 opción para la alternativa java (que provee /usr/bin/java).

  Selección   Ruta                                         Prioridad  Estado
------------------------------------------------------------
* 0            /usr/lib/jvm/java-21-openjdk-amd64/bin/java   2111      modo automático
  1            /usr/lib/jvm/java-21-openjdk-amd64/bin/java   2111      modo manual

Pulse <Intro> para mantener el valor por omisión [*] o pulse un número de selección: 
Existe 1 opción para la alternativa javac (que provee /usr/bin/javac).

  Selección   Ruta                                          Prioridad  Estado
------------------------------------------------------------
* 0            /usr/lib/jvm/java-21-openjdk-amd64/bin/javac   2111      modo automático
  1            /usr/lib/jvm/java-21-openjdk-amd64/bin/javac   2111      modo manual

Pulse <Intro> para mantener el valor por omisión [*] o pulse un número de selección: 
```
Ahora vuelve a seleccionar JDK 21 y configura sus variables:


```
openjdk version "21.0.12.1" 2026-08-18
OpenJDK Runtime Environment (build 21.0.12.1+1-1-deb13u1-Debian)
OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-deb13u1-Debian, mixed mode, sharing)
javac 21.0.12.1
The JAVA_HOME environment variable is not defined correctly,
this environment variable is needed to run this program.
/usr/lib/jvm/java-17-openjdk-amd64
```