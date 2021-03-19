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
@Table(name = "permissions")
public class Permesso {
	@Id
	private String nome;
	
	private String descrizione;
	
	@ManyToMany
	@JoinTable(name = "users_permissions",
    joinColumns = @JoinColumn(name = "id_permission"),
    inverseJoinColumns = @JoinColumn(name = "id_user"))
	Set<Utente> setUtenti;
	@ManyToMany
	@JoinTable(name = "roles_permissions",
    joinColumns = @JoinColumn(name = "id_permission"),
    inverseJoinColumns = @JoinColumn(name = "id_role"))
	Set<Ruolo> setRuoli;
	
	public Permesso() {
		setUtenti = new HashSet<Utente>();
		setRuoli = new HashSet<Ruolo>();
	}
	
	public void addRuolo(Ruolo ruolo) {
		setRuoli.add(ruolo);
	}
	
	public void addUtente(Utente utente) {
		setUtenti.add(utente);
	}
	
	public void removeUtente(Utente utente) {
		setUtenti.remove(utente);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
