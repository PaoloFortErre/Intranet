package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Libro;
import com.erretechnology.intranet.repositories.RepositoryLibro;

@Service
public class ServiceLibroImpl implements ServiceLibro {

	@Autowired
	RepositoryLibro repositoryLibro;
	
	@Override
	public void save(Libro libro) {
		repositoryLibro.save(libro);
	}
	@Override
	public Libro findById(int id) {
		return repositoryLibro.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryLibro.deleteById(id);
	}
	@Override
	public List<Libro> getAllNotVisible() {
		return repositoryLibro.getAllNotVisible();
	}

}
