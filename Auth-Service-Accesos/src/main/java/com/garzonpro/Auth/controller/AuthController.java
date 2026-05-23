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

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@Valid @RequestBody RegisterRequestDTO dto) {
        String respuesta = service.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = service.login(dto);
        return ResponseEntity.ok(token);
    }
}