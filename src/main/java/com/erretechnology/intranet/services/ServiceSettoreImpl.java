package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Settore;
import com.erretechnology.intranet.repositories.RepositorySettore;
@Service("serviceSettore")
public class ServiceSettoreImpl implements ServiceSettore {
	@Autowired
	RepositorySettore repositorySettore;
	
	@Override
	public Settore findById(String settore) {
		return repositorySettore.findById(settore).get();
	}

}
