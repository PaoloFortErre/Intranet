package com.erretechnology.intranet.services;

import java.util.List;
import java.util.Map;

import com.erretechnology.intranet.models.Authority;

public interface ServiceAuthority {
	public List<Authority> findByUserId(Integer id);
	public List<Authority> getAll();
	public Map<String,String> getMapById(int id);
}
