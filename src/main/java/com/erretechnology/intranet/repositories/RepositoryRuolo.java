package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Ruolo;

@Repository(value = "RepositoryRuolo")
public interface RepositoryRuolo extends JpaRepository<Ruolo, String>{
}