package com.erretechnology.intranet.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/*
 * 	MODELLO PER I PERMESSI CHE POSSONO ESSERE ASSEGNATI AGLI UTENTI
 */
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
	private Set<Utente> setUtenti;
	
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
