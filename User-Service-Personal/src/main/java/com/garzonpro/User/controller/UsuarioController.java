package com.garzonpro.User.controller;

import com.garzonpro.User.dto.UsuarioRequestDTO; // Usa el DTO
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.service.UsuarioService;
import jakarta.validation.Valid; // Para activar la validación
import lombok.extern.slf4j.Slf4j; // Para los logs (IE 2.3.2)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // IE 2.2.2: Usamos @Valid para que el DTO valide los datos antes de entrar
    // IE 2.3.1: Retornamos ResponseEntity con estado 201 (Created)
    @PostMapping("/create")
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        log.info("Recibida petición para crear usuario: {}", dto.getCorreo());
        Usuario nuevoUsuario = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        log.info("Consultando lista completa de usuarios");
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable String rol) {
        log.info("Consultando usuarios con rol: {}", rol);
        List<Usuario> usuarios = usuarioService.listarPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }
}