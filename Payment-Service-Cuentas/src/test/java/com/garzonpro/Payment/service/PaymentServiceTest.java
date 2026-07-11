package com.garzonpro.Payment.service;

import com.garzonpro.Payment.client.OrderClient;
import com.garzonpro.Payment.client.dto.PedidoResponseDTO;
import com.garzonpro.Payment.dto.PagoRequestDTO;
import com.garzonpro.Payment.dto.PagoResponseDTO;
import com.garzonpro.Payment.exception.PagoNotFoundException;
import com.garzonpro.Payment.model.Pago;
import com.garzonpro.Payment.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de PagoService.
 * Se construye manualmente (sin @InjectMocks) porque el constructor es
 * generado por @RequiredArgsConstructor de Lombok con dos dependencias finales.
 */
@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private OrderClient orderClient;

    private PagoService pagoService;

    private PagoRequestDTO requestValido;
    private PedidoResponseDTO pedidoAbierto;

    @BeforeEach
    void setUp() {
        pagoService = new PagoService(pagoRepository, orderClient);

        requestValido = new PagoRequestDTO();
        requestValido.setIdPedido(1L);
        requestValido.setMontoTotal(10000.0);
        requestValido.setMetodoPago("EFECTIVO");

        pedidoAbierto = new PedidoResponseDTO();
        pedidoAbierto.setIdPedido(1L);
        pedidoAbierto.setIdMesa(5L);
        pedidoAbierto.setEstadoGeneral("ABIERTO");
        pedidoAbierto.setTotalParcial(10000.0);
    }

    @Nested
    @DisplayName("procesarPago")
    class ProcesarPago {

        @Test
        @DisplayName("Con pedido ABIERTO, registra el pago y notifica a Order-Service")
        void pedidoAbierto_registraPagoYNotifica() {
            when(orderClient.obtenerPedido(1L)).thenReturn(ResponseEntity.ok(pedidoAbierto));
            when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
                Pago p = inv.getArgument(0);
                p.setIdPago(100L);
                return p;
            });
            when(orderClient.actualizarEstadoPedido(1L, "PAGADO"))
                    .thenReturn(ResponseEntity.ok().build());

            PagoResponseDTO resultado = pagoService.procesarPago(requestValido);

            assertThat(resultado.getIdPago()).isEqualTo(100L);
            assertThat(resultado.getEstadoPago()).isEqualTo("COMPLETADO");

            verify(pagoRepository).save(any(Pago.class));
            verify(orderClient).actualizarEstadoPedido(1L, "PAGADO");
        }

        @Test
        @DisplayName("Si Order-Service no responde (body null), lanza IllegalStateException y no guarda el pago")
        void ordenServiceSinRespuesta_lanzaExcepcion() {
            when(orderClient.obtenerPedido(1L)).thenReturn(ResponseEntity.ok(null));

            assertThatThrownBy(() -> pagoService.procesarPago(requestValido))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("no responde");

            verify(pagoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Si el pedido no está ABIERTO, lanza IllegalArgumentException y no guarda el pago")
        void pedidoNoAbierto_lanzaExcepcion() {
            pedidoAbierto.setEstadoGeneral("PAGADO");
            when(orderClient.obtenerPedido(1L)).thenReturn(ResponseEntity.ok(pedidoAbierto));

            assertThatThrownBy(() -> pagoService.procesarPago(requestValido))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("PAGADO");

            verify(pagoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Si falla la notificación post-pago, el pago igual queda registrado")
        void fallaNotificacionPostPago_pagoSigueRegistrado() {
            when(orderClient.obtenerPedido(1L)).thenReturn(ResponseEntity.ok(pedidoAbierto));
            when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> {
                Pago p = inv.getArgument(0);
                p.setIdPago(100L);
                return p;
            });
            when(orderClient.actualizarEstadoPedido(1L, "PAGADO"))
                    .thenThrow(new RuntimeException("Order-Service caído"));

            PagoResponseDTO resultado = pagoService.procesarPago(requestValido);

            // El try/catch del service debe absorber el fallo de la notificación post-pago
            assertThat(resultado.getIdPago()).isEqualTo(100L);
            verify(pagoRepository).save(any(Pago.class));
        }
    }

    @Nested
    @DisplayName("Consultas de pagos")
    class Consultas {

        @Test
        @DisplayName("obtenerPorId devuelve el DTO cuando el pago existe")
        void obtenerPorId_existente() {
            Pago pago = new Pago();
            pago.setIdPago(1L);
            pago.setIdPedido(1L);
            pago.setMontoTotal(5000.0);
            pago.setMetodoPago("TARJETA");
            pago.setEstadoPago("COMPLETADO");

            when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

            PagoResponseDTO resultado = pagoService.obtenerPorId(1L);

            assertThat(resultado.getIdPago()).isEqualTo(1L);
            assertThat(resultado.getMetodoPago()).isEqualTo("TARJETA");
        }

        @Test
        @DisplayName("obtenerPorId lanza PagoNotFoundException si el pago no existe")
        void obtenerPorId_noExistente_lanzaExcepcion() {
            when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> pagoService.obtenerPorId(999L))
                    .isInstanceOf(PagoNotFoundException.class)
                    .hasMessageContaining("999");
        }

        @Test
        @DisplayName("obtenerPorPedido devuelve todos los pagos asociados a un pedido")
        void obtenerPorPedido_devuelveLista() {
            Pago pago1 = new Pago();
            pago1.setIdPago(1L);
            pago1.setIdPedido(7L);

            when(pagoRepository.findByIdPedido(7L)).thenReturn(List.of(pago1));

            List<PagoResponseDTO> resultado = pagoService.obtenerPorPedido(7L);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdPedido()).isEqualTo(7L);
        }

        @Test
        @DisplayName("obtenerTodos devuelve la lista completa mapeada a DTO")
        void obtenerTodos_devuelveListaCompleta() {
            Pago pago1 = new Pago();
            pago1.setIdPago(1L);
            Pago pago2 = new Pago();
            pago2.setIdPago(2L);

            when(pagoRepository.findAll()).thenReturn(List.of(pago1, pago2));

            List<PagoResponseDTO> resultado = pagoService.obtenerTodos();

            assertThat(resultado).hasSize(2);
        }
    }
}