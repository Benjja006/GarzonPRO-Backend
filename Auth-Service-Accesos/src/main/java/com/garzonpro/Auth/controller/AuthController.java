package com.garzonpro.Auth.controller;

import com.garzonpro.Auth.dto.LoginRequestDTO;
import com.garzonpro.Auth.dto.RegisterRequestDTO;
import com.garzonpro.Auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * POST http://localhost:8081/auth/register
     * POST http://localhost:8080/auth/register (API Gateway)
     * Registra un nuevo usuario y sus credenciales.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@Valid @RequestBody RegisterRequestDTO dto) {
        String respuesta = service.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * POST http://localhost:8081/auth/login
     * POST http://localhost:8080/auth/login (API Gateway)
     * Autentica un usuario y devuelve un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = service.login(dto);
        return ResponseEntity.ok(token);
    }

    /**
     * PUT http://localhost:8081/auth/usuarios/actualizar/{idUsuario}
     * PUT http://localhost:8080/auth/usuarios/actualizar/{idUsuario} (API Gateway)
     * Actualiza la información del usuario y sus credenciales.
     */
    @PutMapping("/usuarios/actualizar/{idUsuario}")
    public ResponseEntity<String> actualizarDatosUsuario(@PathVariable Long idUsuario,
                                                         @RequestBody RegisterRequestDTO dto) {
        service.actualizarUsuario(idUsuario, dto);
        return ResponseEntity.ok("Usuario y credenciales actualizados correctamente");
    }

    /**
     * DELETE http://localhost:8081/auth/usuarios/eliminar/{idUsuario}
     * DELETE http://localhost:8080/auth/usuarios/eliminar/{idUsuario} (API Gateway)
     * Elimina el usuario y sus credenciales del sistema.
     */
    @DeleteMapping("/usuarios/eliminar/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long idUsuario) {
        service.eliminarUsuario(idUsuario);
        return ResponseEntity.ok("Empleado desvinculado: Acceso y perfil eliminados correctamente");
    }
}