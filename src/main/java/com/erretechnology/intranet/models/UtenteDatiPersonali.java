package com.erretechnology.intranet.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DatiUtente")
public class UtenteDatiPersonali implements Serializable{
	private static final long serialVersionUID = -828732162220395783L;
	@Id
	@Column(name="id") 
	private int id;
	@MapsId	
	@OneToOne()
	@JoinColumn(name = "id_user")
	private Utente utente;
	private String descrizione;
	private long dataNascita;
	private String nome;
	private String cognome;
	private String settore;
	private Boolean passwordCambiata;
	private String immagine;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getPasswordCambiata() {
		return passwordCambiata;
	}

	public void setPasswordCambiata(Boolean passwordCambiata) {
		this.passwordCambiata = passwordCambiata;
	}

	public UtenteDatiPersonali(){	}

	public long getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(long dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	/*
	public int getIdTMP() {
		return idTMP;
	}

	public void setIdTMP(int idTMP) {
		this.idTMP = idTMP;
	}*/

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public boolean isPasswordCambiata() {
		return passwordCambiata;
	}

	public void setPasswordCambiata(boolean passwordCambiata) {
		this.passwordCambiata = passwordCambiata;
	}
	public String getImmagine() {
		return immagine;
	}
	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
}
