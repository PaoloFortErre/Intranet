package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

public interface ServicePost {
	
	public List<Post> getLastMessage();

	public void save(Post mex);
	
	public void delete(Post mex);
	
	public Post findById(int id);
	
	public Page<Post> findPaginated(Pageable pageable) ;
	
	public List<Post> getAll();

	public void remove(Post p);

	public CompletableFuture<List<Post>> getAllNotVisible();
	
	public List<Post> getByAutore(UtenteDatiPersonali autore);

	public List<Post> getAllByAutore(UtenteDatiPersonali autore);

}
