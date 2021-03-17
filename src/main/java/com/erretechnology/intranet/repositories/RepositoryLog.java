package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Log;

@Repository
public interface RepositoryLog extends JpaRepository<Log, Integer>{

}
