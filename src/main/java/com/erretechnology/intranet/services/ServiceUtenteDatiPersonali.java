package com.erretechnology.intranet.services;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceUtenteDatiPersonali {

	void save(UtenteDatiPersonali utente);
	UtenteDatiPersonali findById(int id);

	UtenteDatiPersonali findByAutore(Utente autore);

}
