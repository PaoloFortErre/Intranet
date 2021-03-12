package com.erretechnology.intranet.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "DatiUtente")
public class UtenteDatiPersonali implements Serializable{

	//private ?? immagineProfilo;
	
/*	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name= "IdTmp")
	private int idTMP;
*/

	/**
	 * 
	 */
	private static final long serialVersionUID = -828732162220395783L;
	private String descrizione;
	private long dataNascita;
	
	@Id
	@Column(name="id_user") 
	private Integer id;
	
	
	@MapsId	
	@OneToOne()
	@JoinColumn(name = "id_user")
	private Utente utente;
	
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
}
