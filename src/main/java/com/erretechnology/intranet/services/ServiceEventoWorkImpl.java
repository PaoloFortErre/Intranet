package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoWork;
import com.erretechnology.intranet.repositories.RepositoryEventoWork;

@Service
public class ServiceEventoWorkImpl implements ServiceEventoWork {

	@Autowired
	RepositoryEventoWork repositoryEventoWork;
	
	@Override
	public void save(EventoWork evento) {
		repositoryEventoWork.save(evento);
	}
	@Override
	public EventoWork findById(int id) {
		return repositoryEventoWork.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryEventoWork.deleteById(id);
	}
	@Override
	@Async
	public CompletableFuture<List<Evento>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryEventoWork.getAllNotVisible());
	}

}
