package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryLog;
@Service
public class ServiceLogImpl implements ServiceLog{
	
	@Autowired
	private RepositoryLog repositoryLog;
	
	
	@Override
	public Log save(Log log) {
		return repositoryLog.save(log);
	}


	@Override
	public List<Log> findLogById(UtenteDatiPersonali u) {
		return repositoryLog.findByUtente(u, Sort.by("id").descending());
/*		return repositoryLog.findAll().stream()
				.filter(x-> x.getUtente().getId() == id)
				.sorted(Comparator.comparingInt(Log::getId).reversed())
				.collect(Collectors.toList());
	*/}


	@Override
	public List<Log> findAll() {
		return repositoryLog.findAll(Sort.by("id").descending());
		// TODO Auto-generated method stub
		//return repositoryLog.findAll().stream().sorted(Comparator.comparingInt(Log::getId).reversed())
			//	.collect(Collectors.toList());

	}


	@Override
	public List<Log> findLastFive() {
		return repositoryLog.findFirst5ByOrderByIdDesc();
		// TODO Auto-generated method stub
	/*	return repositoryLog.findAll().stream().sorted(Comparator.comparingInt(Log::getId).reversed())
				.limit(5).collect(Collectors.toList());
*/
	}


	@Override
	public List<Log> findLastFiveLogById(UtenteDatiPersonali id) {
		// TODO Auto-generated method stub
		return repositoryLog.findFirst5ByUtenteOrderByIdDesc(id);
	/*	return repositoryLog.findAll().stream()
				.filter(x-> x.getUtente().getId() == id)
				.sorted(Comparator.comparingInt(Log::getId).reversed())
				.limit(5)
				.collect(Collectors.toList());*/
	}

}
