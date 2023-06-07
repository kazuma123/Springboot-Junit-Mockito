package org.app.springboot.test;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.app.springboot.test.models.TransaccionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CuentaControllerWebTestClientTest {

	private ObjectMapper mapper;
	
	@Autowired
	private WebTestClient client;
	
	@BeforeEach
	void setUo() {
		mapper = new ObjectMapper();
	}
	
	@Test
	void testTransferir() throws JsonProcessingException {
		TransaccionDto dto = new TransaccionDto();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setBancoId(1L);
		dto.setMonto(new BigDecimal("100"));
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "OK");
		response.put("message", "Transferencia realizada con exito");
		response.put("transaccion", dto);
		
		client.post().uri("http://localhost:8080/api/cuentas/transferir")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(dto)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.message").isNotEmpty()
		.jsonPath("$.message").value(is("Transferencia realizada con exito"))
		.jsonPath("transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
		.jsonPath("$.date").isEqualTo(LocalDate.now().toString())
		.json(mapper.writeValueAsString(response));
		
		
	}
	
}
