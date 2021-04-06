package com.erretechnology.intranet.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.erretechnology.intranet.models.FilePdf;

@Repository(value = "RepositoryFilePdf")
public interface RepositoryFilePdf extends JpaRepository<FilePdf, Integer>{
	@Query
	List<FilePdf> findByVisibileFalse(Sort sort);
}
