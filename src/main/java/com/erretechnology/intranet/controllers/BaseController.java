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
import com.erretechnology.intranet.services.ServicePost;
import com.erretechnology.intranet.services.ServicePostModificato;
import com.erretechnology.intranet.services.ServiceRuolo;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

public abstract class BaseController {
	@Autowired
	ServiceAuthority serviceAuthority;
	@Autowired
	ServiceCommento serviceCommento;
	@Autowired
	ServiceCommentoModificato serviceCommentoModificato;
	@Autowired
	ServiceFileImmagini serviceFileImmagine;
	@Autowired
	ServiceFilePdf serviceFilePdf;
	@Autowired
	ServiceFileSystem serviceFileSystem;
	@Autowired
	ServiceLog serviceLog;
	@Autowired
	ServicePermesso servicePermesso;
	@Autowired
	ServicePost servicePost;
	@Autowired
	ServicePostModificato servicePostModificato;
	@Autowired
	ServiceRuolo serviceRuolo;
	@Autowired
	ServiceUtente serviceUtente;
	@Autowired
	ServiceUtenteDatiPersonali serviceDatiPersonali;
	
	protected void saveLog(String testo, UtenteDatiPersonali autore) {
		Log log = new Log();
		log.setTimestamp(Instant.now().getEpochSecond());
		log.setUtente(autore);
		log.setAzione(testo);
		serviceLog.save(log);
	}
}
