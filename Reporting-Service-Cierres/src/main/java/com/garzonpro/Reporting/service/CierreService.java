package com.garzonpro.Reporting.service;

import com.garzonpro.Reporting.dto.CierreCajaRequestDTO;
import com.garzonpro.Reporting.dto.CierreCajaResponseDTO;
import com.garzonpro.Reporting.exception.CierreNotFoundException;
import com.garzonpro.Reporting.exception.CierreDuplicadoException;
import com.garzonpro.Reporting.model.CierreCaja;
import com.garzonpro.Reporting.repository.CierreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CierreService {

    private final CierreRepository cierreRepository;

    public CierreCajaResponseDTO guardarCierre(CierreCajaRequestDTO dto) {
        LocalDate hoy = LocalDate.now();
        log.info("Generando cierre de caja para la fecha: {}", hoy);

        if (cierreRepository.findByFecha(hoy).isPresent()) {
            log.warn("Ya existe un cierre de caja para la fecha: {}", hoy);
            throw new CierreDuplicadoException("Ya existe un cierre de caja registrado para hoy: " + hoy);
        }

        CierreCaja cierre = new CierreCaja();
        cierre.setFecha(hoy);
        cierre.setTotalVentasDia(dto.getTotalVentasDia());
        cierre.setCantidadPedidos(dto.getCantidadPedidos());

        CierreCaja guardado = cierreRepository.save(cierre);
        log.info("Cierre de caja registrado exitosamente con ID: {}", guardado.getIdCierre());
        return toResponseDTO(guardado);
    }

    public List<CierreCajaResponseDTO> obtenerHistorial() {
        log.info("Consultando historial de cierres de caja");
        return cierreRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // CORRECCIÓN CRÍTICA: Se repara la asignación rota del findByFecha
    public CierreCajaResponseDTO obtenerPorFecha(LocalDate fecha) {
        log.info("Buscando cierre de caja para la fecha: {}", fecha);

        CierreCaja cierre = cierreRepository.findByFecha(fecha)
                .orElseThrow(() -> {
                    log.warn("No se encontró cierre de caja para la fecha: {}", fecha);
                    return new CierreNotFoundException("No existe cierre de caja para la fecha: " + fecha);
                });

        return toResponseDTO(cierre);
    }

    private CierreCajaResponseDTO toResponseDTO(CierreCaja cierre) {
        CierreCajaResponseDTO dto = new CierreCajaResponseDTO();
        dto.setIdCierre(cierre.getIdCierre());
        dto.setFecha(cierre.getFecha());
        dto.setTotalVentasDia(cierre.getTotalVentasDia());
        dto.setCantidadPedidos(cierre.getCantidadPedidos());
        return dto;
    }
}