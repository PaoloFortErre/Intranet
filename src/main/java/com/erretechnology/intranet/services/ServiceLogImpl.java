package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
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
	@Async
	public CompletableFuture<List<Log>> findLogById(UtenteDatiPersonali u) {
		return CompletableFuture.completedFuture(repositoryLog.findByUtente(u, Sort.by("id").descending()));
/*		return repositoryLog.findAll().stream()
				.filter(x-> x.getUtente().getId() == id)
				.sorted(Comparator.comparingInt(Log::getId).reversed())
				.collect(Collectors.toList());
	*/}


	@Override
	@Async
	public CompletableFuture<List<Log>> findAll() {
		return CompletableFuture.completedFuture(repositoryLog.findAll(Sort.by("id").descending()));
		// TODO Auto-generated method stub
		//return repositoryLog.findAll().stream().sorted(Comparator.comparingInt(Log::getId).reversed())
			//	.collect(Collectors.toList());

	}


	@Override
	public CompletableFuture<List<Log>> findLastFive() {
		return CompletableFuture.completedFuture(repositoryLog.findFirst5ByOrderByIdDesc());
		// TODO Auto-generated method stub
	/*	return repositoryLog.findAll().stream().sorted(Comparator.comparingInt(Log::getId).reversed())
				.limit(5).collect(Collectors.toList());
*/
	}


	@Override	
	@Async
	public CompletableFuture<List<Log>> findLastFiveLogById(UtenteDatiPersonali id) {
		// TODO Auto-generated method stub
		return CompletableFuture.completedFuture(repositoryLog.findFirst5ByUtenteOrderByIdDesc(id));
	/*	return repositoryLog.findAll().stream()
				.filter(x-> x.getUtente().getId() == id)
				.sorted(Comparator.comparingInt(Log::getId).reversed())
				.limit(5)
				.collect(Collectors.toList());*/
	}

}
