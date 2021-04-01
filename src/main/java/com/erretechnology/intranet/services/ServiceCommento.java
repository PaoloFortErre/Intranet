package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Commento;

public interface ServiceCommento {
	public List<Commento> getLastMessage();
	
	public List<Commento> getAll();

	public void save(Commento commento);
	
	public void delete(Commento commento);
	
	public Commento findById(int id);

	public List<Commento> getAllNotVisible();
}
