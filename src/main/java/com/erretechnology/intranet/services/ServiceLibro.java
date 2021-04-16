package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Libro;

public interface ServiceLibro {
	
	public void save(Libro libro);
	
	public void deleteById(int id);
	
	public Libro findById(int id);
	
	public CompletableFuture<List<Libro>> getAllNotVisible();
}
