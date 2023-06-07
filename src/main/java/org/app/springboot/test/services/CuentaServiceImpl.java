package org.app.springboot.test.services;

import java.math.BigDecimal;
import java.util.List;

import org.app.springboot.test.models.Banco;
import org.app.springboot.test.models.Cuenta;
import org.app.springboot.test.repositories.BancoRepository;
import org.app.springboot.test.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentaServiceImpl implements ICuentaService{

	private CuentaRepository cuentaRepository;
	private BancoRepository bancoRepository;
	
	public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
		this.cuentaRepository = cuentaRepository;
		this.bancoRepository = bancoRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Cuenta findById(Long id) {
		// TODO Auto-generated method stub
		return cuentaRepository.findById(id).orElseThrow();
	}
	@Override
	@Transactional(readOnly = true)
	public int revisarTotalTransferencia(Long bancoId) {
		// TODO Auto-generated method stub
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		return banco.getTotalTransferencias();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal revisarSaldo(Long cuentaId) {
		// TODO Auto-generated method stub
		Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
		return cuenta.getSaldo();
	}

	@Override
	@Transactional(readOnly = true)
	public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId) {
		// TODO Auto-generated method stub
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		bancoRepository.save(banco);
		
		Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
		cuentaOrigen.debito(monto);
		cuentaRepository.save(cuentaOrigen);
		
		Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow();
		cuentaDestino.credito(monto);
		cuentaRepository.save(cuentaDestino);
	}

	
	@Override
	public List<Cuenta> findAll() {
		// TODO Auto-generated method stub
		return cuentaRepository.findAll();
	}

	@Override
	public Cuenta save(Cuenta cuenta) {
		// TODO Auto-generated method stub
		return cuentaRepository.save(cuenta);
	}

}
