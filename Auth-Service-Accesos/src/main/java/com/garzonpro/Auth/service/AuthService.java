package com.garzonpro.Auth.service;

import com.garzonpro.Auth.client.UserFeignClient;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final CredencialRepository credencialRepo;
    @Autowired
    private UserFeignClient userFeignClient;
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

        try {
            log.info("Estableciendo conexión por OpenFeign con User-Service para consultar el ID: {}", c.getIdUsuario());

            // Llama por red a user-service pasando el id del usuario
            Object datosDelUsuario = userFeignClient.obtenerUsuarioPorId(c.getIdUsuario());

            // Mensaje que verás en tu consola para comprobar que funcionó
            System.out.println("-> ¡CONEXIÓN EXITOSA CON USER-SERVICE!");
            System.out.println("-> Datos del empleado recibidos: " + datosDelUsuario);

        } catch (Exception e) {
            log.error("Error al intentar comunicarse con User-Service mediante OpenFeign: {}", e.getMessage());
            // Si user-service está apagado, avisará y detendrá el proceso de login
            throw new AuthException("No se pudo iniciar sesión: Servicio de usuarios no responde", HttpStatus.SERVICE_UNAVAILABLE);
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

    @Transactional
    public void actualizarUsuario(Long idUsuario, RegisterRequestDTO dto) {
        log.info("Iniciando actualización orquestada para el usuario ID: {}", idUsuario);

        // 1. Buscar las credenciales actuales en Auth-Service
        Credencial credencialAntigua = credencialRepo.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new AuthException("No existen credenciales para el ID: " + idUsuario, HttpStatus.NOT_FOUND));

        // Comprobar si el usuario envió un username nuevo y diferente al que ya tiene
        boolean cambiarUsername = dto.getUsername() != null
                && !dto.getUsername().trim().isEmpty()
                && !dto.getUsername().equals(credencialAntigua.getUsername());

        if (cambiarUsername) {
            // Validar que el nuevo username no esté ocupado por otra persona
            if (credencialRepo.existsById(dto.getUsername())) {
                throw new AuthException("El nuevo nombre de usuario ya está en uso", HttpStatus.BAD_REQUEST);
            }

            // Truco para JPA: Creamos una nueva credencial para poder cambiar el @Id (username)
            Credencial credencialNueva = new Credencial();
            credencialNueva.setUsername(dto.getUsername());
            credencialNueva.setIdUsuario(idUsuario);
            credencialNueva.setTokenSesion(credencialAntigua.getTokenSesion());

            // Verificamos si también quiso cambiar el PIN
            if (dto.getPin() != null && !dto.getPin().trim().isEmpty()) {
                credencialNueva.setPinUsuario(dto.getPin());
            } else {
                credencialNueva.setPinUsuario(credencialAntigua.getPinUsuario());
            }

            // Borramos el registro viejo y guardamos el nuevo
            credencialRepo.delete(credencialAntigua);
            credencialRepo.save(credencialNueva);
            log.info("Username y credenciales actualizados para el ID: {}", idUsuario);

        } else {
            // Si NO quiso cambiar el username, solo actualizamos el PIN (como lo teníamos antes)
            if (dto.getPin() != null && !dto.getPin().trim().isEmpty()) {
                credencialAntigua.setPinUsuario(dto.getPin());
                credencialRepo.save(credencialAntigua);
                log.info("PIN de acceso actualizado para el ID: {}", idUsuario);
            }
        }

        // 2. Enviar los datos personales al User-Service a través del Feign Client
        try {
            userClient.actualizarPerfilUsuario(idUsuario, dto);
            log.info("Datos personales enviados a User-Service para actualización.");
        } catch (Exception e) {
            log.error("Error al comunicarse con User-Service para actualizar perfil", e);
            throw new AuthException("No se pudo actualizar el perfil en el servicio de usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void eliminarUsuario(Long idUsuario) {
        log.info("Iniciando proceso de desvinculación para el empleado con ID: {}", idUsuario);

        // 1. Buscar si existen credenciales
        Credencial credencial = credencialRepo.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new AuthException("No existen credenciales para el ID: " + idUsuario, HttpStatus.NOT_FOUND));

        // 2. Eliminar credenciales (Revoca el acceso al sistema inmediatamente)
        credencialRepo.delete(credencial);
        log.info("Credenciales de acceso eliminadas para el ID: {}", idUsuario);

        // 3. Comunicarse con User-Service para eliminar los datos personales
        try {
            userClient.eliminarPerfilUsuario(idUsuario);
            log.info("Orden enviada a User-Service: Perfil eliminado exitosamente.");
        } catch (Exception e) {
            log.error("Error al comunicarse con User-Service durante la eliminación", e);
            throw new AuthException("Se eliminó el acceso, pero hubo un error al borrar el perfil en User-Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}