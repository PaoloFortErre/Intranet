package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.PostModificato;

@Repository(value = "RepositoryPostModificato")
public interface RepositoryPostModificato extends JpaRepository<PostModificato, Integer> {

}
