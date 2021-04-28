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
/*
 * 	MODELLO PER TENERE TRACCIA DELLE IMMAGINE CARICATE DAGLI UTENTI
 */

@Entity
@Table(name = "fileImmagini")
public class FileImmagine{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private long timestamp;
	
	private String nomeFile;
	@Lob
    private byte[] data;
	@ManyToOne
	@JoinColumn(name = "id_autore")
	private UtenteDatiPersonali autore;

	
	public Integer getId() {
		return id;
	}

	public UtenteDatiPersonali getAutore() {
		return autore;
	}

	public void setAutore(UtenteDatiPersonali autore) {
		this.autore = autore;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
	
}
