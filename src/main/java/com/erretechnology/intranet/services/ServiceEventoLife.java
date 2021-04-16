package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoLife;

public interface ServiceEventoLife {
	
	public void save(EventoLife evento);
	
	public void deleteById(int id);
	
	public EventoLife findById(int id);
	
	public CompletableFuture<List<Evento>> getAllNotVisible();
}
