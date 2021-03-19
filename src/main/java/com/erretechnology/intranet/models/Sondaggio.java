package com.erretechnology.intranet.models;

import javax.persistence.*;

@Entity
@Table(name = "sondaggio")
public class Sondaggio {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String nomeSondaggio;
	private String link;
	private long timestamp;
	private boolean visibile;
	
	public int getId() {
		return id;
	}
	@OneToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;
	
	public UtenteDatiPersonali getAutore() {
		return autore;
	}
	public void setAutore(UtenteDatiPersonali autore) {
		this.autore = autore;
	}
	public String getNomeSondaggio() {
		return nomeSondaggio;
	}
	public void setNomeSondaggio(String nomeSondaggio) {
		this.nomeSondaggio = nomeSondaggio;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
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