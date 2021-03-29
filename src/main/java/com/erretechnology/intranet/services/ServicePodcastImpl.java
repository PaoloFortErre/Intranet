package com.erretechnology.intranet.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
		return repositoryPodcast.findAll().stream().filter(x -> x.getVisibile()).collect(Collectors.toList());
	}

	@Override
	public Podcast getById(int id) {
		return repositoryPodcast.findById(id).get();
	} 
}
