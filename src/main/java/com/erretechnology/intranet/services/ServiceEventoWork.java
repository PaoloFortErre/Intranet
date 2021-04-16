package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoWork;

public interface ServiceEventoWork {
	
	public void save(EventoWork evento);
	
	public void deleteById(int id);
	
	public EventoWork findById(int id);
	
	public CompletableFuture<List<Evento>> getAllNotVisible();
}
