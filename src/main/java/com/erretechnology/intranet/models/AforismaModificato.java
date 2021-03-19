package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "old_aphorism")
public class AforismaModificato {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String frase;
	private String autore;
	@OneToOne
	@JoinColumn(name = "id_aforisma")
	private Aforisma aforisma;
	
	public AforismaModificato() {
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

	public Aforisma getAforisma() {
		return aforisma;
	}

	public void setAforisma(Aforisma aforisma) {
		this.aforisma = aforisma;
	}
	
}
