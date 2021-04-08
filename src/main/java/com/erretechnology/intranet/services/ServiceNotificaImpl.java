package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Notifica;
import com.erretechnology.intranet.repositories.RepositoryNotifica;

@Service
public class ServiceNotificaImpl implements ServiceNotifica{

	@Autowired
	RepositoryNotifica repositoryNotifica;
	
	@Override
	public void save(Notifica notifica) {
		repositoryNotifica.save(notifica);
	}
	@Override
	public Notifica findById(int id) {
		return repositoryNotifica.findById(id).get();
	}

}
