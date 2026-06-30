package com.garzonpro.Payment.controller;

import com.garzonpro.Payment.model.Pago;
import com.garzonpro.Payment.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoRepository pagoRepository;

    @Test
    public void testProcesarPagoConMockito() throws Exception {
        Pago pagoMock = new Pago();
        pagoMock.setIdPago(1L);
        pagoMock.setIdPedido(2L);
        pagoMock.setMontoTotal(15500.0);
        pagoMock.setEstadoPago("COMPLETADO");

        Mockito.when(pagoRepository.save(any(Pago.class))).thenReturn(pagoMock);

        String jsonRequestBody = "{\"idPedido\":2,\"montoTotal\":15500.0,\"metodoPago\":\"EFECTIVO\"}";

        mockMvc.perform(post("/payments/procesar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPago").value(1))
                .andExpect(jsonPath("$.estadoPago").value("COMPLETADO"));
    }
}