package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.FileImmagine;

@Repository(value = "RepositoryFileImmagini")
public interface RepositoryFileImmagine extends JpaRepository<FileImmagine, Integer> {

}
