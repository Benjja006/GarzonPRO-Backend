package com.garzonpro.Order.client;

import com.garzonpro.Order.client.dto.TicketCocinaRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicarse con KDS-Service-Cocina.
 * url apunta al host:puerto donde corre kds-service (localhost:8086 en desarrollo).
 * En produccion con Eureka se reemplaza url por name = "kds-service".
 */
@FeignClient(name = "kds-service", url = "http://localhost:8086",
        fallback = KdsClientFallback.class)
public interface KdsClient {

    /**
     * Notifica a cocina que hay un nuevo pedido listo para preparar.
     */
    @PostMapping("/kds/tickets")
    ResponseEntity<Void> notificarNuevoPedido(@RequestBody TicketCocinaRequestDTO dto);
}