package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.News;

@Repository
public interface RepositoryNews extends CrudRepository<News, Integer>{
	List<News> findByOrderByDataPubblicazioneDesc();
}
 