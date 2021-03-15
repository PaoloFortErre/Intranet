package com.erretechnology.intranet.models;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

	@ManyToMany(mappedBy = "setUtenti",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<Ruolo> setRuoli;
	@ManyToMany(mappedBy = "setUtenti", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<Permesso> setPermessi;
	
	public Utente() {
		setRuoli = new HashSet<Ruolo>();
		setPermessi = new HashSet<Permesso>();
	}
	
	
	public void addRuolo(Ruolo ruolo) {
		setRuoli.add(ruolo);
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
	public Set<Ruolo> getSetGruppi() {
		return setRuoli;
	}
	public void setSetGruppi(Set<Ruolo> setRuoli) {
		this.setRuoli = setRuoli;
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
}
