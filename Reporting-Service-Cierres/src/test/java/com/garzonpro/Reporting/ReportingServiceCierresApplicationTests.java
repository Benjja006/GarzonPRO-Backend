package com.garzonpro.Reporting;

import com.garzonpro.Reporting.controller.ReportingController;
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
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportingController.class)
public class ReportingServiceCierresApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CierreRepository repository;

	@Test
	public void testHistorialCierresConMockito() throws Exception {
		CierreCaja cierreMock = new CierreCaja();
		cierreMock.setIdCierre(1L);
		cierreMock.setFecha(LocalDate.now());
		cierreMock.setTotalVentasDia(150500.0);
		cierreMock.setCantidadPedidos(25);

		Mockito.when(repository.findAll()).thenReturn(Arrays.asList(cierreMock));

		mockMvc.perform(get("/reporting/historial")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].idCierre").value(1))
				.andExpect(jsonPath("$[0].totalVentasDia").value(150500.0))
				.andExpect(jsonPath("$[0].cantidadPedidos").value(25));
	}
}