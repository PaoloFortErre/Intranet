package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ComunicazioneHR")
public class ComunicazioneHR {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long timestamp;
	private String descrizione;
	private String nome;
	private boolean visibile;
	
	@Lob
	private byte[] data;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali utente;
	
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
	
	public UtenteDatiPersonali getUtente() {
		return utente;
	}
	public void setUtente(UtenteDatiPersonali utente) {
		this.utente = utente;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String nome) {
		this.descrizione = nome;
	}
	public boolean isVisibile() {
		return visibile;
	}
	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
