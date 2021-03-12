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
import com.erretechnology.intranet.services.ServiceAuthority;
import com.erretechnology.intranet.services.ServicePermesso;
import com.erretechnology.intranet.services.ServiceUtente;

@Controller
public class HomeController {

	@Autowired
	private ServiceUtente serviceUtente;
	@Autowired
	private ServicePermesso servicePermesso;
	@Autowired
	private ServiceAuthority authorityService;

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
	@RequestMapping(value = "/homepageAdmin", method = RequestMethod.GET)
	public ModelAndView homepageAdmin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("homepage_admin");
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
