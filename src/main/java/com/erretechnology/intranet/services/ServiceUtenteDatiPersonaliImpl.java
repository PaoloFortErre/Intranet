package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Ruolo;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryUtenteDatiPersonali;

@Service("serviceUtenteDati")
public class ServiceUtenteDatiPersonaliImpl implements ServiceUtenteDatiPersonali {

	@Autowired
	RepositoryUtenteDatiPersonali repositoryUtenteDatiPersonali;
	@Autowired
	ServiceRuolo serviceRuolo;
	@Autowired
    ServicePermesso servicePermesso;
	@Autowired
    ServiceUtente serviceUtente;
	@Autowired
    ServiceSettore serviceSettore;
	@Autowired
    ServiceFileImmagini serviceFileImmagine;

	@Override
	public void save(UtenteDatiPersonali utente) {
		repositoryUtenteDatiPersonali.save(utente);
	}

	@Override
	public UtenteDatiPersonali findByAutore(Utente autore) {
		return repositoryUtenteDatiPersonali.findByUtente(autore);
	/*	if (getAll().stream().filter(x->x.getUtente().getId().equals(autore.getId())).count() == 1)
		return getAll().stream().filter(x-> x.getUtente().getId().equals(autore.getId())).findFirst().get();
		return null;*/
	}
	@Override
	@Async
	public CompletableFuture<List<UtenteDatiPersonali>> getAll() {
		return CompletableFuture.completedFuture(repositoryUtenteDatiPersonali.findAll());
	}

	@Override
	public UtenteDatiPersonali findById(int id) {
		return repositoryUtenteDatiPersonali.findById(id).get();
	}

	@Override
	public void insert(String psw, String email, String settore ,UtenteDatiPersonali udp){
		Utente utente = new Utente();
		utente.setEmail(email);
		utente.setPassword(psw);
		utente.setAttivo(true);
		Ruolo r = serviceRuolo.getById("USER");
		utente.setRuolo(r);
		Permesso p = servicePermesso.findById("UB");
		utente.addPermesso(p);
		
		serviceUtente.saveUtente(utente);
		p.addUtente(utente);
		/*r.addUtente(utente);
		utente.addRuolo(r);*/
		udp.setSettore(serviceSettore.findById(settore));
		udp.setPasswordCambiata(false);
		udp.setVisualizzaDataNascita(true);
		udp.setUtente(utente);
		udp.setImmagine(serviceFileImmagine.getImmagine(63));
		save(udp);
	}

	@Override
	public List<UtenteDatiPersonali> getInattivi() {
		return repositoryUtenteDatiPersonali.findByAttivoFalse();
	//	return repositoryUtenteDatiPersonali.findAll().stream().filter(x->x.getUtente().getAttivo() == false).collect(Collectors.toList());
	}
	
	@Override
	public List<UtenteDatiPersonali> getAttivi() {
		return repositoryUtenteDatiPersonali.findByAttivoTrue();
	//	return repositoryUtenteDatiPersonali.findAll().stream().filter(x->x.getUtente().getAttivo() == false).collect(Collectors.toList());
	}

}
