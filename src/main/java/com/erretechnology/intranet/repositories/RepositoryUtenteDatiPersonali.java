package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface RepositoryUtenteDatiPersonali extends JpaRepository<UtenteDatiPersonali, Integer> {

}
