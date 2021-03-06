package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceCommento {
	public List<Commento> getLastMessage();
	
	public List<Commento> getAll();

	public void save(Commento commento);
	
	public void delete(Commento commento);
	
	public Commento findById(int id);

	public CompletableFuture<List<Commento>> getAllNotVisible() throws InterruptedException;

	public List<Commento> getAllByAutore(UtenteDatiPersonali autore);
}
