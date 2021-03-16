package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.FilePdf;

public interface ServiceFilePdf {
	public FilePdf insert(FilePdf file);
	
	public FilePdf get(int id);
	
	public List<FilePdf> getAll();
}
