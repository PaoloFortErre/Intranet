package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
