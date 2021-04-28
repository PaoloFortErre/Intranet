package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
/*
 * 	MODELLO PER TENERE TRACCIA DEI POST MODIFICATI
 */
@Entity
@Table(name = "old_post")
public class PostModificato {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String testo;
	private long timestamp;
	@OneToOne
	@JoinColumn(name = "id_post", referencedColumnName = "id")
	private Post post;
	
	//private Set<Commento> setCommentiOrdinati = new TreeSet<Commento>();

	public PostModificato(String testo) {
		this.setTesto(testo);
	}
	
	public PostModificato() {}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
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


	public Post getPost() {
		return post;
	}


	public void setPost(Post post) {
		this.post = post;
	}
}

