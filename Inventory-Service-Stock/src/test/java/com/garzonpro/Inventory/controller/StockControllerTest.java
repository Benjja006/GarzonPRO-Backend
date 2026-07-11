package com.garzonpro.Inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garzonpro.Inventory.dto.DescontarStockDTO;
import com.garzonpro.Inventory.dto.StockDTO;
import com.garzonpro.Inventory.exception.GlobalExceptionHandler;
import com.garzonpro.Inventory.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
// Nota: si tu versión de Spring Boot es >= 3.4, reemplaza @MockBean por
// @org.springframework.test.context.bean.override.mockito.MockitoBean,
// ya que @MockBean está deprecado desde esa versión.
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de la capa web (controller) de StockController.
 * Se usa @WebMvcTest para levantar solo el contexto MVC (sin base de datos)
 * y se mockea StockService con @MockBean. También se importa el
 * GlobalExceptionHandler para verificar el mapeo de errores a HTTP.
 */
@WebMvcTest(StockController.class)
@Import(GlobalExceptionHandler.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StockService stockService;

    // ---------- POST /inventory/stock/inicializar ----------

    @Nested
    @DisplayName("POST /inventory/stock/inicializar")
    class Inicializar {

        @Test
        @DisplayName("Con datos válidos, responde 201 Created")
        void inicializar_datosValidos_created() throws Exception {
            StockDTO dto = new StockDTO();
            dto.setIdPlato(1L);
            dto.setCantidad(15);

            doNothing().when(stockService).inicializarStock(any(StockDTO.class));

            mockMvc.perform(post("/inventory/stock/inicializar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Stock inicializado correctamente"));

            verify(stockService).inicializarStock(any(StockDTO.class));
        }

        @Test
        @DisplayName("Sin idPlato, responde 400 con el error de validación")
        void inicializar_sinIdPlato_badRequest() throws Exception {
            StockDTO dto = new StockDTO();
            dto.setCantidad(10); // falta idPlato

            mockMvc.perform(post("/inventory/stock/inicializar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.idPlato").value("El ID del plato es obligatorio"));

            verify(stockService, never()).inicializarStock(any());
        }

        @Test
        @DisplayName("Con cantidad negativa, responde 400 con el error de validación")
        void inicializar_cantidadNegativa_badRequest() throws Exception {
            StockDTO dto = new StockDTO();
            dto.setIdPlato(1L);
            dto.setCantidad(-5);

            mockMvc.perform(post("/inventory/stock/inicializar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.cantidad").value("La cantidad no puede ser negativa"));

            verify(stockService, never()).inicializarStock(any());
        }
    }

    // ---------- GET /inventory/stock/verificar/{idPlato}/{cantidad} ----------

    @Nested
    @DisplayName("GET /inventory/stock/verificar/{idPlato}/{cantidad}")
    class Verificar {

        @Test
        @DisplayName("Si hay stock suficiente, responde 200 con body true")
        void verificar_hayStock_true() throws Exception {
            when(stockService.verificarStock(1L, 5)).thenReturn(true);

            mockMvc.perform(get("/inventory/stock/verificar/1/5"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        @DisplayName("Si no hay stock suficiente, responde 200 con body false")
        void verificar_noHayStock_false() throws Exception {
            when(stockService.verificarStock(1L, 999)).thenReturn(false);

            mockMvc.perform(get("/inventory/stock/verificar/1/999"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
    }

    // ---------- POST /inventory/stock/descontar ----------

    @Nested
    @DisplayName("POST /inventory/stock/descontar")
    class Descontar {

        @Test
        @DisplayName("Con stock suficiente, descuenta y responde 200")
        void descontar_conStock_ok() throws Exception {
            DescontarStockDTO dto = new DescontarStockDTO();
            dto.setIdPlato(1L);
            dto.setCantidad(3);

            when(stockService.verificarStock(1L, 3)).thenReturn(true);
            doNothing().when(stockService).descontarStock(1L, 3);

            mockMvc.perform(post("/inventory/stock/descontar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                            "Stock actualizado con éxito para el plato ID: 1"));

            verify(stockService).descontarStock(1L, 3);
        }

        @Test
        @DisplayName("Sin stock suficiente, responde 400 sin llamar a descontarStock")
        void descontar_sinStock_badRequest() throws Exception {
            DescontarStockDTO dto = new DescontarStockDTO();
            dto.setIdPlato(1L);
            dto.setCantidad(999);

            when(stockService.verificarStock(1L, 999)).thenReturn(false);

            mockMvc.perform(post("/inventory/stock/descontar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_validacion")
                            .value("No hay stock suficiente para el plato ID: 1"));

            verify(stockService, never()).descontarStock(anyLong(), anyInt());
        }

        @Test
        @DisplayName("Con idPlato inválido (cero o negativo), responde 400 de validación")
        void descontar_idPlatoInvalido_badRequest() throws Exception {
            DescontarStockDTO dto = new DescontarStockDTO();
            dto.setIdPlato(0L); // @Positive falla
            dto.setCantidad(1);

            mockMvc.perform(post("/inventory/stock/descontar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.idPlato").value("El ID del plato debe ser válido"));

            verify(stockService, never()).verificarStock(anyLong(), anyInt());
        }
    }
}