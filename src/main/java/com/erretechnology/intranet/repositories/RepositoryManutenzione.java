package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Manutenzione;
@Repository
public interface RepositoryManutenzione extends JpaRepository<Manutenzione, Integer>{
}
