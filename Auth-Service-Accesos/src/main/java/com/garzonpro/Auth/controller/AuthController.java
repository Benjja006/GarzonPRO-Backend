package com.garzonpro.Auth.controller;

import com.garzonpro.Auth.model.Credencial;
import com.garzonpro.Auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class    AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String pin) {
        return authService.login(username, pin);
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String token) {
        return authService.validateToken(token);
    }

    @PostMapping("/register")
    public Credencial register(@RequestBody Credencial credencial) {
        return authService.registrarUsuario(credencial);
    }
}
