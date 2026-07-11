package com.garzonpro.Kds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import com.garzonpro.Kds.service.KdsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de la capa web de KdsController.
 * Nota: el controller inyecta tanto KdsService como KdsRepository directamente
 * (dos endpoints usan el repositorio sin pasar por el service), así que se
 * mockean ambos.
 */
@WebMvcTest(KdsController.class)
class KdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KdsService kdsService;

    @MockBean
    private KdsRepository repository;

    @Nested
    @DisplayName("GET /kds/pendientes")
    class ObtenerPendientes {

        @Test
        @DisplayName("Devuelve la lista de tickets cuyo estado no es LISTO")
        void devuelveTicketsPendientes() throws Exception {
            TicketCocina t1 = new TicketCocina();
            t1.setIdTicket(1L);
            t1.setEstadoGeneral("En Preparación");

            when(repository.findByEstadoGeneralNot("LISTO")).thenReturn(List.of(t1));

            mockMvc.perform(get("/kds/pendientes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].idTicket").value(1))
                    .andExpect(jsonPath("$[0].estadoGeneral").value("En Preparación"));

            verify(repository).findByEstadoGeneralNot("LISTO");
        }
    }

    @Nested
    @DisplayName("POST /kds/nuevo")
    class RecibirTicket {

        @Test
        @DisplayName("Guarda el ticket recibido y lo devuelve")
        void guardaYDevuelveTicket() throws Exception {
            TicketCocina nuevo = new TicketCocina();
            nuevo.setEstadoGeneral("En Preparación");

            when(repository.save(any(TicketCocina.class))).thenAnswer(inv -> {
                TicketCocina t = inv.getArgument(0);
                t.setIdTicket(5L);
                return t;
            });

            mockMvc.perform(post("/kds/nuevo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevo)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idTicket").value(5));
        }
    }

    @Nested
    @DisplayName("PUT /kds/listo/{id}")
    class MarcarListo {

        @Test
        @DisplayName("Delega en KdsService y devuelve el ticket actualizado")
        void marcaListoYDevuelveTicket() throws Exception {
            TicketCocina listo = new TicketCocina();
            listo.setIdTicket(1L);
            listo.setEstadoGeneral("Listo");

            when(kdsService.marcarTicketListo(1L)).thenReturn(listo);

            mockMvc.perform(put("/kds/listo/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoGeneral").value("Listo"));

            verify(kdsService).marcarTicketListo(1L);
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("Si el ticket no existe, la excepción se propaga (no hay manejador para RuntimeException)")
        void ticketNoExiste_propagaExcepcion() {
            when(kdsService.marcarTicketListo(99L))
                    .thenThrow(new RuntimeException("Ticket no encontrado"));

            // GlobalExceptionHandler de KDS solo maneja MethodArgumentNotValidException,
            // así que esta RuntimeException no tiene @ExceptionHandler que la capture.
            // MockMvc, a diferencia de un servidor real desplegado, no la convierte
            // en una respuesta 500: la relanza directamente al test.
            Exception ex = assertThrows(Exception.class, () ->
                    mockMvc.perform(put("/kds/listo/99")));

            assertThat(ex.getCause()).isInstanceOf(RuntimeException.class)
                    .hasMessage("Ticket no encontrado");
        }
    }
}