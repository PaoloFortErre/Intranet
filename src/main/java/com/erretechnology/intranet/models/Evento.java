package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "event")
public class Evento implements MyWorkBean{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String titolo;
	private String contenuto;
	@OneToOne
	@JoinColumn(name = "id_immagine")
	private FileImmagine copertina;
	private long data;
	@OneToOne
	@JoinColumn(name = "id_indirizzo")
	private Indirizzo indirizzo;
	@ManyToOne
	@JoinColumn(name="autore_id")
	private UtenteDatiPersonali autore;
	private boolean visibile;
	private boolean isLife;
	private long timestampEliminazione;
	
	public Evento() {

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

	public FileImmagine getCopertina() {
		return copertina;
	}

	public void setCopertina(FileImmagine copertina) {
		this.copertina = copertina;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public Indirizzo getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
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

	public boolean isLife() {
		return isLife;
	}

	public void setLife(boolean isLife) {
		this.isLife = isLife;
	}

	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}

	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}
	
}
