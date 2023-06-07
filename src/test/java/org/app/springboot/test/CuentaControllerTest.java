package org.app.springboot.test;

import static org.mockito.Mockito.*;

import org.app.springboot.test.controllers.CuentaController;
import org.app.springboot.test.models.Cuenta;
import org.app.springboot.test.models.TransaccionDto;
import org.app.springboot.test.services.ICuentaService;
import org.assertj.core.util.Arrays;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ICuentaService service;

	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void detalle() throws Exception {
		// Given
		when(service.findById(1L)).thenReturn(Datos.crearCuenta001().get());

		// when
		mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))

				// then
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nombre").value("Eddu")).andExpect(jsonPath("$.saldo").value("1000"));

		verify(service).findById(1L);

	}

	@Test
	void transferir() throws JsonProcessingException, Exception {

		TransaccionDto dto = new TransaccionDto();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setMonto(new BigDecimal("100"));
		dto.setBancoId(1L);

		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "OK");
		response.put("message", "Transferencia realizada con exito");
		response.put("transaccion", dto);

		System.out.println(objectMapper.writeValueAsString(response));

		mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))

				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
				.andExpect(jsonPath("$.message").value("Transferencia realizada con exito"))
				.andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
				.andExpect(content().json(objectMapper.writeValueAsString(response)));

	}

	@Test
	void testFindAll() throws JsonProcessingException, Exception {
		List<Cuenta> cuentas = new ArrayList<>();
		cuentas.add(Datos.crearCuenta001().orElseThrow());
		cuentas.add(Datos.crearCuenta002().orElseThrow());
		when(service.findAll()).thenReturn(cuentas);

		mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].nombre").value("Eddu")).andExpect(jsonPath("$[1].nombre").value("Kakaroto"))
				.andExpect(jsonPath("$[0].saldo").value("1000")).andExpect(jsonPath("$[1].saldo").value("2000"))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
		
		verify(service).findAll();
		
	}

	@Test
	void testCrearCuenta() throws JsonProcessingException, Exception {
		Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
		when(service.save(any())).then(invocarion -> {
			Cuenta c = invocarion.getArgument(0);
			c.setId(3L);
			return c;
		});

		mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cuenta)))

				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.nombre", is("Pepe")))
				.andExpect(jsonPath("$.saldo", is(3000)));
		
		verify(service).save(any());
	}

	

	
	
	
	
	
	
	
	
	
}
