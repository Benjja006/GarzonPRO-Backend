package com.garzonpro.Reporting.controller;

import com.garzonpro.Reporting.model.CierreCaja;
import com.garzonpro.Reporting.repository.CierreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ReportingController.class)
public class ReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CierreRepository repository;

    // ---------- GET /reporting/historial ----------

    @Test
    public void testObtenerHistorialExitoso() throws Exception {
        CierreCaja cierreMock = new CierreCaja();
        cierreMock.setIdCierre(1L);
        cierreMock.setFecha(LocalDate.of(2026, 7, 12));
        cierreMock.setTotalVentasDia(150000.0);
        cierreMock.setCantidadPedidos(25);

        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(cierreMock));

        mockMvc.perform(get("/reporting/historial")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idCierre").value(1))
                .andExpect(jsonPath("$[0].totalVentasDia").value(150000.0))
                .andExpect(jsonPath("$[0].cantidadPedidos").value(25));
    }

    @Test
    public void testObtenerHistorialVacio() throws Exception {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reporting/historial")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- POST /reporting/guardar-cierre ----------

    @Test
    public void testGuardarCierreExitoso() throws Exception {
        CierreCaja cierreMock = new CierreCaja();
        cierreMock.setIdCierre(1L);
        cierreMock.setTotalVentasDia(150000.0);
        cierreMock.setCantidadPedidos(25);

        Mockito.when(repository.save(any(CierreCaja.class))).thenReturn(cierreMock);

        String jsonCierre = "{\"totalVentasDia\":150000.0,\"cantidadPedidos\":25}";

        mockMvc.perform(post("/reporting/guardar-cierre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCierre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCierre").value(1))
                .andExpect(jsonPath("$.totalVentasDia").value(150000.0))
                .andExpect(jsonPath("$.cantidadPedidos").value(25));
    }

    @Test
    public void testGuardarCierreLlamaAlRepositorioConDatosCorrectos() throws Exception {
        CierreCaja cierreMock = new CierreCaja();
        cierreMock.setIdCierre(2L);
        cierreMock.setTotalVentasDia(250000.0);
        cierreMock.setCantidadPedidos(40);

        Mockito.when(repository.save(any(CierreCaja.class))).thenReturn(cierreMock);

        String jsonCierre = "{\"totalVentasDia\":250000.0,\"cantidadPedidos\":40}";

        mockMvc.perform(post("/reporting/guardar-cierre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCierre))
                .andExpect(status().isOk());

        // Verifica que el controller efectivamente invocó save() exactamente una vez
        Mockito.verify(repository, Mockito.times(1)).save(any(CierreCaja.class));
    }
}