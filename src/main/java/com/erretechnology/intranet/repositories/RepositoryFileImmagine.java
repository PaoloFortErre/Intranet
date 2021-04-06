package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
@Repository(value = "RepositoryFileImmagini")
public interface RepositoryFileImmagine extends JpaRepository<FileImmagine, Integer> {
	@Query
	public FileImmagine findTopByAutore(UtenteDatiPersonali autore, Sort sort);
	@Query
	public FileImmagine findByData(byte[] data);
}
