package com.garzonpro.Kds.service;

import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import com.garzonpro.Kds.client.StatusClient; // Importar el cliente
import com.garzonpro.Kds.client.dto.NotificacionKdsDTO; // Importar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KdsService {

    @Autowired
    private KdsRepository kdsRepo;

    @Autowired
    private StatusClient statusClient; // CORRECCIÓN: Inyectamos el cliente de notificaciones

    public TicketCocina marcarTicketListo(Long idTicket) {
        TicketCocina ticket = kdsRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        ticket.setEstadoGeneral("Listo");
        TicketCocina ticketGuardado = kdsRepo.save(ticket);

        // INTEGRACIÓN REQUISITO PAUTA: KDS notifica a Status-Service
        try {
            NotificacionKdsDTO notificacion = new NotificacionKdsDTO();
            // Asumimos que el ticket tiene el ID del garzón. Si no, puedes poner un ID de prueba o sacarlo de la mesa.
            notificacion.setIdGarzonDestino(1L);
            notificacion.setMensaje("El ticket de cocina #" + idTicket + " está LISTO para ser servido.");

            statusClient.notificarGarzon(notificacion);
        } catch (Exception e) {
            // Si el servicio de notificaciones está caído, el fallback lo atajará, pero no bloqueamos la cocina.
        }

        return ticketGuardado;
    }
}