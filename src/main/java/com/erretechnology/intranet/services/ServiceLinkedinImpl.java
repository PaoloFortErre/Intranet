package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.PostLinkedin;
import com.erretechnology.intranet.repositories.RepositoryLinkedin;

@Service
public class ServiceLinkedinImpl implements ServiceLinkedin {

	@Autowired
	RepositoryLinkedin repositoryLinkedin;
	
	@Override
	public void save(PostLinkedin Linkedin) {
		repositoryLinkedin.save(Linkedin);
	}
	@Override
	public void deleteById(int id) {
		repositoryLinkedin.deleteById(id);
	}

}
