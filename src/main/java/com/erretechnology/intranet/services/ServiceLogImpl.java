package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.Post;
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
	public List<Log> findLogById(int id) {
		return repositoryLog.findAll().stream()
				.filter(x-> x.getUtente().getId() == id)
				.sorted(Comparator.comparingInt(Log::getId).reversed())
				.limit(5)
				.collect(Collectors.toList());
	}

}
