package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Authority;

public interface ServiceAuthority {
	public List<Authority> findByUsertId(Integer id);

	public List<Authority> getAll();
}
