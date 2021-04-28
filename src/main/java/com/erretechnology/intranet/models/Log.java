package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/*
 * 	MODELLO PER I LOG, OVVERO TUTTE LE AZIONI CHE GLI UTENTI FARANNO SUL SITO
 */
@Entity
@Table(name = "log")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali utente;
	private long timestamp;
	private String azione;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UtenteDatiPersonali getUtente() {
		return utente;
	}
	public void setUtente(UtenteDatiPersonali utente) {
		this.utente = utente;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getAzione() {
		return azione;
	}
	public void setAzione(String azione) {
		this.azione = azione;
	}

}
