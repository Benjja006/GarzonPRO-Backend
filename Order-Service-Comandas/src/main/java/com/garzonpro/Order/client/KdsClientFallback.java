package com.garzonpro.Order.client;

import com.garzonpro.Order.client.dto.TicketCocinaRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback de KdsClient.
 * Se ejecuta cuando KDS-Service no responde o lanza error.
 * Permite que Order-Service continúe funcionando sin bloquear la operación.
 */
@Slf4j
@Component
public class KdsClientFallback implements KdsClient {

    @Override
    public ResponseEntity<Void> notificarNuevoPedido(TicketCocinaRequestDTO dto) {
        log.warn("[FALLBACK] KDS-Service no disponible. " +
                "No se pudo notificar a cocina el pedido ID: {}", dto.getIdPedido());
        // Retorna 200 para no romper el flujo de Order
        return ResponseEntity.ok().build();
    }
}