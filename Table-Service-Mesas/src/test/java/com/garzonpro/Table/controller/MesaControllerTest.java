package com.garzonpro.Table.controller;

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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MesaController.class)
public class MesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MesaService mesaService;

    @Test
    public void testListarMesasConMockito() throws Exception {
        // Usamos los setters que Lombok genera automáticamente basados en tus variables reales
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
}