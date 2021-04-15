package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.erretechnology.intranet.models.Podcast;

public interface RepositoryPodcast extends JpaRepository<Podcast, Integer>{
	@Query("FROM Podcast p group by p.nome order by p.timestamp desc")
	public List<Podcast> findByVisibileTrueGroupByNome();
	
	@Query
	public List<Podcast> findByVisibileFalse(Sort sort);
	
	@Query
	public Podcast findByPodcast(byte[] data);
}
