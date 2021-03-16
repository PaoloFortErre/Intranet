package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.FilePdf;
@Repository(value = "RepositoryFilePdf")
public interface RepositoryFilePdf extends JpaRepository<FilePdf, Integer>{
}
