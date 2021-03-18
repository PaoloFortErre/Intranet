package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceSondaggio {
	public void save(Sondaggio sondaggio);
	public Sondaggio findById(int id);
	public List<Sondaggio> findAll();
	public List<Sondaggio> findByAutore(UtenteDatiPersonali autore);
}
