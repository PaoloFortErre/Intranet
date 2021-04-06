package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.repositories.RepositoryCommento;
@Service("serviceCommento")
public class ServiceCommentoImpl implements ServiceCommento{

	@Autowired
	private RepositoryCommento repositoryCommento;
	
	@Override
	public List<Commento> getLastMessage() {
	//	return repositoryCommento.findAll().stream().filter(x -> x.isVisibile()).sorted(Comparator.comparingInt(Commento::getId).reversed()).collect(Collectors.toList());
		return repositoryCommento.findByVisibileTrue(Sort.by("id"));
	}

	@Override
	public void save(Commento commento) {
		repositoryCommento.save(commento);
		
	}

	@Override
	public void delete(Commento commento) {
		repositoryCommento.delete(commento);
		
	}

	@Override
	public Commento findById(int id) {
		return repositoryCommento.findById(id).get();
	}

	@Override
	public List<Commento> getAll() {
		// TODO Auto-generated method stub
		return repositoryCommento.findAll();
	}

	@Override
	public List<Commento> getAllNotVisible() {
		// TODO Auto-generated method stub
	//	return getAll().stream().filter(x->x.isVisibile() == false).collect(Collectors.toList());
		return repositoryCommento.findByVisibileFalse(Sort.by("id"));
	}

}
