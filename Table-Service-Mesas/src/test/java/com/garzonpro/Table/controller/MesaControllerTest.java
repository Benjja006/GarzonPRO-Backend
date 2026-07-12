package com.garzonpro.Table.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Table.dto.MesaDTO;
import com.garzonpro.Table.model.Mesa;
import com.garzonpro.Table.model.EnumTableStatus;
import com.garzonpro.Table.service.MesaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MesaController.class)
public class MesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MesaService mesaService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- GET /table/listar ----------

    @Test
    public void testListarMesasConMockito() throws Exception {
        Mesa mesaMock = new Mesa();
        mesaMock.setIdMesa(1L);
        mesaMock.setNombreMesa("Mesa 4");
        mesaMock.setEstado(EnumTableStatus.LIBRE);

        Mockito.when(mesaService.listarTodas()).thenReturn(Arrays.asList(mesaMock));

        mockMvc.perform(get("/table/listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMesa").value(1))
                .andExpect(jsonPath("$[0].nombreMesa").value("Mesa 4"))
                .andExpect(jsonPath("$[0].estado").value("LIBRE"));
    }

    @Test
    public void testListarMesas_SinMesas_DeberiaRetornarListaVacia() throws Exception {
        Mockito.when(mesaService.listarTodas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/table/listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- POST /table ----------

    @Test
    public void testCrearMesa_DeberiaRetornarStatusCreated() throws Exception {
        MesaDTO dto = new MesaDTO();
        dto.setNombreMesa("Mesa 7");

        Mesa mesaCreada = new Mesa();
        mesaCreada.setIdMesa(7L);
        mesaCreada.setNombreMesa("Mesa 7");
        mesaCreada.setEstado(EnumTableStatus.LIBRE);

        Mockito.when(mesaService.crearMesa(any(MesaDTO.class))).thenReturn(mesaCreada);

        mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMesa").value(7L))
                .andExpect(jsonPath("$.nombreMesa").value("Mesa 7"))
                .andExpect(jsonPath("$.estado").value("LIBRE"));
    }

    @Test
    public void testCrearMesa_SinNombre_DeberiaRetornarBadRequest() throws Exception {
        // El DTO tiene @NotBlank en nombreMesa, así que un nombre vacío debe fallar la validación
        MesaDTO dto = new MesaDTO();
        dto.setNombreMesa("");

        mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ---------- PATCH /table/{id}/estado ----------

    @Test
    public void testActualizarEstado_DeberiaCambiarEstadoYRetornarOk() throws Exception {
        Mesa mesaActualizada = new Mesa();
        mesaActualizada.setIdMesa(1L);
        mesaActualizada.setNombreMesa("Mesa 4");
        mesaActualizada.setEstado(EnumTableStatus.OCUPADA);

        Mockito.when(mesaService.cambiarEstado(eq(1L), eq(EnumTableStatus.OCUPADA)))
                .thenReturn(mesaActualizada);

        mockMvc.perform(patch("/table/{id}/estado", 1L)
                        .param("estado", "OCUPADA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMesa").value(1L))
                .andExpect(jsonPath("$.estado").value("OCUPADA"));
    }

    @Test
    public void testActualizarEstado_DeberiaInvocarAlServicioConParametrosCorrectos() throws Exception {
        Mesa mesaActualizada = new Mesa();
        mesaActualizada.setIdMesa(2L);
        mesaActualizada.setNombreMesa("Mesa 5");
        mesaActualizada.setEstado(EnumTableStatus.RESERVADA);

        Mockito.when(mesaService.cambiarEstado(eq(2L), eq(EnumTableStatus.RESERVADA)))
                .thenReturn(mesaActualizada);

        mockMvc.perform(patch("/table/{id}/estado", 2L)
                        .param("estado", "RESERVADA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verifica que el controller pasó exactamente el id y el estado correctos al service
        Mockito.verify(mesaService, Mockito.times(1)).cambiarEstado(2L, EnumTableStatus.RESERVADA);
    }
}