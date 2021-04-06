package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Repository
public interface RepositoryLog extends JpaRepository<Log, Integer>{
	
	
	public List<Log> findFirst5ByUtenteOrderByIdDesc(UtenteDatiPersonali id);

	public List<Log> findFirst5ByOrderByIdDesc();
	
	public List<Log> findByUtente(UtenteDatiPersonali id, Sort sort);
}
