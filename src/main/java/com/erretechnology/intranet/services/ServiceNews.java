package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.News;

public interface ServiceNews {
	
	public void save(News news);
	
	public void deleteById(int id);
	
	public News findById(int id);
	
	public List<News> getAllNotVisible();
}
