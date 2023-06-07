package org.app.springboot.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.app.springboot.test.models.Cuenta;
import org.app.springboot.test.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class IntegracionJpaTest {

	@Autowired
	CuentaRepository cuentaRepository;
	
	@Test
	void testFindById() {
		Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
		assertTrue(cuenta.isPresent());
		assertEquals("Eddu", cuenta.orElseThrow().getNombre());
	}
	
	@Test
	void testFindByPersona() { //Nombre
		Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Eddu");
		assertTrue(cuenta.isPresent());
		assertEquals("Eddu", cuenta.orElseThrow().getNombre());
		assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
	}
	@Test
	void testFindByPersonaThrowException() { //Nombre
		Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Goku");
		assertThrows(NoSuchElementException.class, () ->{
			cuenta.orElseThrow();
		});
		//assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
		assertFalse(cuenta.isPresent());
		
	}
	
	@Test
	void testFindAll() {
		List<Cuenta> cuentas = cuentaRepository.findAll();
		
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
	}
	@Test
	void testSave() {
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("2600.00"));
		Cuenta cuenta = cuentaRepository.save(cuentaPepe);
		
		//Cuenta cuenta = cuentaRepository.findByNombre("Pepe").orElseThrow();
		//Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();
		
		assertEquals("Pepe", cuenta.getNombre());
		assertEquals("2600.00", cuenta.getSaldo().toPlainString());
		assertEquals(3, cuenta.getId());
	}
	
	@Test
	void testUpdate() {
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("2600.00"));
		Cuenta cuenta = cuentaRepository.save(cuentaPepe);
		
		//Cuenta cuenta = cuentaRepository.findByNombre("Pepe").orElseThrow();
		//Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();
		
		assertEquals("Pepe", cuenta.getNombre());
		assertEquals("2600.00", cuenta.getSaldo().toPlainString());
		//assertEquals(3, cuenta.getId());
		
		cuenta.setSaldo(new BigDecimal("3800"));
		Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
		
		assertEquals("Pepe", cuentaActualizada.getNombre());
		assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
		
	}
	
	@Test
	void testDelete() {
		Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
		
		assertEquals("Kakaroto", cuenta.getNombre());
		
		cuentaRepository.deleteById(cuenta.getId());
		
		assertThrows(NoSuchElementException.class, ()->{
			//cuentaRepository.findByNombre("Kakaroto").orElseThrow();
			cuentaRepository.findById(2L).orElseThrow();
		});
		assertEquals(1, cuentaRepository.findAll().size());
	}
	
}
