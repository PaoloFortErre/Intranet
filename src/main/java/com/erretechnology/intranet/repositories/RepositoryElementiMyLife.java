package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.ElementiMyLife;

@Repository(value = "RepositoryElementiMyLife")
public interface RepositoryElementiMyLife extends JpaRepository<ElementiMyLife, Integer>{

}
