package com.garzonpro.Status.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Status.dto.NotificacionRequestDTO;
import com.garzonpro.Status.dto.NotificacionResponseDTO;
import com.garzonpro.Status.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    private NotificacionRequestDTO requestDTO;
    private NotificacionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new NotificacionRequestDTO();
        requestDTO.setIdGarzonDestino(1L);
        requestDTO.setMensaje("El plato para la Mesa 4 está listo");

        responseDTO = new NotificacionResponseDTO();
        responseDTO.setIdNotificacion(100L);
        responseDTO.setIdGarzonDestino(1L);
        responseDTO.setMensaje("El plato para la Mesa 4 está listo");
        responseDTO.setLeido(false);
    }

    // ---------- POST /status/notificar ----------

    @Test
    void enviarNotificacion_DeberiaRetornarStatusCreated() throws Exception {
        when(notificacionService.enviarNotificacion(any(NotificacionRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/status/notificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idNotificacion").value(100L))
                .andExpect(jsonPath("$.idGarzonDestino").value(1L))
                .andExpect(jsonPath("$.mensaje").value("El plato para la Mesa 4 está listo"))
                .andExpect(jsonPath("$.leido").value(false));
    }

    @Test
    void enviarNotificacion_SinMensaje_DeberiaRetornarBadRequest() throws Exception {
        // El DTO tiene @NotBlank en mensaje, así que un mensaje vacío debe fallar la validación
        requestDTO.setMensaje("");

        mockMvc.perform(post("/status/notificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET /status/pendientes/{idGarzon} ----------

    @Test
    void obtenerPendientes_DeberiaRetornarListaDeNotificaciones() throws Exception {
        List<NotificacionResponseDTO> listaPendientes = Arrays.asList(responseDTO);
        when(notificacionService.obtenerPendientesPorGarzon(1L)).thenReturn(listaPendientes);

        mockMvc.perform(get("/status/pendientes/{idGarzon}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idNotificacion").value(100L))
                .andExpect(jsonPath("$[0].leido").value(false));
    }

    @Test
    void obtenerPendientes_SinPendientes_DeberiaRetornarListaVacia() throws Exception {
        when(notificacionService.obtenerPendientesPorGarzon(2L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/status/pendientes/{idGarzon}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- GET /status/garzon/{idGarzon} ----------

    @Test
    void obtenerTodas_DeberiaRetornarHistorialCompleto() throws Exception {
        NotificacionResponseDTO responseLeida = new NotificacionResponseDTO();
        responseLeida.setIdNotificacion(99L);
        responseLeida.setIdGarzonDestino(1L);
        responseLeida.setMensaje("Pedido Mesa 2 entregado");
        responseLeida.setLeido(true);

        List<NotificacionResponseDTO> historial = Arrays.asList(responseLeida, responseDTO);
        when(notificacionService.obtenerTodasPorGarzon(1L)).thenReturn(historial);

        mockMvc.perform(get("/status/garzon/{idGarzon}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].leido").value(true))
                .andExpect(jsonPath("$[1].leido").value(false));
    }

    @Test
    void obtenerTodas_SinHistorial_DeberiaRetornarListaVacia() throws Exception {
        when(notificacionService.obtenerTodasPorGarzon(3L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/status/garzon/{idGarzon}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- PUT /status/leer/{id} ----------

    @Test
    void marcarComoLeida_DeberiaModificarEstadoYRetornarOk() throws Exception {
        NotificacionResponseDTO responseModificada = new NotificacionResponseDTO();
        responseModificada.setIdNotificacion(100L);
        responseModificada.setIdGarzonDestino(1L);
        responseModificada.setMensaje("El plato para la Mesa 4 está listo");
        responseModificada.setLeido(true);

        when(notificacionService.marcarComoLeida(100L)).thenReturn(responseModificada);

        mockMvc.perform(put("/status/leer/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNotificacion").value(100L))
                .andExpect(jsonPath("$.leido").value(true));
    }

    @Test
    void marcarComoLeida_DeberiaInvocarAlServicioExactamenteUnaVez() throws Exception {
        NotificacionResponseDTO responseModificada = new NotificacionResponseDTO();
        responseModificada.setIdNotificacion(100L);
        responseModificada.setLeido(true);

        when(notificacionService.marcarComoLeida(100L)).thenReturn(responseModificada);

        mockMvc.perform(put("/status/leer/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(notificacionService, Mockito.times(1)).marcarComoLeida(100L);
    }
}