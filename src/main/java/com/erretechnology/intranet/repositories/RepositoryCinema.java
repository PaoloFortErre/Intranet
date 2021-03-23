package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.Cinema;
import com.erretechnology.intranet.models.Cliente;

public interface RepositoryCinema extends CrudRepository<Cinema, Integer>{
	@Query("SELECT c FROM Cinema c WHERE c.visibile = true ORDER BY c.id DESC")
	List<Cinema> findAll();
	
	@Query(value = "SELECT * FROM cinema WHERE visibile = true ORDER BY id DESC LIMIT ?1", nativeQuery = true)
	List<Cinema> findLimit(int number);
}
