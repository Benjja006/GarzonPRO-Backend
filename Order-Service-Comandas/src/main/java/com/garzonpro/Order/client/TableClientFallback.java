package com.garzonpro.Order.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback de TableClient.
 * Si Table-Service no responde, Order igual registra el pedido
 * y loguea la advertencia para revisión manual.
 */
@Slf4j
@Component
public class TableClientFallback implements TableClient {

    @Override
    public ResponseEntity<Void> actualizarEstadoMesa(Long idMesa, String estado) {
        log.warn("[FALLBACK] Table-Service no disponible. " +
                "No se pudo actualizar estado de mesa ID: {} a {}", idMesa, estado);
        return ResponseEntity.ok().build();
    }
}