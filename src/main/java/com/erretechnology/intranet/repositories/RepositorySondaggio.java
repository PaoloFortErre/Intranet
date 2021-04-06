package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Repository(value = "RepositorySondaggio")
public interface RepositorySondaggio extends JpaRepository<Sondaggio, Integer>{
	@Query
	public List<Sondaggio> findByVisibileTrue(Sort sort);
	
	@Query
	public List<Sondaggio> findByVisibileFalse(Sort sort);
	
	@Query
	public List<Sondaggio> findByAutore(UtenteDatiPersonali autore);
}
