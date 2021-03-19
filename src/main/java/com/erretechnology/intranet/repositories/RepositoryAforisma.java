package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Aforisma;
import com.erretechnology.intranet.models.News;
@Repository
public interface RepositoryAforisma extends CrudRepository<Aforisma, Integer> {
	@Query("SELECT a FROM Aforisma a WHERE a.visibile = true ORDER BY a.id DESC")
	List<Aforisma> findAll();
}
