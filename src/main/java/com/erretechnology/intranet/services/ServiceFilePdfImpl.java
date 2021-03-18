package com.erretechnology.intranet.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryFilePdf;
@Service("ServiceFilePdf")
public class ServiceFilePdfImpl implements ServiceFilePdf {
	@Autowired
	RepositoryFilePdf repositoryFilePdf;

	@Override
	public void insert(FilePdf file) {
		repositoryFilePdf.save(file);
	}

	@Override
	public FilePdf findById(int id) {
		return repositoryFilePdf.findById(id).get();
	}

	@Override
	public List<FilePdf> findAll() {
		return repositoryFilePdf.findAll();
	}

	@Override
	public List<FilePdf> findByAutore(UtenteDatiPersonali autore) {
		return findAll().stream().filter(x -> x.getAutore().equals(autore)).collect(Collectors.toList());
	}

}
