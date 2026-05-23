package com.garzonpro.Payment.controller;

import com.garzonpro.Payment.dto.PagoRequestDTO;
import com.garzonpro.Payment.dto.PagoResponseDTO;
import com.garzonpro.Payment.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PagoService pagoService;

    /**
     * POST /payments/procesar
     * Registra un nuevo pago para un pedido.
     */
    @PostMapping("/procesar")
    public ResponseEntity<PagoResponseDTO> procesarPago(@Valid @RequestBody PagoRequestDTO dto) {
        log.info("Solicitud recibida para procesar pago del pedido ID: {}", dto.getIdPedido());
        PagoResponseDTO response = pagoService.procesarPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /payments/historial
     * Retorna el historial completo de pagos.
     */
    @GetMapping("/historial")
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        log.info("Solicitud de historial de pagos recibida");
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    /**
     * GET /payments/{id}
     * Retorna un pago específico por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPago(@PathVariable Long id) {
        log.info("Solicitud de pago con ID: {}", id);
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    /**
     * GET /payments/pedido/{idPedido}
     * Retorna todos los pagos asociados a un pedido.
     */
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorPedido(@PathVariable Long idPedido) {
        log.info("Solicitud de pagos del pedido ID: {}", idPedido);
        return ResponseEntity.ok(pagoService.obtenerPorPedido(idPedido));
    }
}