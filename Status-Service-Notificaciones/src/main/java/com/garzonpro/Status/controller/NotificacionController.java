package com.garzonpro.Status.controller;

import com.garzonpro.Status.dto.NotificacionRequestDTO;
import com.garzonpro.Status.dto.NotificacionResponseDTO;
import com.garzonpro.Status.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * POST /status/notificar
     * Crea una nueva notificación para un garzón.
     * Este endpoint será llamado por KDS-Service cuando un plato esté listo.
     */
    @PostMapping("/notificar")
    public ResponseEntity<NotificacionResponseDTO> enviarNotificacion(
            @Valid @RequestBody NotificacionRequestDTO dto) {
        log.info("Solicitud de nueva notificación para garzón ID: {}", dto.getIdGarzonDestino());
        NotificacionResponseDTO response = notificacionService.enviarNotificacion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /status/pendientes/{idGarzon}
     * Retorna notificaciones no leídas de un garzón específico.
     */
    @GetMapping("/pendientes/{idGarzon}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerPendientes(
            @PathVariable Long idGarzon) {
        log.info("Consultando notificaciones pendientes del garzón ID: {}", idGarzon);
        return ResponseEntity.ok(notificacionService.obtenerPendientesPorGarzon(idGarzon));
    }

    /**
     * GET /status/garzon/{idGarzon}
     * Retorna todas las notificaciones de un garzón (historial completo).
     */
    @GetMapping("/garzon/{idGarzon}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerTodas(
            @PathVariable Long idGarzon) {
        log.info("Consultando historial de notificaciones del garzón ID: {}", idGarzon);
        return ResponseEntity.ok(notificacionService.obtenerTodasPorGarzon(idGarzon));
    }

    /**
     * PUT /status/leer/{id}
     * Marca una notificación como leída.
     */
    @PutMapping("/leer/{id}")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Long id) {
        log.info("Solicitud para marcar notificación ID: {} como leída", id);
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }
}