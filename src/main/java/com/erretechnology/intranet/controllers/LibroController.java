package com.erretechnology.intranet.controllers;

import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.erretechnology.intranet.models.Libro;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping("/my-life/libri")
public class LibroController extends BaseController {

	@GetMapping("/aggiungi")
	public String form(Model model) {
		model.addAttribute("libro", new Libro()); 
		return "libroForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("libro") Libro libro, @RequestParam(required=false) MultipartFile immagine, ModelMap model, 
			HttpSession session) throws Exception {	
		libro.setVisibile(true);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		libro.setAutore(utenteLoggato);

		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			libro.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceLibro.save(libro);
		saveLog("inserito un libro", utenteLoggato);
		notificaTutti("ha inserito un nuovo libro consigliato dalla redazione!", utenteLoggato, "MyLife");
		return "redirect:/my-life/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String scrittore ,@RequestParam(required=false) MultipartFile immagine,
			HttpSession session, Model model) throws Exception{
		Libro libro = serviceLibro.findById(id);
		libro.setTitolo(titolo);
		libro.setScrittore(scrittore);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			libro.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceLibro.save(libro);
		model.addAttribute("libro", libro);
		saveLog("modificato le informazioni di un libro", utenteLoggato);
		return "redirect:/my-life/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Libro libro = serviceLibro.findById(id);
		libro.setVisibile(false);
		libro.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceLibro.save(libro);
		saveLog("cancellato un libro", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/my-life/";
	}
	
	@RequestMapping("/cancella")
	public String cancellaLibro(HttpSession session, int id) {
		serviceLibro.deleteById(id);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@RequestMapping("/ripristina")
	public String ripristinaLibro(HttpSession session, int id) {
		Libro l = serviceLibro.findById(id);
		l.setVisibile(true);
		l.setTimestampEliminazione(0);
		serviceLibro.save(l);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	
}
