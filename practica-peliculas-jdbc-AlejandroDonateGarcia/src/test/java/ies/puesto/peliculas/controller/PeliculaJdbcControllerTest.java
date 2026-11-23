package ies.puesto.peliculas.controller;

import ies.puesto.peliculas.controller.mapper.PeliculaApiMapper;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.PeliculaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de la API con MockMvc: una por cada fila de la tabla de estados del README.
 * El servicio es un mock, así que no se toca la base de datos.
 *
 * @author AlejandroDonGar
 */
@WebMvcTest(PeliculaJdbcController.class)
@Import(PeliculaApiMapper.class)
@ActiveProfiles("jdbc")
class PeliculaJdbcControllerTest {

    private static final String URL = "/api/v1/jdbc/peliculas";
    private static final Pelicula CHIHIRO = new Pelicula("PEL001", "El viaje de Chihiro", 2001, 125);
    private static final String CUERPO_POST =
            "{\"codigo\":\"PEL003\",\"titulo\":\"Ponyo\",\"anioEstreno\":2008,\"duracionMinutos\":101}";
    private static final String CUERPO_PUT =
            "{\"titulo\":\"Chihiro\",\"anioEstreno\":2001,\"duracionMinutos\":125}";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PeliculaService service;

    @Test
    void listarDevuelve200() throws Exception {
        when(service.listar()).thenReturn(List.of(CHIHIRO));
        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("PEL001"));
    }

    @Test
    void obtenerDevuelve200() throws Exception {
        when(service.obtener("PEL001")).thenReturn(Optional.of(CHIHIRO));
        mvc.perform(get(URL + "/PEL001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El viaje de Chihiro"));
    }

    @Test
    void obtenerConCodigoInvalidoDevuelve400() throws Exception {
        mvc.perform(get(URL + "/XYZ")).andExpect(status().isBadRequest());
    }

    @Test
    void obtenerInexistenteDevuelve404ConProblemDetail() throws Exception {
        when(service.obtener("PEL999")).thenReturn(Optional.empty());
        mvc.perform(get(URL + "/PEL999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No existe ninguna película con el código PEL999"));
    }

    @Test
    void crearDevuelve201() throws Exception {
        when(service.crear(any())).thenReturn(Optional.of(new Pelicula("PEL003", "Ponyo", 2008, 101)));
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(CUERPO_POST))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("PEL003"));
    }

    @Test
    void crearConCuerpoInvalidoDevuelve400() throws Exception {
        // título en blanco, año fuera de rango y falta la duración
        String cuerpo = "{\"codigo\":\"PEL003\",\"titulo\":\"  \",\"anioEstreno\":1800}";
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());
        verify(service, never()).crear(any());
    }

    @Test
    void crearDuplicadaDevuelve409() throws Exception {
        when(service.crear(any())).thenReturn(Optional.empty());
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(CUERPO_POST))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Ya existe una película con el código PEL003"));
    }

    @Test
    void actualizarDevuelve200() throws Exception {
        when(service.actualizar(eq("PEL001"), any()))
                .thenReturn(Optional.of(new Pelicula("PEL001", "Chihiro", 2001, 125)));
        mvc.perform(put(URL + "/PEL001").contentType(MediaType.APPLICATION_JSON).content(CUERPO_PUT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Chihiro"));
    }

    @Test
    void actualizarConCodigoInvalidoDevuelve400() throws Exception {
        mvc.perform(put(URL + "/PEL1").contentType(MediaType.APPLICATION_JSON).content(CUERPO_PUT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarConCuerpoInvalidoDevuelve400() throws Exception {
        String cuerpo = "{\"titulo\":\"Chihiro\",\"anioEstreno\":2001,\"duracionMinutos\":0}";
        mvc.perform(put(URL + "/PEL001").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarInexistenteDevuelve404() throws Exception {
        when(service.actualizar(eq("PEL999"), any())).thenReturn(Optional.empty());
        mvc.perform(put(URL + "/PEL999").contentType(MediaType.APPLICATION_JSON).content(CUERPO_PUT))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarDevuelve204SinCuerpo() throws Exception {
        when(service.eliminar("PEL001")).thenReturn(true);
        mvc.perform(delete(URL + "/PEL001"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void eliminarConCodigoInvalidoDevuelve400() throws Exception {
        mvc.perform(delete(URL + "/pel001")).andExpect(status().isBadRequest());
    }

    @Test
    void eliminarInexistenteDevuelve404() throws Exception {
        when(service.eliminar("PEL999")).thenReturn(false);
        mvc.perform(delete(URL + "/PEL999")).andExpect(status().isNotFound());
    }
}
