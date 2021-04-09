package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.FilePdf;
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
	public void remove(FilePdf modulo) {
		repositoryFilePdf.delete(modulo);
	}

	@Override
	public List<FilePdf> getAllNotVisible() {
		// TODO Auto-generated method stub
		return repositoryFilePdf.findByVisibileFalse(Sort.by("id").descending());
	}

}
