package com.erretechnology.intranet.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceAuthority;
import com.erretechnology.intranet.services.ServiceCommento;
import com.erretechnology.intranet.services.ServiceCommentoModificato;
import com.erretechnology.intranet.services.ServiceFileImmagini;
import com.erretechnology.intranet.services.ServiceFilePdf;
import com.erretechnology.intranet.services.ServiceFileSystem;
import com.erretechnology.intranet.services.ServiceLog;
import com.erretechnology.intranet.services.ServicePermesso;
import com.erretechnology.intranet.services.ServicePodcast;
import com.erretechnology.intranet.services.ServicePost;
import com.erretechnology.intranet.services.ServicePostModificato;
import com.erretechnology.intranet.services.ServiceRuolo;
import com.erretechnology.intranet.services.ServiceSettore;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

public abstract class BaseController {
	@Autowired
	protected ServiceAuthority serviceAuthority;
	@Autowired
	protected ServiceCommento serviceCommento;
	@Autowired
	protected ServiceCommentoModificato serviceCommentoModificato;
	@Autowired
	protected ServiceFileImmagini serviceFileImmagine;
	@Autowired
	protected ServiceFilePdf serviceFilePdf;
	@Autowired
	protected ServiceFileSystem serviceFileSystem;
	@Autowired
	protected ServiceLog serviceLog;
	@Autowired
	protected ServicePermesso servicePermesso;
	@Autowired
	protected ServicePost servicePost;
	@Autowired
	protected ServicePostModificato servicePostModificato;
	@Autowired
	protected ServiceRuolo serviceRuolo;
	@Autowired
	protected ServiceUtente serviceUtente;
	@Autowired
	protected ServiceUtenteDatiPersonali serviceDatiPersonali;
	@Autowired
	protected ServiceSettore serviceSettore;
	@Autowired
	protected ServicePodcast servicePodcast;
	
	protected void saveLog(String testo, UtenteDatiPersonali autore) {
		Log log = new Log();
		log.setTimestamp(Instant.now().getEpochSecond());
		log.setUtente(autore);
		log.setAzione(testo);
		serviceLog.save(log);
	}
}
