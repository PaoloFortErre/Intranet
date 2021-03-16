package com.erretechnology.intranet.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Ruolo {
	
	@Id
	private String nome;

	@ManyToMany
	@JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "id_role"),
    inverseJoinColumns = @JoinColumn(name = "id_user"))
	Set<Utente> setUtenti;
	@ManyToMany(mappedBy = "setRuoli")
	Set<Permesso> setPermessi;
	
	public Ruolo() {
		setUtenti = new HashSet<Utente>();
		setPermessi = new HashSet<Permesso>();
	}
	
	public void addUtente(Utente utente) {
		setUtenti.add(utente);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
