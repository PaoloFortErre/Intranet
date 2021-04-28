package com.erretechnology.intranet.models;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
/*
 * 	MODELLO PER LA VIEW CHE INCORPORA GLI ELEMENTI VISUALIZZATI SULLA PAGINA MYLIFE
 */
@Entity
@Table(name = "mylife_last_data")
public class ElementiMyLife {
	@Id
	private int id;
	@Lob
	private byte[] foto;
	private String titolo;
	private String contenuto;
	private Long timestamp;
	private String tipo;
	
	public byte[] getFoto() {
		return foto;
	}
	
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getContenuto() {
		return contenuto;
	}
	
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getId() {
		return id;
	}
	
	public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}
