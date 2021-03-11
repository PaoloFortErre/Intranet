package com.erretechnology.intranet.repositories;


import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Permesso;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository(value = "RepositoryPermesso")
public interface RepositoryPermesso extends JpaRepository<Permesso, String>{
	
}
