package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.EventoWork;

public interface RepositoryEventoWork extends CrudRepository<EventoWork, Integer>{
	@Query("SELECT e FROM EventoWork e WHERE e.data >= ?1 AND e.visibile = true ORDER BY e.data DESC")
	List<EventoWork> findNextEvents(long date);
	
	@Query("SELECT e FROM EventoWork e WHERE e.data < ?1 AND e.visibile = true ORDER BY e.data DESC")
	List<EventoWork> findRecentEvents(long date);
	
	@Query("SELECT e FROM EventoWork e WHERE e.visibile = true ORDER BY e.id DESC")
	List<EventoWork> findOrderByIdDesc();
	
	@Query("SELECT e FROM EventoWork e WHERE e.visibile = false")
	List<EventoWork> getAllNotVisible();
}
