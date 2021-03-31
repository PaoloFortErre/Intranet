package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceComunicazioniHR {
	public void save(ComunicazioneHR com);

	public ComunicazioneHR findById(int id);

	public List<ComunicazioneHR> findByAutore(UtenteDatiPersonali autore);

	public List<ComunicazioneHR> getAll();

	public void remove(ComunicazioneHR hr);
	
	
}
