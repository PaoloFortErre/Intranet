package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.PostModificato;
import com.erretechnology.intranet.repositories.RepositoryPostModificato;

@Service("ServicePostModificatoImpl")
public class ServicePostModificatoImpl implements ServicePostModificato {
	@Autowired
	RepositoryPostModificato repositoryPostModificato;

	@Override
	public void save(PostModificato pm) {
		repositoryPostModificato.save(pm);
	}
	
	
}
