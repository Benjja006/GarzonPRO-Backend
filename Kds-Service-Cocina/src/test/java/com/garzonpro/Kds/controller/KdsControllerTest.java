package com.garzonpro.Kds.controller;

import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import com.garzonpro.Kds.service.KdsService;
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

@WebMvcTest(KdsController.class)
public class KdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KdsRepository kdsRepository; // Mapeado al repositorio real de tu captura

    @MockBean
    private KdsService kdsService; // También lo mockeamos para que Spring no se queje al levantar el controlador

    @Test
    public void testCrearTicketNuevoConMockito() throws Exception {
        // Creamos el objeto simulado con el modelo real: TicketCocina
        TicketCocina ticketMock = new TicketCocina();
        ticketMock.setIdTicket(99L);
        ticketMock.setEstadoGeneral("PENDIENTE");

        // Configuramos la regla de Mockito usando el repositorio correcto
        Mockito.when(kdsRepository.save(any(TicketCocina.class))).thenReturn(ticketMock);

        String jsonTicket = "{\"estadoGeneral\":\"PENDIENTE\"}";

        mockMvc.perform(post("/kds/nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTicket))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(99))
                .andExpect(jsonPath("$.estadoGeneral").value("PENDIENTE"));
    }
}