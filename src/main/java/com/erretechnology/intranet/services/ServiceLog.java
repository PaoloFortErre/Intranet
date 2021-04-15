package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Log;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceLog {
	public Log save(Log log);
	public CompletableFuture<List<Log>> findAll();
	public CompletableFuture<List<Log>> findLastFive();
	public CompletableFuture<List<Log>> findLogById(UtenteDatiPersonali id);
	public CompletableFuture<List<Log>> findLastFiveLogById(UtenteDatiPersonali id);
}
