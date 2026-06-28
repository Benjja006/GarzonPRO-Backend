package com.garzonpro.User.service;

import com.garzonpro.User.dto.UsuarioRequestDTO;
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.repository.UsuarioRepository;
import com.garzonpro.User.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        log.info("Iniciando creación de perfil para el correo electrónico: {}", dto.getCorreo());

        // Regla de Negocio: Validar unicidad del correo
        if (repo.findByCorreo(dto.getCorreo()).isPresent()) {
            log.warn("Fallo de negocio: El correo {} ya está registrado", dto.getCorreo());
            throw new UserException("El correo electrónico ya se encuentra registrado en el sistema", HttpStatus.BAD_REQUEST);
        }

        Usuario u = new Usuario();
        u.setIdUsuario(dto.getIdUsuario()); // Mapeo del id de Auth-Service
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setCorreo(dto.getCorreo());

        // Manejo del rol
        u.setRol(dto.getRol() != null ? dto.getRol().toUpperCase() : "USER");

        Usuario guardado = repo.save(u);
        log.info("Perfil de usuario creado exitosamente con ID Sincronizado: {}", guardado.getIdUsuario());
        return guardado;
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.info("Ejecutando lectura completa de usuarios en la base de datos");
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        log.info("Buscando perfil de usuario asociado al ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new UserException("No se encontró ningún usuario con el ID especificado", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando perfil del usuario con ID: {}", id);

        // 1. Buscar al usuario existente
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new UserException("No se encontró ningún usuario con el ID especificado", HttpStatus.NOT_FOUND));

        // 2. Validar que el nuevo correo no le pertenezca a otra persona
        if (!usuario.getCorreo().equals(dto.getCorreo()) && repo.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new UserException("El nuevo correo ya se encuentra registrado en el sistema", HttpStatus.BAD_REQUEST);
        }

        // 3. Actualizar los datos (omitimos el ID)
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());

        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol().toUpperCase());
        }

        // 4. Guardar y retornar
        Usuario guardado = repo.save(usuario);
        log.info("Perfil de usuario actualizado exitosamente: {}", guardado.getIdUsuario());
        return guardado;
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Iniciando eliminación del perfil de usuario con ID: {}", id);

        // Verificar si el usuario existe antes de intentar borrarlo
        if (!repo.existsById(id)) {
            throw new UserException("No se encontró ningún usuario con el ID especificado para eliminar", HttpStatus.NOT_FOUND);
        }

        repo.deleteById(id);
        log.info("Perfil de usuario con ID {} eliminado exitosamente de la base de datos", id);
    }
}