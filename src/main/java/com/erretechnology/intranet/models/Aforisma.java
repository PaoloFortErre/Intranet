package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "aphorism")
public class Aforisma {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String frase;
	private String autore;
	@ManyToOne
	@JoinColumn(name="utente_id")
	private Utente utente;
	@Lob
	private byte[] immagine;
	//	private boolean visibile;
	private boolean isLife;
	
	

	public byte[] getImmagine() {
		return immagine;
	}

	public void setImmagine(byte[] immagine) {
		this.immagine = immagine;
	}

	public Aforisma() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrase() {
		return frase;
	}
	public void setFrase(String frase) {
		this.frase = frase;
	}
	public String getAutore() {
		return autore;
	}
	public void setAutore(String autore) {
		this.autore = autore;
	}
	public Utente getUtente() {
		return utente;
	}
	
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public boolean isLife() {
		return isLife;
	}

	public void setLife(boolean isLife) {
		this.isLife = isLife;
	}
	
	
}
