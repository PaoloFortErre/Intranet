package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryFileImmagine;

@Service("ServiceFileImmagini")
public class ServiceFileImmagineImpl implements ServiceFileImmagini{
	@Autowired
	RepositoryFileImmagine repositoryFileImmagine;

	@Override
	public void insert(FileImmagine file) {
		repositoryFileImmagine.save(file);
	}

	@Override
	public FileImmagine getImmagine(int id) {
		return repositoryFileImmagine.findById(id).get();
	}
	
	@Override
	public FileImmagine getLastImmagineByUtente(UtenteDatiPersonali u) {
		return repositoryFileImmagine.findTopByAutore(u, Sort.by("id").descending());
	}

	@Override
	public List<FileImmagine> getAll() {
		return repositoryFileImmagine.findAll();
	}
	
	@Override
	public boolean contains(String data) {
		return repositoryFileImmagine.findFirstByData(data) != null;
	}

	@Override
	public FileImmagine getImmagineByData(String data) {
		return repositoryFileImmagine.findFirstByData(data);
	}

}
