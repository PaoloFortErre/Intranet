package com.erretechnology.intranet.services;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.repositories.RepositoryPodcast;

@Service
public class ServicePodcastImpl implements ServicePodcast{

	@Autowired
	private RepositoryPodcast repositoryPodcast;

	@Override
	public void save(Podcast podcast) {
		repositoryPodcast.save(podcast);
	}

	@Override
	public List<Podcast> getAll() {
		return repositoryPodcast.findByVisibileTrueGroupByNome();
	}

	@Override
	@Async
	public CompletableFuture<List<Podcast>> getAllNotVisible() {
		return CompletableFuture.completedFuture(repositoryPodcast.findByVisibileFalse(Sort.by("timestampEliminazione").descending()));
	}

	@Override
	public Podcast getById(int id) {
		return repositoryPodcast.findById(id).get();
	}

	@Override
	public boolean contains(byte[] data) {
		return repositoryPodcast.findByPodcast(data) != null;
	}

	@Override
	public Podcast getpodcastByData(byte[] data) {
		return repositoryPodcast.findByPodcast(data);
	}

	@Override
	public void remove(Podcast p) {
		repositoryPodcast.delete(p);
	}
}
