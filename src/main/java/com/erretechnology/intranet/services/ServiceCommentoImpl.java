package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCommento;
@Service("serviceCommento")
public class ServiceCommentoImpl implements ServiceCommento{

	@Autowired
	private RepositoryCommento repositoryCommento;
	
	@Override
	public List<Commento> getLastMessage() {
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
		return repositoryCommento.findAll();
	}

	@Override
	@Async
	public CompletableFuture<List<Commento>> getAllNotVisible() throws InterruptedException {
		return CompletableFuture.completedFuture(repositoryCommento.findByVisibileFalse(Sort.by("timestampEliminazione").descending()));
	}
	
	@Override
	public List<Commento> getAllByAutore(UtenteDatiPersonali autore) {
		return repositoryCommento.findByAutore(autore);
	}
}
