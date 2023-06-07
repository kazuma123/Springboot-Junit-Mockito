package org.app.springboot.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.app.springboot.test.Datos.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.app.springboot.test.exceptions.DineroInsuficienteException;
import org.app.springboot.test.models.Banco;
import org.app.springboot.test.models.Cuenta;
import org.app.springboot.test.repositories.BancoRepository;
import org.app.springboot.test.repositories.CuentaRepository;
import org.app.springboot.test.services.CuentaServiceImpl;
import org.app.springboot.test.services.ICuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootTestApplicationTests {

	@Mock //@MockBean
	CuentaRepository cuentaRepository;

	@Mock //@MockBean
	BancoRepository bancoRepository;

	@InjectMocks //@Autowired colocar un Bean en la clase
	CuentaServiceImpl service; // CuentaService

	@BeforeEach
	void setUp() {
		//cuentaRepository = mock(CuentaRepository.class);
		//bancoRepository = mock(BancoRepository.class);
		//service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
		//Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
		//Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
		//Datos.BANCO.setTotalTransferencias(0);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		System.out.println(saldoOrigen + "  -  " + saldoDestino);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		service.transferir(1L, 2L, new BigDecimal("500"), 1L);
		
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("500", saldoOrigen.toPlainString());
		assertEquals("2500", saldoDestino.toPlainString());

		int total = service.revisarTotalTransferencia(1L);
		assertEquals(1, total);
		
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));;
		
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));
		
		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
		
	}
	@Test
	void contextLoads2() {
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteException.class, ()->{
			service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});
		
		
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		int total = service.revisarTotalTransferencia(1L);
		assertEquals(1, total);
		
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));;
		
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));
		
		verify(bancoRepository, times(2)).findById(anyLong());
		verify(cuentaRepository,never()).findAll();
		
	}
	@Test
	void contextLoads3(){
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		
		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		
		assertSame(cuenta1, cuenta2);
		
		assertEquals("Eddu", cuenta1.getNombre());
		assertEquals("Eddu", cuenta2.getNombre());
		
		verify(cuentaRepository, times(2)).findById(1L);
	}
	
	//############ TEST SERVICIOS ####################
	
	@Test
	void testFindAll() {
		List<Cuenta> datos = new ArrayList<>();
		datos.add(Datos.crearCuenta001().orElseThrow());
		datos.add(Datos.crearCuenta002().orElseThrow());

		when(cuentaRepository.findAll()).thenReturn(datos);
		
		List<Cuenta> cuentas = service.findAll();
		
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(Datos.crearCuenta002().orElseThrow()));
		
		verify(cuentaRepository).findAll();
	}
	
	@Test
	void testSave() {
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

		when(cuentaRepository.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		
		Cuenta cuenta = service.save(cuentaPepe);
		
		assertEquals("Pepe", cuenta.getNombre());
		assertEquals(3, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());
		
		verify(cuentaRepository).save(any());
	}
	
	
	
	
	
	
	
	
	
	
	
}
