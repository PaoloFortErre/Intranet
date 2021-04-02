package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "mylife_last_data")
public class ElementiMyLife {
	@Id
	private int id;
	private Integer foto;
	private String titolo;
	private String contenuto;
	private Long timestamp;
	private String tipo;
	@Transient
	private FileImmagine immagine;
	@Transient
	private CategoriaCinema cinema;
	
	public CategoriaCinema getCinema() {
		return cinema;
	}
	public void setCinema(CategoriaCinema cinema) {
		this.cinema = cinema;
	}
	public FileImmagine getImmagine() {
		return immagine;
	}
	public void setImmagine(FileImmagine immagine) {
		this.immagine = immagine;
	}
	public Integer getFoto() {
		return foto;
	}
	public void setFoto(Integer foto) {
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getId() {
		return id;
	}
}
