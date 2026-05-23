package com.garzonpro.Auth.service;

import com.garzonpro.Auth.dto.LoginRequestDTO;
import com.garzonpro.Auth.dto.RegisterRequestDTO;
import com.garzonpro.Auth.model.Credencial;
import com.garzonpro.Auth.model.Sesion;
import com.garzonpro.Auth.repository.CredencialRepository;
import com.garzonpro.Auth.repository.SesionRepository;
import com.garzonpro.Auth.client.UserClient;
import com.garzonpro.Auth.exception.AuthException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final CredencialRepository credencialRepo;
    private final SesionRepository sesionRepo;
    private final UserClient userClient;

    public AuthService(CredencialRepository credencialRepo, SesionRepository sesionRepo, UserClient userClient) {
        this.credencialRepo = credencialRepo;
        this.sesionRepo = sesionRepo;
        this.userClient = userClient;
    }

    @Transactional
    public String registrarUsuario(RegisterRequestDTO dto) {
        log.info("Iniciando solicitud de registro para el username: {}", dto.getUsername());

        if (credencialRepo.existsById(dto.getUsername())) {
            log.warn("Fallo de registro: El username {} ya está ocupado", dto.getUsername());
            throw new AuthException("El nombre de usuario ya se encuentra en uso", HttpStatus.BAD_REQUEST);
        }

        Long nuevoIdUsuario = System.currentTimeMillis();
        dto.setIdUsuario(nuevoIdUsuario);

        try {
            log.info("Enviando comando de creación de perfil a User-Service para el ID: {}", nuevoIdUsuario);
            userClient.crearPerfilUsuario(dto);
        } catch (Exception e) {
            log.error("Fallo crítico en comunicación distribuida con User-Service: {}", e.getMessage());
            throw new AuthException("El registro no se pudo procesar: Servicio de perfiles no disponible", HttpStatus.SERVICE_UNAVAILABLE);
        }

        Credencial c = new Credencial();
        c.setUsername(dto.getUsername());
        c.setPinUsuario(dto.getPin());
        c.setIdUsuario(nuevoIdUsuario);
        credencialRepo.save(c);

        log.info("Usuario {} registrado con éxito bajo el ID {}", dto.getUsername(), nuevoIdUsuario);
        return "Usuario registrado de manera exitosa";
    }

    @Transactional
    public String login(LoginRequestDTO dto) {
        log.info("Procesando credenciales de acceso para el username: {}", dto.getUsername());

        Credencial c = credencialRepo.findById(dto.getUsername())
                .orElseThrow(() -> new AuthException("Credenciales de acceso inválidas", HttpStatus.UNAUTHORIZED));

        if (!c.getPinUsuario().equals(dto.getPin())) {
            log.warn("Fallo de autenticación: PIN erróneo para el username {}", dto.getUsername());
            throw new AuthException("Credenciales de acceso inválidas", HttpStatus.UNAUTHORIZED);
        }

        sesionRepo.findFirstByIdUsuarioAndFechaFinAfter(c.getIdUsuario(), LocalDateTime.now())
                .ifPresent(sesionActiva -> {
                    sesionActiva.setFechaFin(LocalDateTime.now());
                    sesionRepo.save(sesionActiva);
                    log.info("Sesión activa anterior forzada a expirar para el usuario ID: {}", c.getIdUsuario());
                });

        String token = UUID.randomUUID().toString();
        c.setTokenSesion(token);
        credencialRepo.save(c);

        Sesion s = new Sesion();
        s.setIdUsuario(c.getIdUsuario());
        s.setFechaInicio(LocalDateTime.now());
        s.setFechaFin(LocalDateTime.now().plusMinutes(30));
        sesionRepo.save(s);

        log.info("Autenticación exitosa. Token emitido para el usuario {}", dto.getUsername());
        return token;
    }
}