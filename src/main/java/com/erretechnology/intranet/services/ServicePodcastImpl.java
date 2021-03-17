package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.repositories.RepositoryPodcast;

@Service
public class ServicePodcastImpl implements ServicePodcast{

	@Autowired
	private RepositoryPodcast repositoryPodcast; 
}
