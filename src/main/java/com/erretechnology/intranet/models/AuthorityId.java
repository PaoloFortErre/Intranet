package com.erretechnology.intranet.models;

import java.io.Serializable;
import java.util.Objects;

public class AuthorityId implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer idUtente;
	private String idPermesso;
	public AuthorityId() {}
	public AuthorityId(Integer idUtente, String idPermesso)  {
		this.idUtente = idUtente;
		this.idPermesso = idPermesso;
	}
	public Integer getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}
	public String getIdPermesso() {
		return idPermesso;
	}
	public void setIdPermesso(String idPermesso) {
		this.idPermesso = idPermesso;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityId accountId = (AuthorityId) o;
        return idUtente.equals(accountId.idUtente) &&
        		idPermesso.equals(accountId.idPermesso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtente, idPermesso);
    }
}
