package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "event")
public class Evento extends News {
	private long data;
	@OneToOne
	@JoinColumn(name = "id_indirizzo")
	private Indirizzo indirizzo;
	
	public long getData() {
		return data;
	}
	public void setData(long data) {
		this.data = data;
	}
	public Indirizzo getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
	}	
	
	
}
