package com.erretechnology.intranet.models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.DiscriminatorType;
/*
 * 	MODELLO PER LR NOVITA' SU MYLIFE
 */
@Entity(name="news")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.INTEGER)

public class Novita implements MyWorkBean {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull
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
	private long timestampEliminazione;

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

	public long getTimestampEliminazione() {
		return timestampEliminazione;
	}

	public void setTimestampEliminazione(long timestampEliminazione) {
		this.timestampEliminazione = timestampEliminazione;
	}
}
