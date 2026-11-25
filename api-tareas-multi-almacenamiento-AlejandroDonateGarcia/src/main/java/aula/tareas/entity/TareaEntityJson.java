package aula.tareas.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Diferencia entre cuando usar record o cuando usar class.
 * Según el profe dependiendo de como se vaya buscar la informacion. Si se usan coloeciones que requieran de Equals por ejemplo se usa class.
 * Expandir en eso.
 */

public class TareaEntityJson {
    @JsonAlias("id")
    private Long idTarea;
    @JsonAlias("titulo")
    private String nombreTarea;

   
}
