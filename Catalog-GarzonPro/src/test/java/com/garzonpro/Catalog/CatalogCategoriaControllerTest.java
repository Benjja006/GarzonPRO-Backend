package com.garzonpro.Catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Catalog.controller.CatalogController;
import com.garzonpro.Catalog.dto.CategoriaDTO;
import com.garzonpro.Catalog.exception.CatalogException;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.service.CatalogService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;


@WebMvcTest(CatalogController.class)
class CatalogControllerCategoriaTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CatalogService catalogService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearCategoria_OK() throws Exception {

        // DTO enviado
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombreCategoria("Bebidas");

        // Categoría que devolverá el servicio
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombreCategoria("Bebidas");

        // Mockito
        when(catalogService.crearCategoria(any(CategoriaDTO.class)))
                .thenReturn(categoria);

        // Ejecutamos el POST
        mockMvc.perform(post("/catalog/categorias")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCategoria", is("Bebidas")));

    }


    @Test
    void crearCategoria_ERROR() throws Exception {

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombreCategoria("Bebidas");

        // Mockito simula un error
        when(catalogService.crearCategoria(any(CategoriaDTO.class)))
                .thenThrow(new CatalogException(
                        "Ya existe una categoría con el nombre especificado",
                        HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/catalog/categorias")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))

                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        is("Ya existe una categoría con el nombre especificado")));

    }


    @Test
    void modificarCategoria_OK() throws Exception {

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombreCategoria("Postres");

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombreCategoria("Postres");

        when(catalogService.modificarCategoria(eq(1L), any(CategoriaDTO.class)))
                .thenReturn(categoria);

        mockMvc.perform(
                        put("/catalog/categorias/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCategoria", is("Postres")));
    }


    @Test
    void modificarCategoria_ERROR() throws Exception {

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombreCategoria("Postres");

        when(catalogService.modificarCategoria(eq(1L), any(CategoriaDTO.class)))
                .thenThrow(new CatalogException(
                        "La categoría a modificar no existe",
                        HttpStatus.NOT_FOUND));

        mockMvc.perform(
                        put("/catalog/categorias/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error",
                        is("La categoría a modificar no existe")));
    }


    @Test
    void eliminarCategoria_OK() throws Exception {

        doNothing().when(catalogService).eliminarCategoria(1L);

        mockMvc.perform(delete("/catalog/categorias/1"))

                .andDo(print())

                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarCategoria_ERROR() throws Exception {

        doThrow(new CatalogException(
                "No se puede eliminar la categoría porque contiene platos asociados",
                HttpStatus.CONFLICT))
                .when(catalogService)
                .eliminarCategoria(1L);

        mockMvc.perform(delete("/catalog/categorias/1"))

                .andDo(print())

                .andExpect(status().isConflict())

                .andExpect(jsonPath("$.error",
                        is("No se puede eliminar la categoría porque contiene platos asociados")));
    }

    @Test
    void obtenerCategorias_OK() throws Exception {

        Categoria cat1 = new Categoria();
        cat1.setIdCategoria(1L);
        cat1.setNombreCategoria("Bebidas");

        Categoria cat2 = new Categoria();
        cat2.setIdCategoria(2L);
        cat2.setNombreCategoria("Postres");

        List<Categoria> lista = Arrays.asList(cat1, cat2);

        when(catalogService.obtenerCategorias())
                .thenReturn(lista);

        mockMvc.perform(get("/catalog/categorias"))

                .andDo(print())

                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].nombreCategoria", is("Bebidas")))

                .andExpect(jsonPath("$[1].nombreCategoria", is("Postres")));
    }

    @Test
    void obtenerCategorias_ERROR() throws Exception {

        when(catalogService.obtenerCategorias())
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/catalog/categorias"))

                .andDo(print())

                .andExpect(status().isInternalServerError())

                .andExpect(jsonPath("$.error",
                        is("Error interno en Catalog-Service: Error inesperado")));
    }


}