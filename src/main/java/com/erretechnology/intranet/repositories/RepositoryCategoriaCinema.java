package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.CategoriaCinema;

public interface RepositoryCategoriaCinema extends CrudRepository<CategoriaCinema, Integer>{
	@Query("SELECT c FROM CategoriaCinema c WHERE c.visibile = true ORDER BY c.id DESC")
	List<CategoriaCinema> findAll();
}
