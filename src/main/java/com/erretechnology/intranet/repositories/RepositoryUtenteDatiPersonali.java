package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface RepositoryUtenteDatiPersonali extends JpaRepository<UtenteDatiPersonali, Integer> {
	
	public UtenteDatiPersonali findByUtente(Utente autore);
	
	@Query("SELECT u2 FROM Utente u1, UtenteDatiPersonali u2 WHERE u1.id = u2.id AND u1.attivo = FALSE")
	public List<UtenteDatiPersonali> findByAttivoFalse();

}
