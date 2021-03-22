package com.erretechnology.intranet.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Authority;
import com.erretechnology.intranet.repositories.RepositoryAuthorities;

@Service("authorityService")
public class ServiceAuthorityImpl implements ServiceAuthority{
	@Autowired
	private RepositoryAuthorities authoritiesRepository;
	@Override
	public List<Authority> findByUserId(Integer id) {
		// TODO Auto-generated method stub
		return getAll().stream().filter(x -> x.getIdUtente()==id).collect(Collectors.toList());
	}
	
	@Override
	public List<Authority> getAll() {
		return authoritiesRepository.findAll();
	}
	@Override
	public Map<String,String> getMapById(int id){
		List<String> list1 = findByUserId(id).stream().map(x-> x.getIdPermesso()).collect(Collectors.toList());
		List<String> list2  = findByUserId(id).stream().map(x-> x.getDescrizione()).collect(Collectors.toList());
		return IntStream.range(0, Math.min( list1.size(), list2.size()))
			   .boxed()
			   .collect(Collectors.toMap(list1::get, list2::get));
	}
}
