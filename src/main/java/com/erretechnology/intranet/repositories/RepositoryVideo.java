package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erretechnology.intranet.models.VideoDelGiorno;

public interface RepositoryVideo extends JpaRepository<VideoDelGiorno, Integer>{
	@Query
	public VideoDelGiorno findTopByPaginaOrderByIdDesc(String pagina);
}
