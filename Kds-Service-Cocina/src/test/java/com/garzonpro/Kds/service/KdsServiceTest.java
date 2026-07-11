package com.garzonpro.Kds.service;

import com.garzonpro.Kds.client.StatusClient;
import com.garzonpro.Kds.client.dto.NotificacionKdsDTO;
import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de KdsService.
 * StatusClient (Feign) se mockea igual que cualquier otra dependencia:
 * a Mockito no le importa que sea un cliente HTTP, solo que es una interfaz inyectada.
 */
@ExtendWith(MockitoExtension.class)
class KdsServiceTest {

    @Mock
    private KdsRepository kdsRepo;

    @Mock
    private StatusClient statusClient;

    @InjectMocks
    private KdsService kdsService;

    private TicketCocina ticketPendiente;

    @BeforeEach
    void setUp() {
        ticketPendiente = new TicketCocina();
        ticketPendiente.setIdTicket(1L);
        ticketPendiente.setEstadoGeneral("En Preparación");
    }

    @Nested
    @DisplayName("marcarTicketListo")
    class MarcarTicketListo {

        @Test
        @DisplayName("Con ticket existente, cambia el estado a Listo, guarda y notifica a Status-Service")
        void ticketExistente_marcaListoYNotifica() {
            when(kdsRepo.findById(1L)).thenReturn(Optional.of(ticketPendiente));
            when(kdsRepo.save(any(TicketCocina.class))).thenAnswer(inv -> inv.getArgument(0));
            when(statusClient.notificarGarzon(any(NotificacionKdsDTO.class)))
                    .thenReturn(ResponseEntity.ok().build());

            TicketCocina resultado = kdsService.marcarTicketListo(1L);

            assertThat(resultado.getEstadoGeneral()).isEqualTo("Listo");

            ArgumentCaptor<TicketCocina> captor = ArgumentCaptor.forClass(TicketCocina.class);
            verify(kdsRepo).save(captor.capture());
            assertThat(captor.getValue().getEstadoGeneral()).isEqualTo("Listo");

            ArgumentCaptor<NotificacionKdsDTO> notifCaptor = ArgumentCaptor.forClass(NotificacionKdsDTO.class);
            verify(statusClient).notificarGarzon(notifCaptor.capture());
            assertThat(notifCaptor.getValue().getMensaje()).contains("1");
        }

        @Test
        @DisplayName("Con ticket inexistente, lanza RuntimeException y no llama al repositorio ni al cliente")
        void ticketInexistente_lanzaExcepcion() {
            when(kdsRepo.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> kdsService.marcarTicketListo(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Ticket no encontrado");

            verify(kdsRepo, never()).save(any());
            verifyNoInteractions(statusClient);
        }

        @Test
        @DisplayName("Si Status-Service falla al notificar, el ticket igual queda guardado como Listo")
        void statusServiceFalla_noRompeElFlujo() {
            when(kdsRepo.findById(1L)).thenReturn(Optional.of(ticketPendiente));
            when(kdsRepo.save(any(TicketCocina.class))).thenAnswer(inv -> inv.getArgument(0));
            when(statusClient.notificarGarzon(any(NotificacionKdsDTO.class)))
                    .thenThrow(new RuntimeException("Status-Service caído"));

            TicketCocina resultado = kdsService.marcarTicketListo(1L);

            // El try/catch del service debe absorber el error del cliente Feign
            assertThat(resultado.getEstadoGeneral()).isEqualTo("Listo");
            verify(kdsRepo).save(any(TicketCocina.class));
        }
    }
}