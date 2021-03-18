package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.repositories.RepositorySondaggio;

@Service("ServiceSondaggio")
public class ServiceSondaggioImpl implements ServiceSondaggio{
	@Autowired
	RepositorySondaggio repositorySondaggio;
	@Override
	public Sondaggio findById(int id) {
		return repositorySondaggio.findById(id).get();
	}
	@Override
	public List<Sondaggio> findAll(){
		return repositorySondaggio.findAll();
	}
	
	@Override
	public void save(Sondaggio sondaggio) {
		repositorySondaggio.save(sondaggio);		
	}
}
