package com.garzonpro.Payment.service;

import com.garzonpro.Payment.dto.PagoRequestDTO;
import com.garzonpro.Payment.dto.PagoResponseDTO;
import com.garzonpro.Payment.exception.PagoNotFoundException;
import com.garzonpro.Payment.model.Pago;
import com.garzonpro.Payment.repository.PagoRepository;
import com.garzonpro.Payment.client.OrderClient;
import com.garzonpro.Payment.client.dto.PedidoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final OrderClient orderClient; // Inyectamos el cliente Feign

    public PagoResponseDTO procesarPago(PagoRequestDTO dto) {
        log.info("Procesando pago para pedido ID: {}, monto: {}", dto.getIdPedido(), dto.getMontoTotal());

        // 1. VALIDACIÓN INTER-SERVICIO: Consultamos el pedido
        ResponseEntity<PedidoResponseDTO> response = orderClient.obtenerPedido(dto.getIdPedido());

        // Manejo seguro del Fallback en caso de que order-service esté caído y devuelva null
        if (response == null || response.getBody() == null) {
            throw new IllegalStateException("No se pudo verificar el pedido porque el servicio de Órdenes no responde.");
        }

        PedidoResponseDTO pedido = response.getBody();

        // 2. REGLA DE NEGOCIO: Validar que el pedido esté ABIERTO
        if (!"ABIERTO".equalsIgnoreCase(pedido.getEstadoGeneral())) {
            throw new IllegalArgumentException("No se puede procesar el pago. El pedido se encuentra en estado: " + pedido.getEstadoGeneral());
        }

        // 3. REGISTRO DEL PAGO
        Pago pago = new Pago();
        pago.setIdPedido(dto.getIdPedido());
        pago.setMontoTotal(dto.getMontoTotal());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstadoPago("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());

        Pago pagoGuardado = pagoRepository.save(pago);
        log.info("Pago registrado exitosamente con ID: {}", pagoGuardado.getIdPago());

        // 4. INTEGRACIÓN POST-PAGO: Cambiamos el estado del pedido en order-service a PAGADO
        try {
            orderClient.actualizarEstadoPedido(dto.getIdPedido(), "PAGADO");
            log.info("Order-Service notificado: El pedido ID {} ahora está PAGADO", dto.getIdPedido());
        } catch (Exception e) {
            log.error("No se pudo actualizar el estado del pedido en Order-Service, pero el pago quedó registrado: {}", e.getMessage());
        }

        return toResponseDTO(pagoGuardado);
    }

    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public PagoResponseDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException("Pago con ID " + id + " no encontrado"));
        return toResponseDTO(pago);
    }

    public List<PagoResponseDTO> obtenerPorPedido(Long idPedido) {
        return pagoRepository.findByIdPedido(idPedido).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private PagoResponseDTO toResponseDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdPedido(pago.getIdPedido());
        dto.setMontoTotal(pago.getMontoTotal());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstadoPago(pago.getEstadoPago());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}