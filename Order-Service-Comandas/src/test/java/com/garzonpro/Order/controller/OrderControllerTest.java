package com.garzonpro.Order.controller;

import com.garzonpro.Order.model.Pedido;
import com.garzonpro.Order.repository.PedidoRepository;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoRepository pedidoRepository;

    @Test
    public void testAbrirPedidoConMockito() throws Exception {
        Pedido pedidoMock = new Pedido();
        pedidoMock.setIdPedido(123L);
        pedidoMock.setIdMesa(5L);
        pedidoMock.setEstadoGeneral("ABIERTO");

        // Cuando el controlador haga repository.save(), Mockito devolverá nuestro pedido Mock
        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        mockMvc.perform(post("/orders/abrir/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(123))
                .andExpect(jsonPath("$.estadoGeneral").value("ABIERTO"));
    }
}