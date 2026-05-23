package com.garzonpro.Payment.client;

import com.garzonpro.Payment.client.dto.PedidoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "http://localhost:8085", fallback = OrderClientFallback.class)
public interface OrderClient {

    @GetMapping("/orders/{id}")
    ResponseEntity<PedidoResponseDTO> obtenerPedido(@PathVariable("id") Long id);

    @PutMapping("/orders/{id}/estado")
    ResponseEntity<Void> actualizarEstadoPedido(@PathVariable("id") Long id, @RequestParam("nuevoEstado") String nuevoEstado);
}