package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.News;

@Repository
public interface RepositoryNews extends CrudRepository<News, Integer>{
	
	@Query("SELECT n FROM News n WHERE n.visibile = false ORDER BY timestampEliminazione DESC")
	List<News> getAllNotVisible();
}
 