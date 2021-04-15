package com.erretechnology.intranet.repositories;


import org.springframework.stereotype.Repository;
import com.erretechnology.intranet.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository(value = "RepositoryUtente")
public interface RepositoryUtente extends JpaRepository<Utente, Integer>{
	
	public Utente findByEmail(String email);
	public Utente findByTokenResetPassword(String tokenResetPassword);
	public Integer countByAttivoTrue();
}
