package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.repositories.RepositoryNews;

@Service
public class ServiceNewsImpl implements ServiceNews {

	@Autowired
	RepositoryNews repositoryNews;
	
	@Override
	public void save(News news) {
		repositoryNews.save(news);
	}
	@Override
	public News findById(int id) {
		return repositoryNews.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryNews.deleteById(id);
	}
	@Override
	@Async
	public CompletableFuture<List<News>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryNews.getAllNotVisible());
	}

}
