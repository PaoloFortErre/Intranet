package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Log;

public interface ServiceLog {
	public Log save(Log log);

	public List<Log> findLogById(int id);
}
