package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.CommentoModificato;
import com.erretechnology.intranet.repositories.RepositoryCommentoModificato;
@Service("ServiceCommentoModificato")
public class ServiceCommentoModificatoImpl implements ServiceCommentoModificato{
	@Autowired
	RepositoryCommentoModificato repositoryCommentoModificato;
	@Override
	public void save(CommentoModificato cm) {
		repositoryCommentoModificato.save(cm);
		
	}

}
