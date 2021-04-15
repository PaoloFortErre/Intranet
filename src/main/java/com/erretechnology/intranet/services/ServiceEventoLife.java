package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.EventoLife;

public interface ServiceEventoLife {
	
	public void save(EventoLife evento);
	
	public void deleteById(int id);
	
	public EventoLife findById(int id);
	
	public List<Evento> getAllNotVisible();
}
