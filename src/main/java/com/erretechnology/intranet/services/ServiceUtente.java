package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Utente;


public interface ServiceUtente {

	public void saveUtente(Utente u);
	public List<Utente> getAll();
	public Utente findByEmail(String email);
	public Utente findById(int id);
}