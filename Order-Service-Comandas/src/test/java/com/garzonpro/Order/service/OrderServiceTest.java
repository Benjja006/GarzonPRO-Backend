package com.garzonpro.Order.service;

import com.garzonpro.Order.model.DetallePedido;
import com.garzonpro.Order.model.Pedido;
import com.garzonpro.Order.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PedidoRepository pedidoRepo;

    @InjectMocks
    private OrderService orderService;

    private Pedido pedidoAbierto;

    @BeforeEach
    void setUp() {
        pedidoAbierto = new Pedido();
        pedidoAbierto.setIdPedido(1L);
        pedidoAbierto.setIdMesa(10L);
        pedidoAbierto.setEstadoGeneral("Abierto");
        pedidoAbierto.setTotalParcial(0.0);
    }

    @Nested
    @DisplayName("abrirPedido")
    class AbrirPedido {

        @Test
        @DisplayName("Crea un pedido nuevo en estado Abierto con total en 0")
        void creaPedidoAbierto() {
            when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

            Pedido resultado = orderService.abrirPedido(10L);

            assertThat(resultado.getIdMesa()).isEqualTo(10L);
            assertThat(resultado.getEstadoGeneral()).isEqualTo("Abierto");
            assertThat(resultado.getTotalParcial()).isEqualTo(0.0);

            ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
            verify(pedidoRepo).save(captor.capture());
            assertThat(captor.getValue().getIdMesa()).isEqualTo(10L);
        }
    }

    @Nested
    @DisplayName("agregarItem")
    class AgregarItem {

        @Test
        @DisplayName("Agrega el detalle, asocia el pedido y recalcula el total correctamente")
        void agregaDetalleYRecalculaTotal() {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdPlato(200L);
            detalle.setNombrePlato("Empanada");
            detalle.setCantidad(3);
            detalle.setPrecioUnitarioAlMomentoVenta(1500.0);

            when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedidoAbierto));
            when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

            Pedido resultado = orderService.agregarItem(1L, detalle);

            assertThat(resultado.getDetalles()).hasSize(1);
            assertThat(resultado.getTotalParcial()).isEqualTo(4500.0); // 3 * 1500
            assertThat(detalle.getPedido()).isEqualTo(pedidoAbierto);
        }

        @Test
        @DisplayName("Con varios detalles, el total es la suma de cantidad * precio de cada uno")
        void variosDetalles_sumaCorrecta() {
            DetallePedido d1 = new DetallePedido();
            d1.setCantidad(2);
            d1.setPrecioUnitarioAlMomentoVenta(1000.0);
            pedidoAbierto.agregarDetalle(d1);

            DetallePedido d2 = new DetallePedido();
            d2.setCantidad(1);
            d2.setPrecioUnitarioAlMomentoVenta(2500.0);

            when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedidoAbierto));
            when(pedidoRepo.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

            Pedido resultado = orderService.agregarItem(1L, d2);

            // (2*1000) + (1*2500) = 4500
            assertThat(resultado.getTotalParcial()).isEqualTo(4500.0);
            assertThat(resultado.getDetalles()).hasSize(2);
        }

        @Test
        @DisplayName("Si el pedido no existe, lanza NoSuchElementException y no guarda nada")
        void pedidoNoExiste_lanzaExcepcion() {
            DetallePedido detalle = new DetallePedido();
            when(pedidoRepo.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.agregarItem(999L, detalle))
                    .isInstanceOf(NoSuchElementException.class);

            verify(pedidoRepo, never()).save(any());
        }
    }
}