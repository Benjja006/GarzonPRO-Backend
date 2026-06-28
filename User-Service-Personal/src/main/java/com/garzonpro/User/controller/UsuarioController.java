package com.garzonpro.User.controller;

import com.garzonpro.User.dto.UsuarioRequestDTO;
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /**
     * POST http://localhost:8082/usuarios
     * POST http://localhost:8080/usuarios (API Gateway)
     * Crea un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario nuevoUsuario = service.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    /**
     * GET http://localhost:8082/usuarios
     * GET http://localhost:8080/usuarios (API Gateway)
     * Obtiene la lista de todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    /**
     * GET http://localhost:8082/usuarios/{id}
     * GET http://localhost:8080/usuarios/{id} (API Gateway)
     * Obtiene un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /**
     * PUT http://localhost:8082/usuarios/{id}
     * PUT http://localhost:8080/usuarios/{id} (API Gateway)
     * Actualiza un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                     @RequestBody UsuarioRequestDTO dto) {
        Usuario usuarioActualizado = service.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * DELETE http://localhost:8082/usuarios/{id}
     * DELETE http://localhost:8080/usuarios/{id} (API Gateway)
     * Elimina un usuario por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return ResponseEntity.ok("Perfil de usuario eliminado correctamente");
    }

}