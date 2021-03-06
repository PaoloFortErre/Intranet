package com.erretechnology.intranet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/*
 * 	MODELLO PER I COMMENTI AI POST, PRESENTI IN MYLIFE (VERSIONE CON BACHECA)
 */
@Entity
@Table(name = "comment")
public class Commento /*extends Post*/ {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_commento")
	private int id;
	private String testo;
	private long timestamp;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;
	@ManyToOne
	@JoinColumn(name = "id_post")
	private Post post;
	private boolean visibile;
	private long timestampEliminazione;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UtenteDatiPersonali getAutore() {
		return autore;
	}
	public void setAutore(UtenteDatiPersonali autore) {
		this.autore = autore;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public boolean isVisibile() {
		return visibile;
	}
	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
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
	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}
	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}
}
