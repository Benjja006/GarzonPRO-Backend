package com.garzonpro.Reporting.controller;

import com.garzonpro.Reporting.dto.CierreCajaRequestDTO;
import com.garzonpro.Reporting.dto.CierreCajaResponseDTO;
import com.garzonpro.Reporting.service.CierreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController {

    private final CierreService cierreService;

    /**
     * GET /reporting/historial
     * Retorna el historial completo de cierres de caja.
     */
    @GetMapping("/historial")
    public ResponseEntity<List<CierreCajaResponseDTO>> obtenerHistorial() {
        log.info("Solicitud de historial de cierres recibida");
        return ResponseEntity.ok(cierreService.obtenerHistorial());
    }

    /**
     * POST /reporting/guardar-cierre
     * Registra el cierre de caja del día actual.
     */
    @PostMapping("/guardar-cierre")
    public ResponseEntity<CierreCajaResponseDTO> guardarCierre(@Valid @RequestBody CierreCajaRequestDTO dto) {
        log.info("Solicitud de cierre de caja recibida");
        CierreCajaResponseDTO response = cierreService.guardarCierre(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /reporting/fecha?fecha=2025-06-01
     * Retorna el cierre de caja de una fecha específica.
     */
    @GetMapping("/fecha")
    public ResponseEntity<CierreCajaResponseDTO> obtenerPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.info("Solicitud de cierre para la fecha: {}", fecha);
        return ResponseEntity.ok(cierreService.obtenerPorFecha(fecha));
    }
}