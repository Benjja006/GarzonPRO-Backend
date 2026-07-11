package com.garzonpro.Inventory.service;

import com.garzonpro.Inventory.dto.StockDTO;
import com.garzonpro.Inventory.model.StockPlato;
import com.garzonpro.Inventory.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de StockService.
 * No se levanta el contexto de Spring: se mockea el repositorio
 * con Mockito y se inyecta directamente en el servicio.
 */
@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepo;

    @InjectMocks
    private StockService stockService;

    private StockPlato stockExistente;

    @BeforeEach
    void setUp() {
        stockExistente = new StockPlato();
        stockExistente.setIdStock(1L);
        stockExistente.setIdPlato(100L);
        stockExistente.setCantidadRestante(10);
        stockExistente.setDisponibleParaVenta(true);
    }

    // ---------- inicializarStock ----------

    @Nested
    @DisplayName("inicializarStock")
    class InicializarStock {

        @Test
        @DisplayName("Con cantidad positiva, marca el plato como disponible para venta")
        void inicializarStock_cantidadPositiva_disponibleParaVentaTrue() {
            StockDTO dto = new StockDTO();
            dto.setIdPlato(100L);
            dto.setCantidad(20);

            stockService.inicializarStock(dto);

            ArgumentCaptor<StockPlato> captor = ArgumentCaptor.forClass(StockPlato.class);
            verify(stockRepo, times(1)).save(captor.capture());

            StockPlato guardado = captor.getValue();
            assertThat(guardado.getIdPlato()).isEqualTo(100L);
            assertThat(guardado.getCantidadRestante()).isEqualTo(20);
            assertThat(guardado.getDisponibleParaVenta()).isTrue();
        }

        @Test
        @DisplayName("Con cantidad cero, marca el plato como NO disponible para venta")
        void inicializarStock_cantidadCero_disponibleParaVentaFalse() {
            StockDTO dto = new StockDTO();
            dto.setIdPlato(200L);
            dto.setCantidad(0);

            stockService.inicializarStock(dto);

            ArgumentCaptor<StockPlato> captor = ArgumentCaptor.forClass(StockPlato.class);
            verify(stockRepo).save(captor.capture());

            assertThat(captor.getValue().getDisponibleParaVenta()).isFalse();
        }
    }

    // ---------- verificarStock ----------

    @Nested
    @DisplayName("verificarStock")
    class VerificarStock {

        @Test
        @DisplayName("Devuelve true si hay stock suficiente")
        void verificarStock_stockSuficiente_true() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            boolean resultado = stockService.verificarStock(100L, 5);

            assertThat(resultado).isTrue();
            verify(stockRepo).findByIdPlato(100L);
        }

        @Test
        @DisplayName("Devuelve false si la cantidad pedida supera el stock restante")
        void verificarStock_stockInsuficiente_false() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            boolean resultado = stockService.verificarStock(100L, 50);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Devuelve false si el plato no existe en stock")
        void verificarStock_platoNoExiste_false() {
            when(stockRepo.findByIdPlato(999L)).thenReturn(Optional.empty());

            boolean resultado = stockService.verificarStock(999L, 1);

            assertThat(resultado).isFalse();
        }

        @Test
        @DisplayName("Devuelve true si la cantidad pedida es exactamente igual al stock restante")
        void verificarStock_cantidadExacta_true() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            boolean resultado = stockService.verificarStock(100L, 10);

            assertThat(resultado).isTrue();
        }
    }

    // ---------- descontarStock ----------

    @Nested
    @DisplayName("descontarStock")
    class DescontarStock {

        @Test
        @DisplayName("Descuenta correctamente y mantiene disponibleParaVenta en true si queda stock")
        void descontarStock_conStockRestante_descuentaYGuarda() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            stockService.descontarStock(100L, 4);

            ArgumentCaptor<StockPlato> captor = ArgumentCaptor.forClass(StockPlato.class);
            verify(stockRepo).save(captor.capture());

            StockPlato actualizado = captor.getValue();
            assertThat(actualizado.getCantidadRestante()).isEqualTo(6); // 10 - 4
            assertThat(actualizado.getDisponibleParaVenta()).isTrue();
        }

        @Test
        @DisplayName("Si el descuento deja el stock en cero, marca disponibleParaVenta en false")
        void descontarStock_llegaACero_marcaNoDisponible() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            stockService.descontarStock(100L, 10); // 10 - 10 = 0

            ArgumentCaptor<StockPlato> captor = ArgumentCaptor.forClass(StockPlato.class);
            verify(stockRepo).save(captor.capture());

            StockPlato actualizado = captor.getValue();
            assertThat(actualizado.getCantidadRestante()).isEqualTo(0);
            assertThat(actualizado.getDisponibleParaVenta()).isFalse();
        }

        @Test
        @DisplayName("Lanza RuntimeException si el plato no tiene stock registrado")
        void descontarStock_platoNoExiste_lanzaExcepcion() {
            when(stockRepo.findByIdPlato(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> stockService.descontarStock(999L, 1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Stock no encontrado");

            verify(stockRepo, never()).save(any());
        }

        @Test
        @DisplayName("Lanza RuntimeException si la cantidad a descontar supera el stock restante")
        void descontarStock_stockInsuficiente_lanzaExcepcion() {
            when(stockRepo.findByIdPlato(100L)).thenReturn(Optional.of(stockExistente));

            assertThatThrownBy(() -> stockService.descontarStock(100L, 999))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Stock insuficiente");

            // Nunca debe guardar si falla la validación de negocio
            verify(stockRepo, never()).save(any());
        }
    }
}