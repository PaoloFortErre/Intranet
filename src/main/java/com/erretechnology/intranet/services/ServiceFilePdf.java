package com.erretechnology.intranet.services;

import java.util.List;
import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceFilePdf {
	public void insert(FilePdf file);
	
	public FilePdf findById(int id);
	
	public List<FilePdf> findAll();
	
	public List<FilePdf> findByAutore(UtenteDatiPersonali autore);
}
