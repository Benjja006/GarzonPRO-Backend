package com.garzonpro.Auth.service;

import com.garzonpro.Auth.model.Credencial;
import com.garzonpro.Auth.model.Sesion;
import com.garzonpro.Auth.repository.CredencialRepository;
import com.garzonpro.Auth.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private CredencialRepository credencialRepo;

    @Autowired
    private SesionRepository sesionRepo;

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public Credencial registrarUsuario(Credencial nueva) {
        return credencialRepo.save(nueva);
    }

    /**
     * Valida credenciales, genera un token y registra la sesión.
     * El token tendrá una validez de 30 minutos.
     */
    @Transactional
    public String login(String username, String pin) {
        // 1. Validar que el usuario existe y el PIN coincide
        Credencial cred = credencialRepo.findByUsername(username)
                .filter(c -> c.getPinUsuario().equals(pin))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // 2. Generar nuevo token único
        String token = UUID.randomUUID().toString();

        // 3. Actualizar el token en la tabla 'credencial'
        cred.setTokenSesion(token);
        credencialRepo.save(cred);

        // 4. Crear registro histórico en la tabla 'sesion' con expiración
        Sesion nuevaSesion = new Sesion();
        nuevaSesion.setIdUsuario(cred.getIdUsuario());
        nuevaSesion.setFechaInicio(LocalDateTime.now());
        nuevaSesion.setFechaFin(LocalDateTime.now().plusMinutes(30)); // Expira en 30 min
        nuevaSesion.setRolUsuario("USER");

        sesionRepo.save(nuevaSesion);

        return token;
    }

    /**
     * Valida si un token existe y si aún no ha expirado según la tabla de sesiones.
     */
    public boolean validateToken(String token) {
        return credencialRepo.findByTokenSesion(token)
                .map(cred -> {
                    // Buscamos la sesión más reciente de este usuario
                    return sesionRepo.findFirstByIdUsuarioOrderByFechaInicioDesc(cred.getIdUsuario())
                            .map(sesion -> {
                                // Verificamos si la fecha actual es antes de la fecha de fin
                                return sesion.getFechaFin().isAfter(LocalDateTime.now());
                            })
                            .orElse(false);
                })
                .orElse(false);
    }
}