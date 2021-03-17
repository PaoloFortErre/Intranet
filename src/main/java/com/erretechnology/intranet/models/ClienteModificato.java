package com.erretechnology.intranet.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "old_client")
public class ClienteModificato extends Cliente {
	@OneToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
