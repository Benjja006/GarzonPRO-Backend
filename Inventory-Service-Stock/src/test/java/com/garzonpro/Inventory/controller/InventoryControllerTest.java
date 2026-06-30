package com.garzonpro.Inventory.controller;

import com.garzonpro.Inventory.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    public void testVerificarStockExitoso() throws Exception {
        // Mockito intercepta la llamada del controlador al servicio
        Mockito.when(stockService.verificarStock(10L, 2)).thenReturn(true);

        mockMvc.perform(get("/inventory/stock/verificar/10/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}