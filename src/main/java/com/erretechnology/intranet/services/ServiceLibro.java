package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Libro;

public interface ServiceLibro {
	
	public void save(Libro libro);
	
	public void deleteById(int id);
	
	public Libro findById(int id);
	
	public List<Libro> getAllNotVisible();
}
