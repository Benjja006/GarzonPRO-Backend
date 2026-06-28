package com.garzonpro.Catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Catalog.controller.CatalogController;
import com.garzonpro.Catalog.dto.CategoriaDTO;
import com.garzonpro.Catalog.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.model.Categoria;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import com.garzonpro.Catalog.exception.CatalogException;
import org.springframework.http.HttpStatus;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CatalogService catalogService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void crearPlato_OK() throws Exception {
        PlatoDTO dto = new PlatoDTO();

        dto.setNombrePlato("Pizza");
        dto.setPrecio(12000.0);
        dto.setIdCategoria(1L);

        Categoria categoria = new Categoria();

        categoria.setIdCategoria(1L);
        categoria.setNombreCategoria("Platos");

        Plato plato = new Plato();

        plato.setIdPlato(1L);
        plato.setNombrePlato("Pizza");
        plato.setPrecio(12000.0);
        plato.setCategoria(categoria);

        when(catalogService.crearPlato(any(PlatoDTO.class)))
                .thenReturn(plato);

        mockMvc.perform(
                        post("/catalog/platos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombrePlato", is("Pizza")));
    }

    @Test
    void crearPlato_ERROR() throws Exception {
        PlatoDTO dto = new PlatoDTO();
        dto.setNombrePlato("Pizza");
        dto.setPrecio(12000.0);
        dto.setIdCategoria(1L);

        when(catalogService.crearPlato(any(PlatoDTO.class)))
                .thenThrow(new CatalogException(
                        "El plato ya existe",
                        HttpStatus.BAD_REQUEST));

        mockMvc.perform(
                        post("/catalog/platos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void modificarPlato_OK() throws Exception {

        // DTO que enviaremos
        PlatoDTO dto = new PlatoDTO();
        dto.setNombrePlato("Pizza Italiana");
        dto.setPrecio(15000.0);
        dto.setIdCategoria(1L);

        // Categoría
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombreCategoria("Platos");

        // Plato que devolverá el servicio
        Plato plato = new Plato();
        plato.setIdPlato(1L);
        plato.setNombrePlato("Pizza Italiana");
        plato.setPrecio(15000.0);
        plato.setCategoria(categoria);

        // Mockito
        when(catalogService.modificarPlato(org.mockito.ArgumentMatchers.eq(1L), any(PlatoDTO.class)))
                .thenReturn(plato);

        // Ejecutamos el PUT
        mockMvc.perform(
                        put("/catalog/platos/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePlato", is("Pizza Italiana")));

    }

    @Test
    void modificarPlato_ERROR() throws Exception {

        PlatoDTO dto = new PlatoDTO();
        dto.setNombrePlato("Pizza Italiana");
        dto.setPrecio(15000.0);
        dto.setIdCategoria(1L);

        // Mockito lanza excepción
        when(catalogService.modificarPlato(org.mockito.ArgumentMatchers.eq(1L), any(PlatoDTO.class)))
                .thenThrow(new CatalogException(
                        "El plato a modificar no existe",
                        HttpStatus.NOT_FOUND));

        mockMvc.perform(
                        put("/catalog/platos/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("El plato a modificar no existe")));

    }

    @Test
    void eliminarPlato_OK() throws Exception {

        // Mockito no hace nada (simula eliminación exitosa)
        doNothing().when(catalogService).eliminarPlato(1L);

        // Ejecutamos el DELETE
        mockMvc.perform(delete("/catalog/platos/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    void eliminarPlato_ERROR() throws Exception {

        // Mockito lanza excepción
        doThrow(new CatalogException(
                "El plato especificado no existe",
                HttpStatus.NOT_FOUND))
                .when(catalogService)
                .eliminarPlato(1L);

        // Ejecutamos el DELETE
        mockMvc.perform(delete("/catalog/platos/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error",
                        is("El plato especificado no existe")));

    }

    @Test
    void obtenerTodosLosPlatos_OK() throws Exception {

        // Creamos una categoría
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombreCategoria("Platos");

        // Primer plato
        Plato plato1 = new Plato();
        plato1.setIdPlato(1L);
        plato1.setNombrePlato("Pizza");
        plato1.setPrecio(12000.0);
        plato1.setCategoria(categoria);

        // Segundo plato
        Plato plato2 = new Plato();
        plato2.setIdPlato(2L);
        plato2.setNombrePlato("Hamburguesa");
        plato2.setPrecio(9000.0);
        plato2.setCategoria(categoria);

        // Lista simulada
        List<Plato> lista = Arrays.asList(plato1, plato2);
        // Mockito
        when(catalogService.obtenerTodosLosPlatos())
                .thenReturn(lista);
        // Ejecutamos el GET
        mockMvc.perform(get("/catalog/platos"))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombrePlato", is("Pizza")))
                .andExpect(jsonPath("$[1].nombrePlato", is("Hamburguesa")));

    }

    @Test
    void obtenerTodosLosPlatos_ERROR() throws Exception {

        // Mockito lanza una excepción inesperada
        when(catalogService.obtenerTodosLosPlatos())
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/catalog/platos"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error",
                        is("Error interno en Catalog-Service: Error inesperado")));

    }

}
