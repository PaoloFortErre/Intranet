package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erretechnology.intranet.models.PostLinkedin;

public interface RepositoryLinkedin extends CrudRepository<PostLinkedin, Integer>{
	
	@Query(value = "SELECT * FROM linkedin ORDER BY id DESC LIMIT ?1", nativeQuery = true)
	List<PostLinkedin> findLimit(int number);
}
