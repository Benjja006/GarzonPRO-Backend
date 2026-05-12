package com.garzonpro.Auth.service;

import com.garzonpro.Auth.client.UserClient;
import com.garzonpro.Auth.dto.RegisterRequestDTO;
import com.garzonpro.Auth.model.Credencial;
import com.garzonpro.Auth.model.Sesion;
import com.garzonpro.Auth.repository.CredencialRepository;
import com.garzonpro.Auth.repository.SesionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j // Agregamos SLF4J para logs
@Service
public class AuthService {
    @Autowired
    private CredencialRepository credencialRepo;

    @Autowired
    private SesionRepository sesionRepo;

    @Autowired
    private UserClient userClient; // <--- NUEVO: Inyectar el "teléfono" para llamar a User-Service

    public Credencial registrarUsuario(RegisterRequestDTO dto) {
        log.info("Iniciando registro para el usuario: {}", dto.getUsername());

        // 1. Guardar en la base de datos de AUTH (garzonpro_auth)
        Credencial nueva = new Credencial();
        nueva.setUsername(dto.getUsername());
        nueva.setPinUsuario(dto.getPinUsuario());
        nueva.setIdUsuario(dto.getIdUsuario());

        Credencial guardada = credencialRepo.save(nueva);
        log.info("Usuario {} registrado en AUTH con éxito", guardada.getUsername());

        // 2. NUEVO: Llamar al microservicio de USER para crear el perfil
        try {
            log.info("Llamando a User-Service para crear el perfil del ID: {}", dto.getIdUsuario());
            userClient.crearPerfilUsuario(dto);
            log.info("Perfil creado exitosamente en el microservicio de Usuarios");
        } catch (Exception e) {
            // Es vital capturar la excepción por si el User-Service está apagado
            log.error("No se pudo crear el perfil en User-Service: {}", e.getMessage());
        }

        return guardada;
    }

    @Transactional
    public String login(String username, String pin) {
        log.info("Intento de login para usuario: {}", username);

        Credencial cred = credencialRepo.findByUsername(username)
                .filter(c -> c.getPinUsuario().equals(pin))
                .orElseThrow(() -> {
                    log.error("Credenciales inválidas para usuario: {}", username);
                    return new RuntimeException("Credenciales inválidas");
                });

        String token = UUID.randomUUID().toString();
        cred.setTokenSesion(token);
        credencialRepo.save(cred);

        Sesion nuevaSesion = new Sesion();
        nuevaSesion.setIdUsuario(cred.getIdUsuario());
        nuevaSesion.setFechaInicio(LocalDateTime.now());
        nuevaSesion.setFechaFin(LocalDateTime.now().plusMinutes(30));
        nuevaSesion.setRolUsuario("USER");
        sesionRepo.save(nuevaSesion);

        log.info("Login exitoso. Sesión creada para usuario: {}", username);
        return token;
    }

    public boolean validateToken(String token) {
        log.info("Validando token de sesión...");
        return credencialRepo.findByTokenSesion(token)
                .map(cred -> sesionRepo.findFirstByIdUsuarioOrderByFechaInicioDesc(cred.getIdUsuario())
                        .map(sesion -> sesion.getFechaFin().isAfter(LocalDateTime.now()))
                        .orElse(false))
                .orElse(false);
    }
}