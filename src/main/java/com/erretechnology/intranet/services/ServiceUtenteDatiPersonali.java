package com.erretechnology.intranet.services;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceUtenteDatiPersonali {

	public void save(UtenteDatiPersonali utente);
	public UtenteDatiPersonali findById(int id);

	public UtenteDatiPersonali findByAutore(Utente autore);
	public void insert(String psw, String email, UtenteDatiPersonali udp);
}
