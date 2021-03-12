package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.CommentoModificato;

@Repository(value = "RepositoryCommentoModficato")
public interface RepositoryCommentoModificato extends JpaRepository<CommentoModificato, Integer>{

}