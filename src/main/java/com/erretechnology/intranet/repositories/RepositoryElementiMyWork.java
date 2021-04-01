package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.ElementiMyWork;

@Repository(value = "RepositoryElementiMyWork")
public interface RepositoryElementiMyWork extends JpaRepository<ElementiMyWork, Integer>{

}
