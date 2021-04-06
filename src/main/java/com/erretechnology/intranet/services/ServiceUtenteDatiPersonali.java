package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceUtenteDatiPersonali {

	public void save(UtenteDatiPersonali utente);
	public UtenteDatiPersonali findById(int id);
	public List<UtenteDatiPersonali> getAll();
	public UtenteDatiPersonali findByAutore(Utente autore);
	public void insert(String psw, String email, String settore,UtenteDatiPersonali udp);
	public List<UtenteDatiPersonali> getInattivi();
	public List<UtenteDatiPersonali> getAttivi();
}
