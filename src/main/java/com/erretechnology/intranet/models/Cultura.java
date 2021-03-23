package com.erretechnology.intranet.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class Cultura {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String titolo;
	@OneToOne
	@JoinColumn(name = "id_immagine")
	private FileImmagine copertina;
	private boolean visibile;
	
	public Cultura() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public FileImmagine getCopertina() {
		return copertina;
	}
	public void setCopertina(FileImmagine copertina) {
		this.copertina = copertina;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	
	
}