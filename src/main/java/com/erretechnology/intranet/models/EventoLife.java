package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.Table;
/*
 * 	MODELLO PER GLI EVENTI MYLIFE
 */
@Entity
@Table(name = "event")
public class EventoLife extends Cultura implements MyWorkBean, Evento{
	
	private long data;
	private String luogo;
	private String link;
	
	public EventoLife() {

	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
