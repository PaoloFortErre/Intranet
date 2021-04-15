package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Cliente;

public interface ServiceCliente {
	
	public void save(Cliente cliente);
	
	public void deleteById(int id);
	
	public Cliente findById(int id);
	
	public CompletableFuture<List<Cliente>> getAllNotVisible();
}
