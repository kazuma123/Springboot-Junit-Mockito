package org.app.springboot.test.repositories;

import java.util.List;

import org.app.springboot.test.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long>{
	//List<Banco> findAll();
	//Banco findById(Long id);
	//void update(Banco cuenta);
}
