package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/*
 * 	MODELLO PER GLI I FILM PRESENTI IN MYLIFE
 */
@Entity
public class Cinema extends Cultura {
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private CategoriaCinema categoria;

	public CategoriaCinema getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaCinema categoria) {
		this.categoria = categoria;
	}
	
}
