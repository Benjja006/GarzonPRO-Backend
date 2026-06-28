package com.garzonpro.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Auth.controller.AuthController;
import com.garzonpro.Auth.dto.LoginRequestDTO;
import com.garzonpro.Auth.dto.RegisterRequestDTO;
import com.garzonpro.Auth.exception.AuthException;
import com.garzonpro.Auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva seguridad para enfocarnos en el endpoint
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones HTTP (GET, POST, etc.)

    @MockBean
    private AuthService authService; // Nuestro "doble" del servicio real

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos Java a JSON

    // 1. TEST OK - Registro Exitoso
    @Test
    void registrar_DeberiaRetornarOk() throws Exception {
        // Preparar datos válidos
        RegisterRequestDTO dto = new RegisterRequestDTO("benja", "1234", "Benjamín", "Sepúlveda", "benja@correo.com", "ADMIN", null);

        // Le decimos al mock qué hacer: "Cuando llamen a registrarUsuario con cualquier objeto, devuelve este texto"
        Mockito.when(authService.registrarUsuario(any(RegisterRequestDTO.class)))
                .thenReturn("Usuario registrado de manera exitosa");

        // Ejecutar la petición simulada y verificar
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()) // Esperamos HTTP 201 [cite: 662]
                .andExpect(content().string("Usuario registrado de manera exitosa"));
    }

    // 2. TEST ERROR - Nombre de usuario ya existe
    @Test
    void registrar_DeberiaRetornarError_CuandoUsuarioYaExiste() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("benja", "1234", "Benjamín", "Sepúlveda", "benja@correo.com", "ADMIN", null);

        // Simulamos que el servicio lanza una excepción porque el usuario ya existe [cite: 595]
        Mockito.when(authService.registrarUsuario(any(RegisterRequestDTO.class)))
                .thenThrow(new AuthException("El nombre de usuario ya se encuentra en uso", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()) // Esperamos HTTP 400
                .andExpect(jsonPath("$.error").value("El nombre de usuario ya se encuentra en uso")); // Verifica el JSON de tu GlobalExceptionHandler [cite: 658]
    }


    // 3. TEST OK - Login Exitoso
    @Test
    void login_DeberiaRetornarToken_CuandoCredencialesSonCorrectas() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("benja", "1234");
        String tokenSimulado = "token-uuid-12345";

        Mockito.when(authService.login(any(LoginRequestDTO.class))).thenReturn(tokenSimulado);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // Esperamos HTTP 200 [cite: 662]
                .andExpect(content().string(tokenSimulado));
    }

    // 4. TEST ERROR - Login Fallido (Credenciales inválidas)
    @Test
    void login_DeberiaRetornarError_CuandoCredencialesSonInvalidas() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("benja", "0000");

        Mockito.when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new AuthException("Credenciales de acceso inválidas", HttpStatus.UNAUTHORIZED)); // [cite: 604]

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized()) // Esperamos HTTP 401
                .andExpect(jsonPath("$.error").value("Credenciales de acceso inválidas"));
    }

    // 5. TEST OK - Actualización Exitosa
    @Test
    void actualizarDatosUsuario_DeberiaRetornarOk() throws Exception {
        Long idUsuario = 1L;
        RegisterRequestDTO dto = new RegisterRequestDTO("nuevoBenja", "5678", "Benjamín", "Sepúlveda", "nuevo@correo.com", "ADMIN", 1L);

        // Para métodos void, usamos doNothing()
        Mockito.doNothing().when(authService).actualizarUsuario(eq(idUsuario), any(RegisterRequestDTO.class));

        mockMvc.perform(put("/auth/usuarios/actualizar/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // [cite: 664]
                .andExpect(content().string("Usuario y credenciales actualizados correctamente"));
    }

    // 6. TEST ERROR - Actualización Fallida (Usuario no encontrado)
    @Test
    void actualizarDatosUsuario_DeberiaRetornarError_CuandoNoExiste() throws Exception {
        Long idUsuario = 99L;
        RegisterRequestDTO dto = new RegisterRequestDTO("nuevoBenja", "5678", "Benjamín", "Sepúlveda", "nuevo@correo.com", "ADMIN", 99L);

        Mockito.doThrow(new AuthException("No existen credenciales para el ID: " + idUsuario, HttpStatus.NOT_FOUND)) // [cite: 618]
                .when(authService).actualizarUsuario(eq(idUsuario), any(RegisterRequestDTO.class));

        mockMvc.perform(put("/auth/usuarios/actualizar/{idUsuario}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound()) // Esperamos HTTP 404
                .andExpect(jsonPath("$.error").value("No existen credenciales para el ID: 99"));
    }

    // 7. TEST OK - Eliminación Exitosa
    @Test
    void eliminarUsuario_DeberiaRetornarOk() throws Exception {
        Long idUsuario = 1L;

        Mockito.doNothing().when(authService).eliminarUsuario(idUsuario);

        mockMvc.perform(delete("/auth/usuarios/eliminar/{idUsuario}", idUsuario))
                .andExpect(status().isOk()) // [cite: 664]
                .andExpect(content().string("Empleado desvinculado: Acceso y perfil eliminados correctamente"));
    }

    // 8. TEST ERROR - Error al comunicarse con User-Service
    @Test
    void eliminarUsuario_DeberiaRetornarError_CuandoFallaComunicacion() throws Exception {
        Long idUsuario = 1L;

        Mockito.doThrow(new AuthException("Se eliminó el acceso, pero hubo un error al borrar el perfil en User-Service", HttpStatus.INTERNAL_SERVER_ERROR)) // [cite: 641]
                .when(authService).eliminarUsuario(idUsuario);

        mockMvc.perform(delete("/auth/usuarios/eliminar/{idUsuario}", idUsuario))
                .andExpect(status().isInternalServerError()) // Esperamos HTTP 500
                .andExpect(jsonPath("$.error").value("Se eliminó el acceso, pero hubo un error al borrar el perfil en User-Service"));
    }
}

