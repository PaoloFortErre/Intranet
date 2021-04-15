package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Aforisma;
import com.erretechnology.intranet.repositories.RepositoryAforisma;

@Service
public class ServiceAforismaImpl implements ServiceAforisma {

	@Autowired
	RepositoryAforisma repositoryAforisma;
	
	@Override
	public void save(Aforisma aforisma) {
		repositoryAforisma.save(aforisma);
	}
	@Override
	public Aforisma findById(int id) {
		return repositoryAforisma.findById(id).get();
	}

}
