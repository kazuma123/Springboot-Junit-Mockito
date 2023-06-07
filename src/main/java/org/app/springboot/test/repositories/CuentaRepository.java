package org.app.springboot.test.repositories;

import java.util.Optional;

import org.app.springboot.test.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CuentaRepository extends JpaRepository<Cuenta, Long>{
	@Query("select c from Cuenta c where c.nombre=?1")
	Optional<Cuenta> findByNombre(String persona);
	
	//List<Cuenta> findAll();
	//Cuenta findById(Long id);
	//void update(Cuenta cuenta);
	
}
