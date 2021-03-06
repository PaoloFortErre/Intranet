package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.FilePdf;

public interface ServiceFilePdf {
	public void insert(FilePdf file);
	
	public FilePdf findById(int id);
	
	public List<FilePdf> findAll();

	public void remove(FilePdf modulo);
	public List<FilePdf> getAllVisible();
	public CompletableFuture<List<FilePdf>> getAllNotVisible();
}
