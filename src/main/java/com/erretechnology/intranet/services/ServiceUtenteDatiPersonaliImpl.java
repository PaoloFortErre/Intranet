package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryUtenteDatiPersonali;

@Service("serviceUtenteDati")
public class ServiceUtenteDatiPersonaliImpl implements ServiceUtenteDatiPersonali {

	@Autowired
	RepositoryUtenteDatiPersonali repositoryUtenteDatiPersonali;

	@Override
	public void save(UtenteDatiPersonali utente) {
		repositoryUtenteDatiPersonali.save(utente);
		// TODO Auto-generated method stub

	}

	@Override
	public UtenteDatiPersonali findByAutore(Utente autore) {
		if (getAll().stream().filter(x->x.getUtente().getId().equals(autore.getId())).count() == 1)
		return getAll().stream().filter(x-> x.getUtente().getId().equals(autore.getId())).findFirst().get();
		return null;
	}

	public List<UtenteDatiPersonali> getAll() {
		return repositoryUtenteDatiPersonali.findAll();
	}

	@Override
	public UtenteDatiPersonali findById(int id) {
		return repositoryUtenteDatiPersonali.findById(id).get();
	}
}
