package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Cinema;

public interface ServiceCinema {
	
	public void save(Cinema cinema);
	
	public void deleteById(int id);
	
	public Cinema findById(int id);
	
	public List<Cinema> getAllNotVisible();
}
