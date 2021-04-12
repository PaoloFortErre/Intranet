package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface RepositoryComunicazioniHR extends JpaRepository<ComunicazioneHR, Integer>{
	@Query
	List<ComunicazioneHR> findByVisibileTrue(Sort sort);
	@Query
	public List<ComunicazioneHR> findByVisibileFalse(Sort sort);
	@Query
	public List<ComunicazioneHR> findByUtente(UtenteDatiPersonali utente, Sort sort);
	@Query
	public List<ComunicazioneHR> findFirst10ByVisibileTrueOrderByIdDesc();
}
