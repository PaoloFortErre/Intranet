package com.erretechnology.intranet.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.repositories.RepositoryPost;
@Service("servicePost")
public class ServicePostImpl implements ServicePost{

	@Autowired
	private RepositoryPost repositoryPost;
	
	@Override
	public List<Post> getLastMessage() {
		return repositoryPost.findAll().stream()
				.filter(x->x.isVisibile())
				.sorted(Comparator.comparingInt(Post::getId).reversed())
				.collect(Collectors.toList());
	}

	@Override
	public void save(Post mex) {
		repositoryPost.save(mex);
		
	}

	@Override
	public void delete(Post mex) {
		repositoryPost.delete(mex);
		
	}

	@Override
	public Post findById(int id) {
		return repositoryPost.findById(id).get();
	}
}
