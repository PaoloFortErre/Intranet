package com.erretechnology.intranet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryVideo;

@Service("serviceVideo")
public class ServiceVideoImpl implements ServiceVideo{

	@Autowired
	RepositoryVideo repositoryVideo;

	@Override
	public VideoDelGiorno getLastVideo(String nomePagina) {
		// TODO Auto-generated method stub
		return repositoryVideo.findTopByPaginaOrderByIdDesc(nomePagina);
	}

	@Override
	public void save(VideoDelGiorno video) {
		repositoryVideo.save(video);
	}
}
