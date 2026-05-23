package com.garzonpro.Payment.client;

import com.garzonpro.Payment.client.dto.PedidoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback de OrderClient.
 * Si Order-Service no responde, Payment lanza advertencia
 * y maneja el error correctamente para no afectar al usuario final.
 */
@Slf4j
@Component
public class OrderClientFallback implements OrderClient {

    @Override
    public ResponseEntity<PedidoResponseDTO> obtenerPedido(Long id) {
        log.error("FALLBACK ACTIVADO: No se pudo conectar con Order-Service para obtener el pedido {}", id);
        throw new RuntimeException("Servicio de pedidos no disponible. No se puede validar el monto a pagar.");
    }

    @Override
    public ResponseEntity<Void> actualizarEstadoPedido(Long id, String nuevoEstado) {
        log.error("FALLBACK ACTIVADO: No se pudo conectar con Order-Service para actualizar el pedido {} al estado {}", id, nuevoEstado);
        throw new RuntimeException("Servicio de pedidos no disponible momentáneamente. Intente nuevamente.");
    }
}