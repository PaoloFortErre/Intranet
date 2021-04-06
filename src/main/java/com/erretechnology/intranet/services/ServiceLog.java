package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceLog {
	public Log save(Log log);
	public List<Log> findAll();
	public List<Log> findLastFive();
	public List<Log> findLogById(UtenteDatiPersonali id);
	public List<Log> findLastFiveLogById(UtenteDatiPersonali id);
}
