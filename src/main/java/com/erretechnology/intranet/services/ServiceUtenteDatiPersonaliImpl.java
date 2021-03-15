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
	
	@Autowired
	ServiceUtente serviceUtente;

	@Override
	public void save(UtenteDatiPersonali utente) {
		repositoryUtenteDatiPersonali.saveAndFlush(utente);
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

	@Override
	public void insert(String psw, String email, UtenteDatiPersonali udp) {
		Utente utente = new Utente();
		utente.setEmail(email);
		utente.setPassword(psw);
		utente.setAttivo(true);
		serviceUtente.saveUtente(utente);
		udp.setPasswordCambiata(false);
		udp.setUtente(utente);
		System.out.println(udp.getUtente().getPassword());
		save(udp);
	}
}
