package com.erretechnology.intranet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull
	@Column(length = 37)
	private String titolo;
	@NotNull
	@Lob
	private String contenuto;
	private long dataPubblicazione;
	@OneToOne()
	@JoinColumn(name = "id_immagine")
	private FileImmagine copertina;
	@ManyToOne
	@JoinColumn(name="autore_id")
	private UtenteDatiPersonali autore;
	private boolean visibile;
	
	public News() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public long getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(long dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	public FileImmagine getCopertina() {
		return copertina;
	}

	public void setCopertina(FileImmagine copertina) {
		this.copertina = copertina;
	}

	public UtenteDatiPersonali getAutore() {
		return autore;
	}

	public void setAutore(UtenteDatiPersonali autore) {
		this.autore = autore;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}

	
}
