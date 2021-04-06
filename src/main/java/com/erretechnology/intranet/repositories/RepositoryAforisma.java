package com.erretechnology.intranet.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Aforisma;

@Repository
public interface RepositoryAforisma extends CrudRepository<Aforisma, Integer> {

}
