package com.erretechnology.intranet.models;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

@Entity
@Table(name = "users", uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class Utente{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(unique=true)
	private String email;
	private String password;
	private Boolean attivo;
	@Column(name = "reset_password_token")
	private String tokenResetPassword;
	private long timestampToken;
	@ManyToMany(mappedBy = "setUtenti", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<Permesso> setPermessi;
	@ManyToOne
	@JoinColumn(name = "nome_ruolo")
	private Ruolo ruolo;
	
	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public Utente() {
		setPermessi = new HashSet<Permesso>();
	}
	
	public void removePermesso(Permesso permesso) {
		setPermessi.remove(permesso);
	}
	
	public void addPermesso(Permesso permesso) {
		setPermessi.add(permesso);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Set<Permesso> getSetPermessi() {
		return setPermessi;
	}
	public void setSetPermessi(Set<Permesso> setPermessi) {
		this.setPermessi = setPermessi;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getAttivo() {
		return attivo;
	}
	public void setAttivo(Boolean attivo) {
		this.attivo = attivo;
	}


	public String getTokenResetPassword() {
		return tokenResetPassword;
	}


	public void setTokenResetPassword(String tokenResetPassword) {
		this.tokenResetPassword = tokenResetPassword;
	}

	public long getTimestampToken() {
		return timestampToken;
	}

	public void setTimestampToken(long timestampToken) {
		this.timestampToken = timestampToken;
	}
}
