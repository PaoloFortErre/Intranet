package com.erretechnology.intranet.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryComunicazioniHR;
@Service
public class ServiceComunicazioniHRImpl implements ServiceComunicazioniHR{
	@Autowired
	RepositoryComunicazioniHR repositoryComunicazioniHR;

	@Override
	public void save(ComunicazioneHR com) {
		repositoryComunicazioniHR.save(com);
	}

	@Override
	public ComunicazioneHR findById(int id) {
		// TODO Auto-generated method stub
		return repositoryComunicazioniHR.findById(id).get();
	}

	@Override
	public List<ComunicazioneHR> findByAutore(UtenteDatiPersonali autore) {
		// TODO Auto-generated method stub
		return repositoryComunicazioniHR.findAll().stream().filter(x -> x.getUtente().equals(autore)).collect(Collectors.toList());	
		}

	@Override
	public List<ComunicazioneHR> getAll() {
		// TODO Auto-generated method stub
		return repositoryComunicazioniHR.findAll();
	}
	
	
	
	
}
