package com.garzonpro.Payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Payment.exception.GlobalExceptionHandler;
import com.garzonpro.Payment.model.Pago;
import com.garzonpro.Payment.repository.PagoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de la capa web de PaymentController.
 * IMPORTANTE: este controller NO usa PagoService, opera directamente sobre
 * PagoRepository con la entidad Pago (no con los DTOs PagoRequestDTO/PagoResponseDTO
 * que sí usa PagoService). Por eso aquí no se valida método de pago ni se
 * consulta a Order-Service — esa lógica vive en PagoService pero no se ejecuta
 * desde este endpoint tal como está el código hoy.
 */
@WebMvcTest(PaymentController.class)
@Import(GlobalExceptionHandler.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagoRepository repository;

    @Nested
    @DisplayName("POST /payments/procesar")
    class ProcesarPago {

        @Test
        @DisplayName("Guarda el pago recibido tal cual y lo devuelve")
        void guardaYDevuelvePago() throws Exception {
            Pago pago = new Pago();
            pago.setIdPedido(1L);
            pago.setMontoTotal(5000.0);
            pago.setMetodoPago("EFECTIVO");

            when(repository.save(any(Pago.class))).thenAnswer(inv -> {
                Pago p = inv.getArgument(0);
                p.setIdPago(1L);
                return p;
            });

            mockMvc.perform(post("/payments/procesar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pago)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPago").value(1));
        }
    }

    @Nested
    @DisplayName("GET /payments/historial")
    class ObtenerTodos {

        @Test
        @DisplayName("Devuelve todos los pagos registrados")
        void devuelveHistorialCompleto() throws Exception {
            Pago pago = new Pago();
            pago.setIdPago(1L);

            when(repository.findAll()).thenReturn(List.of(pago));

            mockMvc.perform(get("/payments/historial"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].idPago").value(1));
        }
    }

    @Nested
    @DisplayName("GET /payments/{id}")
    class ObtenerPago {

        @Test
        @DisplayName("Devuelve el pago cuando existe")
        void devuelvePagoExistente() throws Exception {
            Pago pago = new Pago();
            pago.setIdPago(1L);
            pago.setMetodoPago("TARJETA");

            when(repository.findById(1L)).thenReturn(Optional.of(pago));

            mockMvc.perform(get("/payments/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.metodoPago").value("TARJETA"));
        }

        @Test
        @DisplayName("Si el pago no existe, el GlobalExceptionHandler lo captura y responde 500 controlado")
        void pagoNoExiste_manejadoPorHandlerGenerico() throws Exception {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            // findById().orElseThrow() sin mensaje lanza NoSuchElementException,
            // que cae en el @ExceptionHandler(Exception.class) genérico de
            // GlobalExceptionHandler (no en el de PagoNotFoundException,
            // que este endpoint nunca lanza al no pasar por PagoService).
            mockMvc.perform(get("/payments/999"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error")
                            .value("Error interno del servidor. Contacte al administrador."));
        }
    }
}