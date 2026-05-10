package com.garzonpro.user.service;
import com.garzonpro.user.model.Usuario;
import com.garzonpro.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepo.findByRol(rol);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepo.findAll();
    }
}
