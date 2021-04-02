package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.ElementiMyLife;
import com.erretechnology.intranet.repositories.RepositoryElementiMyLife;
@Service("serviceElementiMyLife")
public class ServiceElementiMyLifeImpl implements ServiceElementiMyLife{
	@Autowired
	RepositoryElementiMyLife repositoryElementiMyLife;
	@Override
	public List<ElementiMyLife> findAll() {
		return repositoryElementiMyLife.findAll();
	}

}
