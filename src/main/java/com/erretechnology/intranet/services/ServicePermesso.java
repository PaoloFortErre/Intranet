package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Permesso;


public interface ServicePermesso {
	public Permesso findById(String id);
	public void savePermesso(Permesso p);
	public List<Permesso> getAll();
	public List<String> getAllName();
	public Permesso findByDescrizione(String descrizione);
}
