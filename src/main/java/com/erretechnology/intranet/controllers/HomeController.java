package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;
import com.erretechnology.intranet.services.ServiceAuthority;
import com.erretechnology.intranet.services.ServicePermesso;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
public class HomeController {

	@Autowired
	private ServiceUtente serviceUtente;
	@Autowired
	private ServicePermesso servicePermesso;
	@Autowired
	private ServiceUtenteDatiPersonali serviceDatiPersonali;
	@Autowired
	private ServiceAuthority authorityService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String home() {
		/*
		authorityService.getAll().forEach(x->{
			System.out.println(x.getEmail() + " " + x.getDescrizione());
		});
		authorityService.findByUsertId(2).forEach(x->{
			System.out.println(x.getEmail() + " " + x.getDescrizione());
		});
		*/
		System.out.println();
		return ("info");
	}

	@GetMapping("/user")
	public String user() {
		return ("info");
	}

	@GetMapping("/forbidden")
	public String admin() {
		return ("forbidden");
	}

	// Login form
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		return mav;
	}
	
	// form Registrazione
	@RequestMapping(value = "/registra", method = RequestMethod.GET)
	public ModelAndView registrazione() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("signInPage");
		mav.addObject("user", new UtenteDatiPersonali());
		mav.addObject("email", new String());
		mav.addObject("password", new String());
		mav.addObject("date", new Utility());
		return mav;
	}
	
	@RequestMapping(value = "/eseguiRegistrazione", method = RequestMethod.POST)
	public String addUtente(@ModelAttribute("user")UtenteDatiPersonali utenteDP,
			@RequestParam("email") String email, @RequestParam("password") String password,
			Utility data) {
		Utente utente = new Utente();
		utente.setEmail(email);
		utente.setPassword(password);
		utente.setAttivo(true);
		utenteDP.setPasswordCambiata(false);
		serviceUtente.saveUtente(utente);
		utenteDP.setDataNascita(Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
		utenteDP.setUtente(utente);
		serviceDatiPersonali.save(utenteDP);
		return "info";
	}
	
	// Password Dimenticata da login
	@RequestMapping(value = "/password_dimenticata", method = RequestMethod.GET)
	public ModelAndView passwordLost() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("password_lost");
		return mav;
	}
	//Permission manager
	@RequestMapping(value = "/permission_manager", method = RequestMethod.GET)
	public ModelAndView permissionManager() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("permissionManager");
		return mav;
	}

	// Login form
	@RequestMapping(value = "/homepage", method = RequestMethod.GET)
	public ModelAndView homepageAdmin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("homepage");
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


}
