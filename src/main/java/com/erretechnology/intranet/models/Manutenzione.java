package com.erretechnology.intranet.models;

import javax.persistence.*;

/*
 * 	MODELLO CHE MANTIENE IL BOOLEANO PER SAPERE SE IL SITO È IN MANUTENZIONE O MENO
 */

@Entity
@Table(name = "maintenance")
public class Manutenzione {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(columnDefinition = "boolean default false")
	private Boolean manutenzione;

	public Boolean getManutenzione() {
		return manutenzione;
	}

	public void setManutenzione(Boolean manutenzione) {
		this.manutenzione = manutenzione;
	}
}
