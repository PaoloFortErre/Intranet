package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		if(session.getAttribute("id").toString() == null) {
			return new ModelAndView("bacheca");
		}
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
	
	@RequestMapping(value = "/paginaModificaPassword", method = RequestMethod.POST)
	public String cambiaPaginaModificaPagina() {
		System.out.println("ok ci sono");
		return "cambiaPassword";
	}
}
