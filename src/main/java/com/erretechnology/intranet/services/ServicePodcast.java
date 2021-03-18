package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Podcast;

public interface ServicePodcast{

	public void save(Podcast podcast);
	public List<Podcast> getAll();
}
