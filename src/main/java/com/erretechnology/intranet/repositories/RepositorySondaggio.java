package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Sondaggio;

@Repository(value = "RepositorySondaggio")
public interface RepositorySondaggio extends JpaRepository<Sondaggio, Integer>{
}
