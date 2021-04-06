package com.erretechnology.intranet.repositories;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Authority;
import com.erretechnology.intranet.models.AuthorityId;


@Repository(value = "RepositoryAuthorities")
public interface RepositoryAuthorities extends JpaRepository<Authority,AuthorityId>{
	@Query
	public List<Authority> findByIdUtente(int idUtente);
}
