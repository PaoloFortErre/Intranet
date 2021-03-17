package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.repositories.RepositoryLog;
@Service
public class ServiceLogImpl implements ServiceLog{
	
	@Autowired
	private RepositoryLog repositoryLog;
	
	
	@Override
	public Log save(Log log) {
		// TODO Auto-generated method stub
		return repositoryLog.save(log);
	}

}
