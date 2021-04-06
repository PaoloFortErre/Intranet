package com.erretechnology.intranet.repositories;


import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Utente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository(value = "RepositoryUtente")
public interface RepositoryUtente extends JpaRepository<Utente, Integer>{
	
	public Utente findByEmail(String email);
	public Utente findByTokenResetPassword(String tokenResetPassword);
}
