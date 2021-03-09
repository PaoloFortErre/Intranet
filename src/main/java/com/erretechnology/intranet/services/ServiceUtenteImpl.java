package com.erretechnology.intranet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.repositories.RepositoryUtente;

@Service("userService")
public class ServiceUtenteImpl implements ServiceUtente{
	
	@Autowired
	private RepositoryUtente userRepository;

	@Override
	public void saveUtente(Utente u) {
		u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()) );
		userRepository.save(u);
	}

	@Override
	public List<Utente> getAll() {
		return userRepository.findAll();
	}
	
	
	
	

}
