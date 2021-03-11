package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Permesso;


public interface ServicePermesso {

	public void savePermesso(Permesso p);
	public List<Permesso> getAll();
	public List<Permesso> getFromUser();
}
