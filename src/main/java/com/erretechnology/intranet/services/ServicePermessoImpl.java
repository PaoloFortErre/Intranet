package com.erretechnology.intranet.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.repositories.RepositoryPermesso;
@Service("servicePermesso")
public class ServicePermessoImpl implements ServicePermesso{
	
	@Autowired
	private RepositoryPermesso repositoryPermesso;

	@Override
	public void savePermesso(Permesso p) {
		repositoryPermesso.save(p);
	}

	@Override
	public List<Permesso> getAll() {
		return repositoryPermesso.findAll();
	}
	
	@Override
	public List<String> getAllName(){
		return getAll().stream().map(x-> x.getNome()).collect(Collectors.toList());
	}

	@Override
	public Permesso findById(String id) {
		return repositoryPermesso.findById(id).get();
	}
	
	
	
	

}
