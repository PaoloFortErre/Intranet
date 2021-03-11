
package com.erretechnology.intranet.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String testo;
	private long timestamp;
	private boolean visibile;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private Utente autore;
	@OneToMany(mappedBy = "post")
	private Set<Commento> setCommenti;
	
	public Set<Commento> getSetCommenti() {
		return setCommenti;
	}

	public void setSetCommenti(Set<Commento> setCommenti) {
		this.setCommenti = setCommenti;
	}

	public Post(String testo) {
		this.setTesto(testo);
	}
	
	public Post() {}

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

	public Utente getAutore() {
		return autore;
	}

	public void setAutore(Utente autore) {
		this.autore = autore;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
}
