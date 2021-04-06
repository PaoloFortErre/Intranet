package com.erretechnology.intranet.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
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
	

    public Page<Post> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> list;

        if (getLastMessage().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, getLastMessage().size());
            list = getLastMessage().subList(startItem, toIndex);
        }

        Page<Post> PostPage = new PageImpl<Post>(list, PageRequest.of(currentPage, pageSize), getLastMessage().size());

        return PostPage;

    }

	@Override
	public List<Post> getAll() {
		// TODO Auto-generated method stub
		return repositoryPost.findAll();
	}

	@Override
	public void remove(Post p) {
		repositoryPost.delete(p);
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Post> getAllNotVisible() {
		// TODO Auto-generated method stub
		return getAll().stream().filter(x->x.isVisibile() == false).collect(Collectors.toList());
	}
	
	@Override
	public List<Post> getByAutore(UtenteDatiPersonali autore) {
		return repositoryPost.findAll().stream()
				.filter(x->x.getAutore().equals(autore) && x.isVisibile())
				.sorted(Comparator.comparingInt(Post::getId).reversed())
				.limit(5)
				.collect(Collectors.toList());
	}

}
