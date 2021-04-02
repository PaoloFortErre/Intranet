package com.erretechnology.intranet.services;

import com.erretechnology.intranet.models.VideoDelGiorno;

public interface ServiceVideo {

	public VideoDelGiorno getLastVideo(String string);
	public void save(VideoDelGiorno video);

}
