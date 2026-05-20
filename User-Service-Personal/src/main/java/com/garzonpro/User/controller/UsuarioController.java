package com.garzonpro.User.controller;

import com.garzonpro.User.dto.UsuarioRequestDTO;
import com.garzonpro.User.dto.UsuarioUpdateDTO; // Importamos el nuevo DTO
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    // 🌟 NUEVO ENDPOINT PARA ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        log.info("Recibida petición para actualizar usuario ID: {}", id);
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }
}