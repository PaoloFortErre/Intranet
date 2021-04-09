package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.ComunicazioneHR;

public interface ServiceComunicazioniHR {
	public void save(ComunicazioneHR com);

	public ComunicazioneHR findById(int id);

	public List<ComunicazioneHR> getAll();

	public void remove(ComunicazioneHR hr);
	public List<ComunicazioneHR> getAllVisible();
	public List<ComunicazioneHR> getAllNotVisible();
	
	
}
