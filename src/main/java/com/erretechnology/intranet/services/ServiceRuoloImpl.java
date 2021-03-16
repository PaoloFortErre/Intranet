package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Ruolo;
import com.erretechnology.intranet.repositories.RepositoryRuolo;

@Service("serviceRuolo")
public class ServiceRuoloImpl implements ServiceRuolo {
	@Autowired
	RepositoryRuolo repositoryRuolo;
	
	@Override
	public Ruolo getById(String ruolo) {
		return repositoryRuolo.findById(ruolo).get();
	}
}
