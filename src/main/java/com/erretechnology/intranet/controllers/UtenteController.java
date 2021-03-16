package com.erretechnology.intranet.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceFileSystem;
import com.erretechnology.intranet.services.ServiceUtente;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
@RequestMapping(value = "profile")
public class UtenteController {

	@Autowired
	ServiceUtenteDatiPersonali serviceUtenteDati;
	@Autowired
	ServiceUtente serviceUtente;
	@Autowired
	ServiceFileSystem serviceFileSystem;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("profilo");
		mav.addObject("utente", serviceUtenteDati.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return mav;
	}


	@PostMapping(value = "/modificaDescrizione")
	public String modificaDescrizione(@ModelAttribute("utente")UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceUtenteDati.findById(id_utente);
		utenteLoggato.setDescrizione(utente.getDescrizione());
		serviceUtenteDati.save(utenteLoggato);
		return "redirect:/profile/";
	}
	
	@PostMapping(value = "/eliminaFotoProfilo")
	public String eliminaFotoProfilo(@ModelAttribute("utente")UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceUtenteDati.findById(id_utente);
		utenteLoggato.setImmagine("blank_profile.png");
		serviceUtenteDati.save(utenteLoggato);
		return "redirect:/profile/";
	}

	@PostMapping(value = "cambioPassword")
	public ModelAndView cambioPassword() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cambiaPassword");
		mav.addObject("vecchiaPassword", new String());
		mav.addObject("nuovaPassword", new String());
		mav.addObject("cNuovaPassword", new String());
		return mav;
	}

	@PostMapping(value = "/paginaModificaPassword")
	public String cambiaPaginaModificaPagina(@RequestParam("vecchiaPassword") String vPsw, @RequestParam("nuovaPassword") String nPsw,
			@RequestParam("cNuovaPassword") String cNPsw, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if(passwordEncoder.matches(vPsw, serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getPassword())){
			if(nPsw.equals(cNPsw)) {
				Utente u = serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString()));
				u.setPassword(nPsw);
				serviceUtente.saveUtente(u);
			}else {
				return "redirect:/profile/paginaModificaPassword";
			}
			return "redirect:/profile/";
		}else{
			return "redirect:/profile/cambioPassword";
		}
		//System.out.println("ok ci sono");
		//return "cambiaPassword";
	}


	@PostMapping(value = "/cambiaFotoProfilo")
	public String modificaFoto(@RequestParam(required=false) MultipartFile immagine, HttpSession session) {
		String imageFolder = "fotoProfilo";
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		if(!immagine.isEmpty()) {
			try {
				UtenteDatiPersonali utenteLoggato= serviceUtenteDati.findById(idUser);
				String fileName = serviceFileSystem.saveImage(imageFolder, immagine, idUser);
				utenteLoggato.setImmagine(fileName);
				serviceUtenteDati.save(utenteLoggato);
				System.err.println("File caricato");
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}
		}
		return "redirect:/profile/";
	}
	
	
	@PostMapping(value = "/setVisualizzazione")
	public String modificaVisualizazzioneDataNascita(HttpSession session, HttpServletRequest request) {
		String value = request.getParameter("set");
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceUtenteDati.findById(id_utente);
		if(value.equals("Si")) {
			utenteLoggato.setVisualizzaDataNascita(true);
		} else utenteLoggato.setVisualizzaDataNascita(false);
		serviceUtenteDati.save(utenteLoggato);
		return "redirect:/profile/";
	}
}
