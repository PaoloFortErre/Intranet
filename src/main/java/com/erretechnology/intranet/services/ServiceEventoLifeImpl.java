package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoLife;
import com.erretechnology.intranet.repositories.RepositoryEventoLife;

@Service
public class ServiceEventoLifeImpl implements ServiceEventoLife {

	@Autowired
	RepositoryEventoLife repositoryEventoLife;
	
	@Override
	public void save(EventoLife evento) {
		repositoryEventoLife.save(evento);
	}
	@Override
	public EventoLife findById(int id) {
		return repositoryEventoLife.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryEventoLife.deleteById(id);
	}
	@Override
	@Async
	public CompletableFuture<List<Evento>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryEventoLife.getAllNotVisible());
	}

}
