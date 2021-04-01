package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.repositories.RepositoryElementiMyWork;
@Service("serviceElementiMyWork")
public class ServiceElementiMyWorkImpl implements ServiceElementiMyWork{
	
	@Autowired
	private RepositoryElementiMyWork repositoryElementiMyWork;
	
	@Override
	public List<ElementiMyWork> findAll() {
		return repositoryElementiMyWork.findAll();
	}

}
