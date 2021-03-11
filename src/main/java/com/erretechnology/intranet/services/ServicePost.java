package com.erretechnology.intranet.services;

import java.util.List;

import com.erretechnology.intranet.models.Post;

public interface ServicePost {
	
	public List<Post> getLastMessage();

	public void save(Post mex);
	
	public void delete(Post mex);
	
	public Post findById(int id);

}
