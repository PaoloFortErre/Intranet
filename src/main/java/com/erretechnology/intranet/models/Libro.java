package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.Table;
/*
 * 	MODELLO PER I LIBRI, PRESENTI IN MYLIFE
 */
@Entity
@Table(name = "book")
public class Libro extends Cultura {
	private String scrittore;

	public String getScrittore() {
		return scrittore;
	}

	public void setScrittore(String scrittore) {
		this.scrittore = scrittore;
	}

	
	
}
