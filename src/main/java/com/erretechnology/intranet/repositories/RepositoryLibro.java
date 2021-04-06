package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Libro;

public interface RepositoryLibro extends CrudRepository<Libro, Integer>{
	
	@Query("SELECT l FROM Libro l WHERE l.visibile = false")
	List<Libro> getAllNotVisible();
	
	@Query(value = "DELETE FROM book WHERE id = ?1" , nativeQuery = true)
	void remove(int libro);
}
