package com.erretechnology.intranet.services;

import com.erretechnology.intranet.models.PostLinkedin;

public interface ServiceLinkedin {
	
	public void save(PostLinkedin linkedin);
	public void deleteById(int id);
}
