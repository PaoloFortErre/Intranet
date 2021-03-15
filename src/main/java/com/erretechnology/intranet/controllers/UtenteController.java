package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
@RequestMapping(value = "utente")
public class UtenteController {

	@Autowired
	ServiceUtenteDatiPersonali serviceUtenteDati;
	@Autowired
	ServiceUtente serviceUtente;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView primaPagina(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("profilo");
		mav.addObject("descrizione", serviceUtenteDati.findById(Integer.parseInt(session.getAttribute("id").toString())).getDescrizione());
		return mav;
	}


	@RequestMapping(value = "/modificaDescrizione", method = RequestMethod.POST)
	public String modificaDescrizione(UtenteDatiPersonali utente, HttpSession session) {
		System.out.println("test");
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceUtenteDati.findById(id_utente);
		utenteLoggato.setDescrizione(utente.getDescrizione());
		serviceUtenteDati.save(utenteLoggato);
		return "redirect:/utente/";
	}

	@RequestMapping(value = "cambioPassword" , method = RequestMethod.POST)
	public ModelAndView cambioPassword() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cambiaPassword");
		mav.addObject("vecchiaPassword", new String());
		mav.addObject("nuovaPassword", new String());
		mav.addObject("cNuovaPassword", new String());
		return mav;
	}

	@RequestMapping(value = "/paginaModificaPassword", method = RequestMethod.POST)
	public String cambiaPaginaModificaPagina(@RequestParam("vecchiaPassword") String vPsw, @RequestParam("nuovaPassword") String nPsw,
			@RequestParam("cNuovaPassword") String cNPsw, HttpSession session) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		if(passwordEncoder.matches(vPsw, serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getPassword())){
			if(nPsw.equals(cNPsw)) {
				Utente u = serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())); 
				u.setPassword(nPsw);
				serviceUtente.saveUtente(u);
			}else {
				return "redirect:/utente/paginaModificaPassword";
			}
			return "redirect:/utente/";
		}else{
			return "redirect:/utente/cambioPassword";
		}
		//System.out.println("ok ci sono");
		//return "cambiaPassword";
	}
}
