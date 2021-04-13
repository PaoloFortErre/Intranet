package com.erretechnology.intranet.services;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryPost;
@Service("servicePost")
public class ServicePostImpl implements ServicePost{

	@Autowired
	private RepositoryPost repositoryPost;
	
/*	@Override
	public List<Post> getLastMessage() {
		return repositoryPost.findAll().stream()
				.filter(x->x.isVisibile())
				.sorted(Comparator.comparingInt(Post::getId).reversed())
				
				.collect(Collectors.toList());
	}
	*/
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
		return repositoryPost.findAll();
	}

	@Override
	public void remove(Post p) {
		repositoryPost.delete(p);
		
	}

	@Override
	public List<Post> getAllNotVisible() {
		return repositoryPost.findByVisibileFalse(Sort.by("timestampEliminazione").descending());
	}
	
	@Override
	public List<Post> getByAutore(UtenteDatiPersonali autore) {
		return repositoryPost.findTop6ByAutoreAndVisibileTrueOrderByIdDesc(autore);

	}

	@Override
	public List<Post> getLastMessage() {
		return repositoryPost.findByVisibileTrue(Sort.by("id").descending());
	}

}
