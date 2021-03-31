package com.erretechnology.intranet.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.FileImmagine;
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
	public List<Podcast> getAllNotVisible() {
		return repositoryPodcast.findAll().stream().filter(x -> x.getVisibile()==false).collect(Collectors.toList());
	}

	@Override
	public Podcast getById(int id) {
		return repositoryPodcast.findById(id).get();
	}

	@Override
	public boolean contains(byte[] data) {
		return getAll().stream().filter(x->Arrays.equals(x.getPodcast(), (data))).count() > 0;
	}

	@Override
	public Podcast getpodcastByData(byte[] data) {
		return getAll().stream().filter(x->Arrays.equals(x.getPodcast(), (data))).findFirst().get();
	}
}
