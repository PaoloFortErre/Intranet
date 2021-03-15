package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
public class HomeController {

	@Autowired
	private ServiceUtente serviceUtente;

	@Autowired
	private ServiceUtenteDatiPersonali serviceDatiPersonali;

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

	@GetMapping("/forbidden")
	public String admin() {
		return ("forbidden");
	}

	// Login form
	@GetMapping(value = "/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		return mav;
	}
	
	// form Registrazione
	@GetMapping(value = "/registra")
	public ModelAndView registrazione() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("signInPage");
		mav.addObject("user", new UtenteDatiPersonali());
		mav.addObject("email", new String());
		mav.addObject("password", new String());
		mav.addObject("date", new Utility());
		return mav;
	}
	
	@PostMapping(value = "/eseguiRegistrazione")
	public String addUtente(@ModelAttribute("user")UtenteDatiPersonali utenteDP,
			@RequestParam("email") String email, @RequestParam("password") String password,
			Utility data) {
		
		utenteDP.setDataNascita(Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
		serviceDatiPersonali.insert(password, email, utenteDP);
		
		return "info";
	}
	
	// Password Dimenticata da login
	@GetMapping(value = "/password_dimenticata")
	public ModelAndView passwordLost() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("password_lost");
		return mav;
	}
	//Permission manager
	@GetMapping(value = "/permission_manager")
	public ModelAndView permissionManager() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("permissionManager");
		return mav;
	}

	// Login form
	@GetMapping(value = "/homepage")
	public ModelAndView homepageAdmin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("homepage");
		return mav;
	}

	@PostMapping(value = "/setSession")
	public String setSession(HttpSession session) {
		final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		session.setAttribute("id", serviceUtente.findByEmail(currentUserName).getId());
		System.out.println(session.getAttribute("id"));
		return "redirect:/homepage";
	}

	// Login form with error
	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "loginPage";
	}

}
