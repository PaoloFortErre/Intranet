package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceFileImmagini {
	public void insert(FileImmagine file);

	public FileImmagine getImmagine(int id);

	public List<FileImmagine> getAll();

	public FileImmagine getLastImmagineByUtente(UtenteDatiPersonali u);
	
	public boolean contains(byte[] data);
	
	public FileImmagine getImmagineByData(byte[] data);
}
