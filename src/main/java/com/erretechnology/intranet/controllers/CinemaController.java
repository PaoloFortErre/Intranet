package com.erretechnology.intranet.controllers;

import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.erretechnology.intranet.models.CategoriaCinema;
import com.erretechnology.intranet.models.Cinema;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;

@Controller
@RequestMapping("my-life/film-serie")
public class CinemaController extends BaseController {

	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	@GetMapping("/aggiungi")
	public String form(Model model) {
		model.addAttribute("cinema", new Cinema()); 
		model.addAttribute("categorie", repoCategoria.findAll());
		return "cinemaForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("cinema") Cinema cinema, @RequestParam(required=false) MultipartFile immagine,
			ModelMap model, HttpSession session) throws Exception {	
		cinema.setVisibile(true);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		cinema.setAutore(utenteLoggato);
		
		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			cinema.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceCinema.save(cinema);
		saveLog("inserito un cinema", utenteLoggato);
		notificaTutti("ha inserito un nuovo film consigliato dalla redazione!", utenteLoggato, "MyLife");
		return "redirect:/my-life/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, int categoria, @RequestParam(required=false) MultipartFile immagine,
			HttpSession session, Model model) throws Exception {
		Cinema cinema = serviceCinema.findById(id);
		cinema.setTitolo(titolo);
		CategoriaCinema categoriaCinema = repoCategoria.findById(categoria).get();
		cinema.setCategoria(categoriaCinema);
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		
		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			cinema.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceCinema.save(cinema);
		model.addAttribute("cinema", cinema);
		saveLog("modificato le informazioni di un cinema", utenteLoggato);
		return "redirect:/my-life/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Cinema cinema = serviceCinema.findById(id);
		cinema.setVisibile(false);
		cinema.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceCinema.save(cinema);
		saveLog("cancellato un cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/my-life/";
	}

	@RequestMapping("/cancella")
	public String cancella(int id, HttpSession session) {
		serviceCinema.deleteById(id);
		return "redirect:/profilo/mostra-eliminati";
	}

	@RequestMapping("/ripristina")
	public String ripristina(int id, HttpSession session) {
		Cinema cinema = serviceCinema.findById(id);
		cinema.setVisibile(true);
		cinema.setTimestampEliminazione(0);
		serviceCinema.save(cinema);
		return "redirect:/profilo/mostra-eliminati";
	}

}
