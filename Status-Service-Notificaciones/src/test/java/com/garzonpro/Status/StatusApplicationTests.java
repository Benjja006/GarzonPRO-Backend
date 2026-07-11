package com.garzonpro.Status;

import com.garzonpro.Status.controller.NotificacionController;
import com.garzonpro.Status.dto.NotificacionResponseDTO;
import com.garzonpro.Status.service.NotificacionService;
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

@WebMvcTest(NotificacionController.class)
public class StatusApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificacionService notificacionService;

	@Test
	public void testObtenerPendientesConMockito() throws Exception {
		// Inicializamos el DTO
		NotificacionResponseDTO responseMock = new NotificacionResponseDTO();
		responseMock.setMensaje("¡El plato para la mesa 3 está listo!");
		responseMock.setLeido(false);
		// Omitimos el setId para que no falle por nombres de variables internas del DTO

		Long idGarzonPrueba = 5L;

		Mockito.when(notificacionService.obtenerPendientesPorGarzon(idGarzonPrueba))
				.thenReturn(Arrays.asList(responseMock));

		mockMvc.perform(get("/status/pendientes/" + idGarzonPrueba)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].mensaje").value("¡El plato para la mesa 3 está listo!"))
				.andExpect(jsonPath("$[0].leida").value(false));
	}
}