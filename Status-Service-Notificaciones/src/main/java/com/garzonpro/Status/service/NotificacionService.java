package com.garzonpro.Status.service;

import com.garzonpro.Status.dto.NotificacionRequestDTO;
import com.garzonpro.Status.dto.NotificacionResponseDTO;
import com.garzonpro.Status.exception.NotificacionNotFoundException;
import com.garzonpro.Status.model.Notificacion;
import com.garzonpro.Status.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    /**
     * Crea y persiste una nueva notificación para un garzón.
     */
    public NotificacionResponseDTO enviarNotificacion(NotificacionRequestDTO dto) {
        log.info("Enviando notificación al garzón ID: {} — '{}'", dto.getIdGarzonDestino(), dto.getMensaje());

        Notificacion notificacion = new Notificacion();
        notificacion.setIdGarzonDestino(dto.getIdGarzonDestino());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setLeido(false);

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificación creada con ID: {}", guardada.getIdNotificacion());

        return toResponseDTO(guardada);
    }

    /**
     * Retorna todas las notificaciones pendientes (no leídas) de un garzón.
     */
    public List<NotificacionResponseDTO> obtenerPendientesPorGarzon(Long idGarzon) {
        log.info("Consultando notificaciones pendientes del garzón ID: {}", idGarzon);
        return notificacionRepository.findByIdGarzonDestinoAndLeidoFalse(idGarzon)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las notificaciones de un garzón (leídas y pendientes).
     */
    public List<NotificacionResponseDTO> obtenerTodasPorGarzon(Long idGarzon) {
        log.info("Consultando todas las notificaciones del garzón ID: {}", idGarzon);
        return notificacionRepository.findByIdGarzonDestino(idGarzon)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marca una notificación como leída.
     */
    public NotificacionResponseDTO marcarComoLeida(Long id) {
        log.info("Marcando notificación ID: {} como leida", id);

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación con ID {} no encontrada", id);
                    // CORRECCIÓN: Concatenación de strings correcta en Java
                    return new NotificacionNotFoundException("Notificación con ID " + id + " no encontrada");
                });

        notificacion.setLeido(true);
        Notificacion actualizada = notificacionRepository.save(notificacion);
        log.info("Notificación ID: {} marcada como leída", id);

        return toResponseDTO(actualizada);
    }

    // --- Mapper interno ---
    private NotificacionResponseDTO toResponseDTO(Notificacion n) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setIdNotificacion(n.getIdNotificacion());
        dto.setIdGarzonDestino(n.getIdGarzonDestino());
        dto.setMensaje(n.getMensaje());
        dto.setLeido(n.isLeido());
        return dto;
    }
}