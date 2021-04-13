package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
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
	public List<Sondaggio> findAllVisible(){
		return repositorySondaggio.findByVisibileTrue(Sort.by("id").descending());
	}
	
	@Override
	public void save(Sondaggio sondaggio) {
		repositorySondaggio.save(sondaggio);		
	}
	@Override
	public List<Sondaggio> findByAutore(UtenteDatiPersonali autore) {
		return repositorySondaggio.findByAutore(autore);
	}
	@Override
	public List<Sondaggio> getAllNotVisible() {
		return repositorySondaggio.findByVisibileFalse(Sort.by("timestampEliminazione").descending());
	}
	@Override
	public void delete(Sondaggio s) {
		repositorySondaggio.delete(s);
	}
	@Override
	public List<Sondaggio> findAll() {
		return repositorySondaggio.findAll();
	}
}
