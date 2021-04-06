package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Notifica;

@Repository
public interface RepositoryNotifica extends JpaRepository<Notifica, Integer> {

}
