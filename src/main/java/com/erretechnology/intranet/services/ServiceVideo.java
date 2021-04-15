package com.erretechnology.intranet.services;

import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.VideoDelGiorno;

public interface ServiceVideo {

	public CompletableFuture<VideoDelGiorno> getLastVideo(String string);
	public void save(VideoDelGiorno video);

}
