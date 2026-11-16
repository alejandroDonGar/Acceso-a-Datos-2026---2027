package aula.tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Spring y SpringBoot no son lo mismo. SpringBoot es que nos depliega un server local.
@SpringBootApplication
public class TareasApplication {
    public static void main(String[] args) {
        SpringApplication.run(TareasApplication.class, args);
    }
}

/*--------------------*/
// Antes de escribir codigo, hacemos: mvn clean spring-boot:run porque si no, peta el codigo que hayamos hecho