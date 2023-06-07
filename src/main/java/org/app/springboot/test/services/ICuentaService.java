package org.app.springboot.test.services;

import java.math.BigDecimal;
import java.util.List;

import org.app.springboot.test.models.Cuenta;

public interface ICuentaService {
	
	List<Cuenta> findAll();
	Cuenta findById(Long id);
	Cuenta save (Cuenta cuenta);
	int revisarTotalTransferencia(Long bancoId);
	BigDecimal revisarSaldo(Long cuentaId);
	void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
}
