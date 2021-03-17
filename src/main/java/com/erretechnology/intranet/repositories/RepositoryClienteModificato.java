package com.erretechnology.intranet.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.ClienteModificato;

@Repository
public interface RepositoryClienteModificato extends CrudRepository<ClienteModificato, Integer>{

}
