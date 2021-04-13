package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Repository(value = "RepositoryPost")

public interface RepositoryPost extends JpaRepository<Post, Integer>{

	@Query
	public List<Post> findByVisibileTrue(Sort sort);
	
	@Query
	public List<Post> findByVisibileFalse(Sort sort);
	@Query
	public List<Post> findByAutore(UtenteDatiPersonali u);
	
	@Query
	public List<Post> findTop6ByAutoreAndVisibileTrueOrderByIdDesc(UtenteDatiPersonali u);
}
