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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        // Inicializamos datos de prueba comunes
        requestDTO = new NotificacionRequestDTO();
        requestDTO.setIdGarzonDestino(1L);
        requestDTO.setMensaje("El plato para la Mesa 4 está listo");

        responseDTO = new NotificacionResponseDTO();
        responseDTO.setIdNotificacion(100L);
        responseDTO.setIdGarzonDestino(1L);
        responseDTO.setMensaje("El plato para la Mesa 4 está listo");
        responseDTO.setLeido(false);
    }

    @Test
    void enviarNotificacion_DeberiaRetornarStatusCreated() throws Exception {
        // Given
        when(notificacionService.enviarNotificacion(any(NotificacionRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/status/notificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.idGarzonDestino").value(1L))
                .andExpect(jsonPath("$.mensaje").value("El plato para la Mesa 4 está listo"))
                .andExpect(jsonPath("$.leida").value(false));
    }

    @Test
    void obtenerPendientes_DeberiaRetornarListaDeNotificaciones() throws Exception {
        // Given
        List<NotificacionResponseDTO> listaPendientes = Arrays.asList(responseDTO);
        when(notificacionService.obtenerPendientesPorGarzon(1L)).thenReturn(listaPendientes);

        // When & Then
        mockMvc.perform(get("/status/pendientes/{idGarzon}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(100L))
                .andExpect(jsonPath("$[0].leida").value(false));
    }

    @Test
    void obtenerTodas_DeberiaRetornarHistorialCompleto() throws Exception {
        // Given
        NotificacionResponseDTO responseLeida = new NotificacionResponseDTO();
        responseLeida.setIdNotificacion(99L);
        responseLeida.setIdGarzonDestino(1L);
        responseLeida.setMensaje("Pedido Mesa 2 entregado");
        responseLeida.setLeido(true);

        List<NotificacionResponseDTO> historial = Arrays.asList(responseLeida, responseDTO);
        when(notificacionService.obtenerTodasPorGarzon(1L)).thenReturn(historial);

        // When & Then
        mockMvc.perform(get("/status/garzon/{idGarzon}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].leida").value(true))
                .andExpect(jsonPath("$[1].leida").value(false));
    }

    @Test
    void marcarComoLeida_DeberiaModificarEstadoYRetornarOk() throws Exception {
        // Given
        NotificacionResponseDTO responseModificada = responseDTO;
        responseModificada.setLeido(true); // Cambiamos a true para simular el cambio

        when(notificacionService.marcarComoLeida(100L)).thenReturn(responseModificada);

        // When & Then
        mockMvc.perform(put("/status/leer/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.leida").value(true));
    }
}