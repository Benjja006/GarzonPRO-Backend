package com.garzonpro.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.User.controller.UsuarioController;
import com.garzonpro.User.dto.UsuarioRequestDTO;
import com.garzonpro.User.exception.UserException;
import com.garzonpro.User.model.Usuario;
import com.garzonpro.User.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequestDTO dtoBase;
    private Usuario usuarioBase;

    // Se ejecuta antes de cada test para tener datos listos
    @BeforeEach
    void setUp() {
        dtoBase = new UsuarioRequestDTO();
        dtoBase.setNombre("Carlos");
        dtoBase.setApellido("Pérez");
        dtoBase.setCorreo("admin@garzonpro.com");
        dtoBase.setIdUsuario(1L);
        dtoBase.setRol("ADMINISTRADOR");

        usuarioBase = new Usuario();
        usuarioBase.setIdUsuario(1L);
        usuarioBase.setNombre("Carlos");
        usuarioBase.setApellido("Pérez");
        usuarioBase.setCorreo("admin@garzonpro.com");
        usuarioBase.setRol("ADMINISTRADOR");
    }

    // 1. TEST OK - Creación Exitosa
    @Test
    void crearUsuario_DeberiaRetornarCreated() throws Exception {
        Mockito.when(usuarioService.crearUsuario(any(UsuarioRequestDTO.class))).thenReturn(usuarioBase);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoBase)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correo").value("admin@garzonpro.com"));
    }

    // 2. TEST ERROR - Correo ya registrado
    @Test
    void crearUsuario_DeberiaRetornarBadRequest_CuandoCorreoExiste() throws Exception {
        Mockito.when(usuarioService.crearUsuario(any(UsuarioRequestDTO.class)))
                .thenThrow(new UserException("El correo electrónico ya se encuentra registrado en el sistema", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoBase)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El correo electrónico ya se encuentra registrado en el sistema"));
    }

    // 3. TEST OK - Obtener lista de usuarios
    @Test
    void obtenerTodos_DeberiaRetornarListaDeUsuarios() throws Exception {
        List<Usuario> listaMock = Arrays.asList(usuarioBase);
        Mockito.when(usuarioService.obtenerTodos()).thenReturn(listaMock);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }

    // 4. TEST ERROR - Fallo interno del servidor
// 4. TEST ERROR - Fallo interno del servidor
    @Test
    void obtenerTodos_DeberiaRetornarInternalServerError_CuandoFallaBaseDeDatos() throws Exception {
        Mockito.when(usuarioService.obtenerTodos())
                .thenThrow(new RuntimeException("Error de conexión"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isInternalServerError())
                // Le agregamos el guion a "User-Service" justo aquí abajo:
                .andExpect(jsonPath("$.error").value("Error interno en User-Service: Error de conexión"));
    }

    // 5. TEST OK - Obtener usuario específico
    @Test
    void obtenerPorId_DeberiaRetornarUsuario() throws Exception {
        Long id = 1L;
        Mockito.when(usuarioService.obtenerPorId(id)).thenReturn(usuarioBase);

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(id));
    }

    // 6. TEST ERROR - Usuario no encontrado
    @Test
    void obtenerPorId_DeberiaRetornarNotFound_CuandoNoExiste() throws Exception {
        Long id = 99L;
        Mockito.when(usuarioService.obtenerPorId(id))
                .thenThrow(new UserException("No se encontró ningún usuario con el ID especificado", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se encontró ningún usuario con el ID especificado"));
    }

    // 7. TEST OK - Actualización exitosa
    @Test
    void actualizarUsuario_DeberiaRetornarUsuarioActualizado() throws Exception {
        Long id = 1L;
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setIdUsuario(id);
        usuarioActualizado.setNombre("Carlos Editado");
        usuarioActualizado.setCorreo("admin@garzonpro.com");

        Mockito.when(usuarioService.actualizarUsuario(eq(id), any(UsuarioRequestDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoBase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos Editado"));
    }

    // 8. TEST ERROR - Actualizar usuario que no existe
    @Test
    void actualizarUsuario_DeberiaRetornarNotFound_CuandoUsuarioNoExiste() throws Exception {
        Long id = 99L;
        Mockito.when(usuarioService.actualizarUsuario(eq(id), any(UsuarioRequestDTO.class)))
                .thenThrow(new UserException("No se encontró ningún usuario con el ID especificado", HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoBase)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se encontró ningún usuario con el ID especificado"));
    }

    // 9. TEST OK - Eliminación exitosa
    @Test
    void eliminarUsuario_DeberiaRetornarMensajeOk() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(usuarioService).eliminarUsuario(id);

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Perfil de usuario eliminado correctamente"));
    }

    // 10. TEST ERROR - Eliminar usuario inexistente
    @Test
    void eliminarUsuario_DeberiaRetornarNotFound_CuandoNoExiste() throws Exception {
        Long id = 99L;
        Mockito.doThrow(new UserException("No se encontró ningún usuario con el ID especificado para eliminar", HttpStatus.NOT_FOUND))
                .when(usuarioService).eliminarUsuario(id);

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se encontró ningún usuario con el ID especificado para eliminar"));
    }
}