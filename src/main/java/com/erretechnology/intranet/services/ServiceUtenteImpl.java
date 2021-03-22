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
		return getAll().stream().filter(x -> x.getEmail().equals(email)).findFirst().get();
	}
	@Override
	public Utente findByResetPasswordToken(String token) {
		//List<Utente> a = getAll().stream().filter(x-> x.getTokenResetPassword() != null).filter(x -> x.getTokenResetPassword().equals(token)).collect(Collectors.toList());
		return getAll().stream().filter(x-> x.getTokenResetPassword() != null).filter(x -> x.getTokenResetPassword().equals(token)).findFirst().get();
	}
	@Override
	public void updateResetPasswordToken(String token, String email) /*throws CustomerNotFoundException*/ {
		Utente utente = findByEmail(email);
		if (utente != null) {
			utente.setTokenResetPassword(token);
			userRepository.save(utente);
		}
	}
	
	@Override
	public Utente findById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	public boolean foundEmail(String email) {
		return getAll().stream().filter(x-> x.getEmail().equals(email)).count() == 1;
	}

}
