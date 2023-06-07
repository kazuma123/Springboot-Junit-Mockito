package org.app.springboot.test.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.springboot.test.models.Cuenta;
import org.app.springboot.test.models.TransaccionDto;
import org.app.springboot.test.services.ICuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

	@Autowired
	private ICuentaService service;

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<Cuenta> listar(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Cuenta detalle(@PathVariable("id") Long id) {
		return service.findById(id);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cuenta guardar(@RequestBody Cuenta cuenta) {
		return service.save(cuenta);
	}
	
	@PostMapping("/transferir")
	public ResponseEntity<?> tranferir(@RequestBody TransaccionDto dto) {
		service.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "OK");
		response.put("message", "Transferencia realizada con exito");
		response.put("transaccion", dto);
		
		return ResponseEntity.ok(response);
		
	}

	
	
	
	
	
	
	
	
	
	
	
}
