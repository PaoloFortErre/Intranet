package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.ComunicazioneHR;
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
		return repositoryComunicazioniHR.findById(id).get();
	}

	@Override
	public List<ComunicazioneHR> getAll() {
		return repositoryComunicazioniHR.findAll();
	}

	@Override
	public void remove(ComunicazioneHR hr) {
		 repositoryComunicazioniHR.delete(hr);;
		
	}

	@Override
	@Async
	public CompletableFuture<List<ComunicazioneHR>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryComunicazioniHR.findByVisibileFalse(Sort.by("timestampEliminazione").descending()));
	}
	@Override
	public List<ComunicazioneHR> getAllVisible() {
		return repositoryComunicazioniHR.findByVisibileTrue(Sort.by("id").descending());
	}
	@Override
	public List<ComunicazioneHR> findFirst10ByVisibileTrueOrderByIdDesc() {
		return repositoryComunicazioniHR.findFirst10ByVisibileTrueOrderByIdDesc();
	}
	
	
	
}
