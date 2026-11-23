package ies.puesto.peliculas.controller;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.controller.mapper.PeliculaApiMapper;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST del catálogo de películas en {@code /api/v1/jdbc/peliculas}.
 * Traduce el {@code Optional}/{@code boolean} del servicio a códigos HTTP;
 * los 404 y 409 los devuelve con {@link ProblemDetail}. Solo existe con el perfil {@code jdbc}.
 * Sin {@code @Validated} en la clase: así un código inválido da 400 y no 500.
 *
 * @author AlejandroDonGar
 */
@RestController
@Profile("jdbc")
@RequestMapping("/api/v1/jdbc/peliculas")
@Tag(name = "Películas JDBC", description = "CRUD del catálogo de películas sobre SQLite")
public class PeliculaJdbcController {

    private static final String PATRON_CODIGO = "^PEL[0-9]{3}$";

    private final PeliculaService service;
    private final PeliculaApiMapper mapper;

    public PeliculaJdbcController(PeliculaService service, PeliculaApiMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Lista todas las películas.
     * {@code @GetMapping} sin ruta: responde a {@code GET /api/v1/jdbc/peliculas}.
     * {@code @Operation} y {@code @ApiResponses} solo documentan el endpoint en Swagger.
     *
     * @return lista de películas (200 OK, Spring la convierte a JSON)
     */
    @GetMapping
    @Operation(summary = "Listar todas las películas")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Listado de películas"))
    public List<PeliculaResponse> listar() {
        return mapper.aRespuestas(service.listar());
    }

    /**
     * Obtiene una película: {@code GET /api/v1/jdbc/peliculas/{codigo}}.
     * {@code @PathVariable} copia el {@code {codigo}} de la URL y {@code @Pattern}
     * comprueba que sea {@code PELnnn}; si no, Spring responde 400.
     *
     * @param codigo código de la película
     * @return 200 con la película, o 404 con {@code ProblemDetail} si no existe
     */
    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener una película por su código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "400", description = "Código con formato inválido"),
            @ApiResponse(responseCode = "404", description = "No existe ninguna película con ese código")
    })
    public ResponseEntity<?> obtener(@PathVariable @Pattern(regexp = PATRON_CODIGO) String codigo) {
        Optional<Pelicula> pelicula = service.obtener(codigo);
        if (pelicula.isEmpty()) {
            return noExiste(codigo);
        }
        return ResponseEntity.ok(mapper.aRespuesta(pelicula.get()));
    }

    /**
     * Crea una película: {@code POST /api/v1/jdbc/peliculas}.
     * {@code @RequestBody} convierte el JSON en un {@link PeliculaRequest} y {@code @Valid}
     * aplica sus reglas (patrón, título, rangos); si alguna falla, Spring responde 400.
     *
     * @param peticion cuerpo con código, título, año y duración
     * @return 201 con la película creada, o 409 con {@code ProblemDetail} si el código ya existe
     */
    @PostMapping
    @Operation(summary = "Crear una película")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "400", description = "Cuerpo inválido"),
            @ApiResponse(responseCode = "409", description = "Ya existe una película con ese código")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody PeliculaRequest peticion) {
        Optional<Pelicula> creada = service.crear(mapper.aDatosCreacion(peticion));
        if (creada.isEmpty()) {
            ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                    "Ya existe una película con el código " + peticion.codigo());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.aRespuesta(creada.get()));
    }

    /**
     * Sustituye los datos de una película: {@code PUT /api/v1/jdbc/peliculas/{codigo}}.
     * El código sale de la URL ({@code @PathVariable} + {@code @Pattern}) y el resto
     * del cuerpo ({@code @Valid @RequestBody}). Nunca crea películas nuevas.
     *
     * @param codigo   código de la película
     * @param peticion cuerpo con título, año y duración nuevos
     * @return 200 con la película actualizada, o 404 con {@code ProblemDetail} si no existe
     */
    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar una película")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película actualizada"),
            @ApiResponse(responseCode = "400", description = "Código o cuerpo inválido"),
            @ApiResponse(responseCode = "404", description = "No existe ninguna película con ese código")
    })
    public ResponseEntity<?> actualizar(@PathVariable @Pattern(regexp = PATRON_CODIGO) String codigo,
                                        @Valid @RequestBody PeliculaEdicionRequest peticion) {
        Optional<Pelicula> actualizada = service.actualizar(codigo, mapper.aDatosEdicion(peticion));
        if (actualizada.isEmpty()) {
            return noExiste(codigo);
        }
        return ResponseEntity.ok(mapper.aRespuesta(actualizada.get()));
    }

    /**
     * Borra una película: {@code DELETE /api/v1/jdbc/peliculas/{codigo}}.
     * Aquí no interviene ningún mapper: solo viaja el código.
     *
     * @param codigo código de la película
     * @return 204 sin cuerpo si se ha borrado, o 404 con {@code ProblemDetail} si no existe
     */
    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar una película")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Película eliminada"),
            @ApiResponse(responseCode = "400", description = "Código con formato inválido"),
            @ApiResponse(responseCode = "404", description = "No existe ninguna película con ese código")
    })
    public ResponseEntity<?> eliminar(@PathVariable @Pattern(regexp = PATRON_CODIGO) String codigo) {
        if (service.eliminar(codigo)) {
            return ResponseEntity.noContent().build();
        }
        return noExiste(codigo);
    }

    // 404 con ProblemDetail que nombra el código (lo usan GET, PUT y DELETE)
    private ResponseEntity<ProblemDetail> noExiste(String codigo) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "No existe ninguna película con el código " + codigo);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
    }
}
