package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
@Repository(value = "RepositoryCommento")
public interface RepositoryCommento extends JpaRepository<Commento, Integer>{
	
	public List<Commento> findByVisibileTrue(Sort sort);
	
	public List<Commento> findByVisibileFalse(Sort sort);
	
	public List<Commento> findByAutoreAndVisibileTrue(UtenteDatiPersonali autore);

}
