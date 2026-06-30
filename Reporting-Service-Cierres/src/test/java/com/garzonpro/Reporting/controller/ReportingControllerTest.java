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
    private CierreRepository repository; // Mockeamos directamente el repositorio aquí

    @Test
    public void testObtenerHistorialExitoso() throws Exception {
        CierreCaja cierreMock = new CierreCaja();
        // Si tu modelo CierreCaja tiene un ID u otra propiedad, puedes setearlo aquí si gustas:
        // cierreMock.setId(1L);

        // Simulamos que el repositorio devuelve una lista con nuestro cierre ficticio
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(cierreMock));

        mockMvc.perform(get("/reporting/historial")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Verifica un 200 OK
    }

    @Test
    public void testGuardarCierreExitoso() throws Exception {
        CierreCaja cierreMock = new CierreCaja();

        Mockito.when(repository.save(any(CierreCaja.class))).thenReturn(cierreMock);

        // JSON de prueba simulando el RequestBody del CierreCaja
        String jsonCierre = "{\"totalVentas\":150000.0,\"comentarios\":\"Cierre de caja exitoso\"}";

        mockMvc.perform(post("/reporting/guardar-cierre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCierre))
                .andExpect(status().isOk()); // Tu controlador devuelve un 200 OK por defecto
    }
}