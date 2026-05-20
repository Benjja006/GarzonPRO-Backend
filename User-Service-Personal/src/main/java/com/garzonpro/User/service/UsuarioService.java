package com.garzonpro.User.service;

import com.garzonpro.User.dto.UsuarioRequestDTO;
import com.garzonpro.User.dto.UsuarioUpdateDTO; // Importamos el nuevo DTO
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        log.info("Procesando creación de usuario para: {}", dto.getCorreo());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getCorreo());
        usuario.setIdUsuario(dto.getIdUsuario());

        // Rol por defecto para evitar conflictos con la base de datos
        usuario.setRol("GARZON");

        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listarTodos() {
        log.info("Consultando lista completa de usuarios");
        return usuarioRepo.findAll();
    }

    public List<Usuario> listarPorRol(String rol) {
        log.info("Consultando usuarios con rol: {}", rol);
        return usuarioRepo.findByRol(rol);
    }

    // 🌟 NUEVO MÉTODO PARA ACTUALIZAR
    public Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        log.info("Actualizando datos para el usuario con ID: {}", id);

        // Buscamos si el usuario existe en la BD, si no, lanzamos un error para el ExceptionHandler
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + id));

        // Mapeamos los nuevos datos del DTO a la entidad existente
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getCorreo());
        usuario.setRol(dto.getRol()); // Aquí el administrador ya puede cambiar el rol libremente

        return usuarioRepo.save(usuario);
    }
}