package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Libro;

public interface RepositoryLibro extends CrudRepository<Libro, Integer>{
	
	@Query("SELECT l FROM Libro l WHERE l.visibile = false ORDER BY timestampEliminazione DESC")
	List<Libro> getAllNotVisible();

}
