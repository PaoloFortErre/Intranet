package com.erretechnology.intranet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/*
 * 	MODELLO PER TENERE TRACCIA DEI COMMENTI MODIFICATI
 */
@Entity
@Table(name = "old_comment")
public class CommentoModificato /*extends Post*/ {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	private String testo;
	private long timestamp;
	@OneToOne
	@JoinColumn(name = "id_commento", referencedColumnName = "id_commento")
	private Commento commento;
	
	
	public Commento getCommento() {
		return commento;
	}

	public void setCommento(Commento commento) {
		this.commento = commento;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTesto() {
		return testo;
	}
	
	public void setTesto(String testo) {
		this.testo = testo;
	}
}
