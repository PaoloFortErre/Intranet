package com.erretechnology.intranet.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.erretechnology.intranet.models.Utente;


public interface ServiceUtente {
	public void save(Utente u);
	public void saveUtente(Utente u);
	public CompletableFuture<Integer> findNumberOfActiveUsers();
	public List<Utente> getAll();
	public Utente findByEmail(String email);
	public Utente findByResetPasswordToken(String token);
	public Utente findById(int id);
	public void updateResetPasswordToken(String token, String email);
	public boolean foundEmail(String email);
}