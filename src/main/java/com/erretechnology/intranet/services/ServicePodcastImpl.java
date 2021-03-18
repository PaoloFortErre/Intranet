package com.erretechnology.intranet.services;

import java.util.List;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Podcast> getAll() {
		// TODO Auto-generated method stub
		return repositoryPodcast.findAll();
	} 
}
