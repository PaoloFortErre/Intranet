package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.ComunicazioneHR;

public interface ServiceComunicazioniHR {
	public void save(ComunicazioneHR com);

	public ComunicazioneHR findById(int id);

	public List<ComunicazioneHR> getAll();

	public void remove(ComunicazioneHR hr);
	public List<ComunicazioneHR> getAllVisible();
	public CompletableFuture<List<ComunicazioneHR>> getAllNotVisible();
	public List<ComunicazioneHR> findFirst10ByVisibileTrueOrderByIdDesc();
	
	
}
