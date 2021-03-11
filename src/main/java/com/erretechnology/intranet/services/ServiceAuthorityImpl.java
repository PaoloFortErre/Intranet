package com.erretechnology.intranet.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Authority;
import com.erretechnology.intranet.repositories.RepositoryAuthorities;

@Service("authorityService")
public class ServiceAuthorityImpl implements ServiceAuthority{
	@Autowired
	private RepositoryAuthorities authoritiesRepository;
	@Override
	public List<Authority> findByUsertId(Integer id) {
		// TODO Auto-generated method stub
		return getAll().stream().filter(x -> x.getIdUtente()==id).collect(Collectors.toList());
	}
	
	@Override
	public List<Authority> getAll() {
		return authoritiesRepository.findAll();
	}
}
