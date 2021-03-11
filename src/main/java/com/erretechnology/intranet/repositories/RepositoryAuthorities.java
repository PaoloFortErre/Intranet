package com.erretechnology.intranet.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Authority;
import com.erretechnology.intranet.models.AuthorityId;


@Repository(value = "RepositoryAuthorities")
public interface RepositoryAuthorities extends JpaRepository<Authority,AuthorityId>{
	
}
