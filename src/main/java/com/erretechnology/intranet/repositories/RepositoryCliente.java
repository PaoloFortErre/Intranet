package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Cliente;


public interface RepositoryCliente extends CrudRepository<Cliente, Integer>{
	List<Cliente> findTop3ByOrderByIdDesc();

}
