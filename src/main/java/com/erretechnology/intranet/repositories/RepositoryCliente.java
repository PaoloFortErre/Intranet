package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Cliente;

@Repository
public interface RepositoryCliente extends CrudRepository<Cliente, Integer>{
	@Query(value = "SELECT * FROM Client WHERE visibile = true ORDER BY id DESC LIMIT ?1", nativeQuery = true)
	List<Cliente> findLimit(int number);

}
