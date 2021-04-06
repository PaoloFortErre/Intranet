package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Cinema;

public interface RepositoryCinema extends CrudRepository<Cinema, Integer>{

	@Query("SELECT c FROM Cinema c WHERE c.visibile = false")
	List<Cinema> getAllNotVisible();
}
