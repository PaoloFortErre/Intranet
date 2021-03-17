package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Cliente;

@Repository
public interface RepositoryCliente extends CrudRepository<Cliente, Integer>{
	List<Cliente> findTop3ByOrderByIdDesc();

}
