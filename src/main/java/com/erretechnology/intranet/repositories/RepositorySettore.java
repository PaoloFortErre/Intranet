package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Settore;
@Repository(value = "RepositorySettore")
public interface RepositorySettore extends JpaRepository<Settore, String> {

}
