package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoWork;

public interface RepositoryEventoWork extends CrudRepository<EventoWork, Integer>{
	
	@Query("SELECT e FROM EventoWork e WHERE e.visibile = false ORDER BY timestampEliminazione DESC")
	List<Evento> getAllNotVisible();
}
