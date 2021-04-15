package com.erretechnology.intranet.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryVideo;

@Service("serviceVideo")
public class ServiceVideoImpl implements ServiceVideo{

	@Autowired
	RepositoryVideo repositoryVideo;

	@Override
	@Async
	public CompletableFuture<VideoDelGiorno> getLastVideo(String nomePagina) {
		return CompletableFuture.completedFuture(repositoryVideo.findTopByPaginaOrderByIdDesc(nomePagina));
	}

	@Override
	public void save(VideoDelGiorno video) {
		repositoryVideo.save(video);
	}
}
