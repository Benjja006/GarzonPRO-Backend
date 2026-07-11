package com.garzonpro.Order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Order.model.DetallePedido;
import com.garzonpro.Order.model.Pedido;
import com.garzonpro.Order.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de la capa web de OrderController.
 * IMPORTANTE: este controller NO usa OrderService, opera directamente
 * sobre PedidoRepository. Por eso el mock aquí es del repositorio, no del
 * servicio (a diferencia de KdsController).
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoRepository repository;

    @Nested
    @DisplayName("POST /orders/abrir/{idMesa}")
    class AbrirPedido {

        @Test
        @DisplayName("Crea y guarda un pedido vacío asociado a la mesa")
        void creaPedidoParaLaMesa() throws Exception {
            when(repository.save(any(Pedido.class))).thenAnswer(inv -> {
                Pedido p = inv.getArgument(0);
                p.setIdPedido(1L);
                return p;
            });

            mockMvc.perform(post("/orders/abrir/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPedido").value(1))
                    .andExpect(jsonPath("$.idMesa").value(10));
        }
    }

    @Nested
    @DisplayName("POST /orders/{idPedido}/agregar")
    class AgregarPlatos {

        @Test
        @DisplayName("Con pedido existente, agrega el detalle y devuelve el pedido actualizado")
        void agregaDetalleAlPedido() throws Exception {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(1L);
            pedido.setIdMesa(10L);

            DetallePedido detalle = new DetallePedido();
            detalle.setIdPlato(200L);
            detalle.setNombrePlato("Pizza");
            detalle.setCantidad(2);
            detalle.setPrecioUnitarioAlMomentoVenta(5000.0);

            when(repository.findById(1L)).thenReturn(Optional.of(pedido));
            when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/orders/1/agregar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(detalle)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.detalles[0].nombrePlato").value("Pizza"));
        }

        @Test
        @DisplayName("Con pedido inexistente, la excepción se propaga (no hay manejador para NoSuchElementException)")
        void pedidoNoExiste_propagaExcepcion() {
            DetallePedido detalle = new DetallePedido();

            when(repository.findById(999L)).thenReturn(Optional.empty());

            // Como GlobalExceptionHandler no tiene un @ExceptionHandler para
            // NoSuchElementException, MockMvc no la convierte en una respuesta
            // 500: la relanza directamente (así se comporta MockMvc con
            // cualquier excepción no manejada; un servidor real sí devolvería
            // un 500 gracias al mecanismo de error-page del contenedor).
            Exception ex = assertThrows(Exception.class, () ->
                    mockMvc.perform(post("/orders/999/agregar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(detalle))));

            assertThat(ex.getCause()).isInstanceOf(NoSuchElementException.class);
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("GET /orders/{id}")
    class ObtenerPedido {

        @Test
        @DisplayName("Devuelve el pedido cuando existe")
        void devuelvePedidoExistente() throws Exception {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(1L);
            pedido.setIdMesa(10L);

            when(repository.findById(1L)).thenReturn(Optional.of(pedido));

            mockMvc.perform(get("/orders/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPedido").value(1));
        }

        @Test
        @DisplayName("Si el pedido no existe, la excepción se propaga (mismo gap de manejo de errores)")
        void pedidoNoExiste_propagaExcepcion() {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            Exception ex = assertThrows(Exception.class, () ->
                    mockMvc.perform(get("/orders/999")));

            assertThat(ex.getCause()).isInstanceOf(NoSuchElementException.class);
        }
    }
}