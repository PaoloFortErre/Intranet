package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.services.ServicePermesso;
import com.erretechnology.intranet.services.ServiceUtente;

@Controller
public class HomeController {

	@Autowired
	private ServiceUtente serviceUtente;
	@Autowired
	private ServicePermesso servicePermesso;
	
	@GetMapping("/")
	public String home() {
		return ("info");
	}

	@GetMapping("/user")
	public String user() {
		return ("info");
	}

	@GetMapping("/admin")
	public String admin() {
		return ("info");
	}

	// Login form
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		return mav;
	}
	
	@RequestMapping(value = "/setSession", method = {RequestMethod.POST})
	public String setSession(HttpSession session) {
		final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		session.setAttribute("id", serviceUtente.findByEmail(currentUserName).getId());
		System.out.println(session.getAttribute("id"));
		return "redirect:/";
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "loginPage";
	}
	
	@RequestMapping("/aggiungi")
	public String aggiungi() {
		Utente utente = new Utente();
		utente.setAttivo(true);
		utente.setCognome("Agostini");
		utente.setNome("Marco");
		utente.setEmail("marco.agostini@errepro.tech");
		utente.setPassword("asd");
		Permesso permesso = new Permesso();
		permesso.setNome("Admin");
		utente.addPermesso(permesso);
		permesso.addUtente(utente);
		serviceUtente.saveUtente(utente);
		servicePermesso.savePermesso(permesso);
		Utente utente2 = new Utente();
		utente2.setAttivo(true);
		utente2.setCognome("Paoletti");
		utente2.setNome("Mario");
		utente2.setEmail("mario.paoletti@errepro.tech");
		utente2.setPassword("asd");
		Permesso permesso2 = new Permesso();
		permesso2.setNome("User");
		utente2.addPermesso(permesso2);
		permesso2.addUtente(utente2);
		serviceUtente.saveUtente(utente2);
		servicePermesso.savePermesso(permesso2);
		
		
		return "loginPage";
	}
}
