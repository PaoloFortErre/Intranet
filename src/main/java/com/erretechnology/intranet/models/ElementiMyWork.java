package com.erretechnology.intranet.models;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 	MODELLO PER LA VIEW CHE INCORPORA GLI ELEMENTI VISUALIZZATI SULLA PAGINA MYWORK
 */
@Entity
@Table(name = "mywork_last_data")
public class ElementiMyWork {
	@Id
	private int id;
	private String foto;
	private String titolo;
	private String contenuto;
	private Long timestamp;
	private String luogo;
	private String tipo;
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
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
	public String getLuogo() {
		return luogo;
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
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
