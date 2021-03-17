package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.FileImmagini;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServiceFileImmagini {
	public void insert(FileImmagini file);

	public FileImmagini getImmagine(int id);

	public List<FileImmagini> getAll();

	public FileImmagini getLastImmagineByUtente(UtenteDatiPersonali u);
	
	public boolean contains(byte[] data);
	
	public FileImmagini getImmagineByData(byte[] data);
}
