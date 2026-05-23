package com.garzonpro.Order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente Feign para comunicarse con Table-Service-Mesas.
 * Al crear un pedido, Order notifica a Table para cambiar
 * el estado de la mesa a OCUPADA.
 */
@FeignClient(name = "table-service", url = "http://localhost:8084",
        fallback = TableClientFallback.class)
public interface TableClient {

    /**
     * Actualiza el estado de una mesa (LIBRE, OCUPADA, RESERVADA).
     */
    @PutMapping("/mesas/{idMesa}/estado")
    ResponseEntity<Void> actualizarEstadoMesa(
            @PathVariable Long idMesa,
            @RequestParam String estado);
}