package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.erretechnology.intranet.models.Post;

public interface ServicePost {
	
	public List<Post> getLastMessage();

	public void save(Post mex);
	
	public void delete(Post mex);
	
	public Post findById(int id);
	
	public Page<Post> findPaginated(Pageable pageable) ;
	
	public List<Post> getAll();

}
