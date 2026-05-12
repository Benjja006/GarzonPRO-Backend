package com.garzonpro.User.service;

import com.garzonpro.User.dto.UsuarioRequestDTO;
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

    // Cambiamos el parámetro de 'Usuario' a 'UsuarioRequestDTO'
    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        log.info("Procesando creación de usuario para: {}", dto.getCorreo());

        // PASO CLAVE: Mapear el DTO a la Entidad
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getCorreo());

        // Si tu entidad Usuario tiene un campo idUsuario, lo seteamos
        // Esto es vital para la relación con Auth-Service
        usuario.setIdUsuario(dto.getIdUsuario());

        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepo.findAll();
    }

    public List<Usuario> listarPorRol(String rol) {
        // Asumiendo que tienes este método en el repository
        return usuarioRepo.findByRol(rol);
    }
}