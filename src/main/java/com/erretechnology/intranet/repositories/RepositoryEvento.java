package com.erretechnology.intranet.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.News;

public interface RepositoryEvento extends CrudRepository<Evento, Integer>{
	@Query("SELECT e FROM Evento e WHERE e.data >= ?1 ORDER BY e.data")
	Collection<Evento> findNextEvents(long date);
	
	@Query("SELECT e FROM Evento e WHERE e.data < ?1 ORDER BY e.data DESC")
	Collection<Evento> findRecentEvents(long date);
	
	List<Evento> findByOrderByIdDesc();
}
