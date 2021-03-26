package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<Sondaggio> findAll(){
		return repositorySondaggio.findAll().stream()
				.filter(x->x.isVisibile())
				.sorted(Comparator.comparingInt(Sondaggio::getId).reversed())
				.collect(Collectors.toList());
	}
	
	@Override
	public void save(Sondaggio sondaggio) {
		repositorySondaggio.save(sondaggio);		
	}
	@Override
	public List<Sondaggio> findByAutore(UtenteDatiPersonali autore) {
		return findAll().stream().filter(x-> x.getAutore().equals(autore)).collect(Collectors.toList());
	}
}
