package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Sondaggio;

public interface ServiceSondaggio {
	public void save(Sondaggio sondaggio);
	public Sondaggio findById(int id);
	public List<Sondaggio> findAll();
}
