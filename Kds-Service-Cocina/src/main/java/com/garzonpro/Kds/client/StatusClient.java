package com.garzonpro.Kds.client;

import com.garzonpro.Kds.client.dto.NotificacionKdsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicarse con Status-Service-Notificaciones.
 * Cuando un ticket es marcado como listo en cocina,
 * KDS notifica al garzón correspondiente.
 */
@FeignClient(name = "status-service", url = "http://localhost:8088",
        fallback = StatusClientFallback.class)
public interface StatusClient {

    /**
     * Envía una notificación al garzón indicando que su pedido está listo.
     */
    @PostMapping("/status/notificar")
    ResponseEntity<Void> notificarGarzon(@RequestBody NotificacionKdsDTO dto);
}