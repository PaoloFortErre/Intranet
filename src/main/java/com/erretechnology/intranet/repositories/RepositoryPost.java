package com.erretechnology.intranet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erretechnology.intranet.models.Post;

@Repository(value = "RepositoryPost")

public interface RepositoryPost extends JpaRepository<Post, Integer>{

}
