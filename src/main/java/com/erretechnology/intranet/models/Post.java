
package com.erretechnology.intranet.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	private long timestampEliminazione;
	@OneToOne()
	@JoinColumn(name = "id_immagine")
	private FileImmagine immagine;
	
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;
	@OneToMany(mappedBy = "post")
	private List<Commento> setCommenti;
	
	//private Set<Commento> setCommentiOrdinati = new TreeSet<Commento>();
	
	public List<Commento> getSetCommenti() {
		return setCommenti;
	}

	public void setSetCommenti(List<Commento> setCommenti) {
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

	public UtenteDatiPersonali getAutore() {
		return autore;
	}

	public void setAutore(UtenteDatiPersonali autore) {
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

	public FileImmagine getImmagine() {
		return immagine;
	}

	public void setImmagine(FileImmagine immagine) {
		this.immagine = immagine;
	}

	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}

	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}	
}
