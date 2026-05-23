package com.garzonpro.Kds.client;

import com.garzonpro.Kds.client.dto.NotificacionKdsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback de StatusClient.
 * Si Status-Service no responde, el ticket igual se marca como listo
 * y se loguea la advertencia.
 */
@Slf4j
@Component
public class StatusClientFallback implements StatusClient {

    @Override
    public ResponseEntity<Void> notificarGarzon(NotificacionKdsDTO dto) {
        log.warn("[FALLBACK] Status-Service no disponible. " +
                "No se pudo notificar al garzon ID: {}", dto.getIdGarzonDestino());
        return ResponseEntity.ok().build();
    }
}