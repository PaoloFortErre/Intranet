package com.erretechnology.intranet.repositories;


import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Utente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository(value = "RepositoryUtente")
public interface RepositoryUtente extends JpaRepository<Utente, Integer>{
	/*@Query(value = "SELECT * FROM authorities a WHERE a.id_user = ?1  AND a.authority = ?2 " , 
			  nativeQuery = true)
	public List<Permesso> getPermessi(int id, String permesso);*/
}
