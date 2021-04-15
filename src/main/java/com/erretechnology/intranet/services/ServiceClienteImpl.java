package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
	@Async
	public CompletableFuture<List<Cliente>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryCliente.getAllNotVisible());
	}

}
