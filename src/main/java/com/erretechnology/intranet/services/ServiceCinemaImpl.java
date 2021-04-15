package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Cinema;
import com.erretechnology.intranet.repositories.RepositoryCinema;

@Service
public class ServiceCinemaImpl implements ServiceCinema {

	@Autowired
	RepositoryCinema repositoryCinema;
	
	@Override
	public void save(Cinema cinema) {
		repositoryCinema.save(cinema);
	}
	@Override
	public Cinema findById(int id) {
		return repositoryCinema.findById(id).get();
	}
	@Override
	public void deleteById(int id) {
		repositoryCinema.deleteById(id);
	}
	@Override
	@Async
	public CompletableFuture<List<Cinema>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryCinema.getAllNotVisible());
	}

}
