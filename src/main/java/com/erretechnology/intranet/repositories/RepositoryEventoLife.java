package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoLife;

public interface RepositoryEventoLife extends CrudRepository<EventoLife, Integer>{
	@Query("SELECT e FROM EventoLife e WHERE e.data >= ?1 AND e.visibile = true ORDER BY e.data DESC")
	List<EventoLife> findNextEvents(long date);
	
	@Query("SELECT e FROM EventoLife e WHERE e.data < ?1 AND e.visibile = true ORDER BY e.data DESC")
	List<EventoLife> findRecentEvents(long date);
	
	@Query("SELECT e FROM EventoLife e WHERE e.visibile = false")
	List<Evento> getAllNotVisible();
}
