package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 	MODELLO PER I RUOLI CHE POSSONO ESSERE ASSEGNATI AGLI UTENTI (PER ORA ADMIN E USER)
 */
@Entity
@Table(name = "roles")
public class Ruolo {
	
	@Id
	private String nome;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
