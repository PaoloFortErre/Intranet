package com.erretechnology.intranet.models;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "podcast")
public class Podcast {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String nome;
	@Lob
    private byte[] podcast;
	private long timestamp;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali utente;
	private long dataPodcast;
	private Boolean visibile;
	private long timestampEliminazione;
	
	public UtenteDatiPersonali getUtente() {
		return utente;
	}
	public void setUtente(UtenteDatiPersonali utente) {
		this.utente = utente;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public byte[] getPodcast() {
		return podcast;
	}
	public void setPodcast(byte[] podcast) {
		this.podcast = podcast;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getAudioData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
	public long getDataPodcast() {
		return dataPodcast;
	}
	public void setDataPodcast(long dataPodcast) {
		this.dataPodcast = dataPodcast;
	}
	public Boolean getVisibile() {
		return visibile;
	}
	public void setVisibile(Boolean visibile) {
		this.visibile = visibile;
	}
	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}
	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}


}
