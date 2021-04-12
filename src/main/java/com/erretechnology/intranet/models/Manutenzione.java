package com.erretechnology.intranet.models;

import javax.persistence.*;
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
