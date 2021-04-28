package com.erretechnology.intranet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
/*
 * 	MODELLO PER LA VIEW AUTHORITIES COMPRENDENTE I PERMESSI PER OGNI UTENTE
 */
@Entity
@Table(name = "authorities")
@IdClass(AuthorityId.class)
public class Authority {
	
	@Id
	@Column(name = "id_user")
	private Integer idUtente;

	@Id
	@Column(name = "authority")
	private String idPermesso;
	
	@Column(name = "username")
	private String email;
	
	@Column(name = "descrizione")
	private String descrizione;

	public Integer getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdPermesso() {
		return idPermesso;
	}

	public void setIdPermesso(String idPermesso) {
		this.idPermesso = idPermesso;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
