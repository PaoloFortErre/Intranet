package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Commento;
@Repository(value = "RepositoryCommento")
public interface RepositoryCommento extends JpaRepository<Commento, Integer>{

}
