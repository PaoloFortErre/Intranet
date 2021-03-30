package com.erretechnology.intranet.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Evento;

public interface RepositoryEvento extends CrudRepository<Evento, Integer>{
	@Query("SELECT e FROM Evento e WHERE e.data >= ?1 AND e.isLife = true AND e.visibile = true ORDER BY e.data DESC")
	Collection<Evento> findNextLifeEvents(long date);
	
	@Query("SELECT e FROM Evento e WHERE e.data < ?1 AND e.isLife = true AND e.visibile = true ORDER BY e.data DESC")
	Collection<Evento> findRecentLifeEvents(long date);
	
	@Query("SELECT e FROM Evento e WHERE e.isLife = true AND e.visibile = true ORDER BY e.id DESC")
	List<Evento> findLifeOrderByIdDesc();
	
	@Query("SELECT e FROM Evento e WHERE e.data >= ?1 AND e.isLife = false AND e.visibile = true ORDER BY e.data DESC")
	Collection<Evento> findNextWorkEvents(long date);
	
	@Query("SELECT e FROM Evento e WHERE e.data < ?1 AND e.isLife = false AND e.visibile = true ORDER BY e.data DESC")
	Collection<Evento> findRecentWorkEvents(long date);
	
	@Query("SELECT e FROM Evento e WHERE e.isLife = false AND e.visibile = true ORDER BY e.id DESC")
	List<Evento> findWorkOrderByIdDesc();
	
	@Query("SELECT e FROM Evento e WHERE e.visibile = false")
	List<Evento> getAllNotVisible();
}
