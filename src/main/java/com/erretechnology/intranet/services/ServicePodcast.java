package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Podcast;

public interface ServicePodcast{

	public void save(Podcast podcast);
	public CompletableFuture<List<Podcast>> getAll();
	public Podcast getById(int id);
	public boolean contains(byte[] data);
	public Podcast getpodcastByData(byte[] data);
	public CompletableFuture<List<Podcast>> getAllNotVisible();
	public void remove(Podcast p);
}
