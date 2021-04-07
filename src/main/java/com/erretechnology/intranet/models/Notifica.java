package com.erretechnology.intranet.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Notifica {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String descrizione;
	private long timestamp;
	private UtenteDatiPersonali utente;
	@ManyToMany
	@JoinTable(name = "NotificheUtente",
    joinColumns = @JoinColumn(name = "id_notifica"),
    inverseJoinColumns = @JoinColumn(name = "id_utente"))
	private Set<UtenteDatiPersonali> setUtenti = new HashSet<UtenteDatiPersonali>();
	
	public Set<UtenteDatiPersonali> getSetUtenti() {
		return setUtenti;
	}
	public void setSetUtenti(Set<UtenteDatiPersonali> setUtenti) {
		this.setUtenti = setUtenti;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public void addUtente(UtenteDatiPersonali utente) {
		this.setUtenti.add(utente);
	}
	public UtenteDatiPersonali getUtente() {
		return utente;
	}
	public void setUtente(UtenteDatiPersonali utente) {
		this.utente = utente;
	}


	
}
