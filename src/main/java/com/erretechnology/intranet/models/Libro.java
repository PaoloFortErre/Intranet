package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Libro extends Cultura {
	private String autore;

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}
	
}
