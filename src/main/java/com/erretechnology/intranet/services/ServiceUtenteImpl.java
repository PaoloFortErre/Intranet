package com.erretechnology.intranet.services;

import java.time.Instant;
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
		userRepository.saveAndFlush(u);
	}
	
	@Override
	public void save(Utente u) {
		userRepository.saveAndFlush(u);
	}

	@Override
	public List<Utente> getAll() {
		return userRepository.findAll();
	}
	@Override
	public Utente findByEmail(String email) {
	//	return getAll().stream().filter(x -> x.getEmail().equals(email)).findFirst().get();
		return userRepository.findByEmail(email);
	}
	@Override
	public Utente findByResetPasswordToken(String token) {
		return userRepository.findByTokenResetPassword(token);
	/*	return getAll().stream().filter(x-> x.getTokenResetPassword() != null)
			  .filter(x -> x.getTokenResetPassword().equals(token)).findFirst().orElse(null);
	*/}
	@Override
	public void updateResetPasswordToken(String token, String email) /*throws CustomerNotFoundException*/ {
		Utente utente = findByEmail(email);
		if (utente != null) {
			utente.setTokenResetPassword(token);
			utente.setTimestampToken(Instant.now().getEpochSecond());
			userRepository.save(utente);
		}
	}
	
	@Override
	public Utente findById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	public boolean foundEmail(String email) {
		if(findByEmail(email)!= null) return true;
		return false;
	//	return getAll().stream().filter(x-> x.getEmail().equals(email)).count() == 1;
	}

}
