package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.FileImmagini;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryFileImmagine;

@Service("ServiceFileImmagini")
public class ServiceFileImmagineImpl implements ServiceFileImmagini{
	@Autowired
	RepositoryFileImmagine repositoryFileImmagine;

	@Override
	public void insert(FileImmagini file) {
		repositoryFileImmagine.save(file);
	}

	@Override
	public FileImmagini getImmagine(int id) {
		return repositoryFileImmagine.findById(id).get();
	}
	
	@Override
	public FileImmagini getLastImmagineByUtente(UtenteDatiPersonali u) {
		return getAll().stream().filter(x-> x.getAutore().equals(u))
				.sorted(Comparator.comparingLong(FileImmagini::getTimestamp).reversed()).findFirst().get();
	}

	@Override
	public List<FileImmagini> getAll() {
		return repositoryFileImmagine.findAll();
	}

}
