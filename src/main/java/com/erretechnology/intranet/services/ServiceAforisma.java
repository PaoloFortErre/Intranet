package com.erretechnology.intranet.services;

import com.erretechnology.intranet.models.Aforisma;

public interface ServiceAforisma {
	
	public Aforisma findById(int id);
	public void save(Aforisma aforisma);
}
