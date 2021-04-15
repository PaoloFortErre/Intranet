package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Cliente;
import com.erretechnology.intranet.repositories.RepositoryCliente;

@Service
public class ServiceClienteImpl implements ServiceCliente {

	@Autowired
	RepositoryCliente repositoryCliente;
	
	@Override
	public void save(Cliente cliente) {
		repositoryCliente.save(cliente);
	}
	@Override
	public Cliente findById(int id) {
		return repositoryCliente.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryCliente.deleteById(id);
	}
	@Override
	public List<Cliente> getAllNotVisible() {
		return repositoryCliente.getAllNotVisible();
	}

}
