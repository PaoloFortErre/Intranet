package com.erretechnology.intranet.controllers;

import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.CategoriaCinema;
import com.erretechnology.intranet.models.Cinema;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;
import com.erretechnology.intranet.repositories.RepositoryCinema;

@Controller
@RequestMapping("cinema")
public class CinemaController extends BaseController {
	@Autowired
	private RepositoryCinema repoCinema;
	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("cinema", new Cinema()); 
		model.addAttribute("categorie", repoCategoria.findAll());
		return "cinemaForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("cinema") Cinema cinema, @RequestParam(required=false) MultipartFile immagine,
			ModelMap model, HttpSession session) throws Exception {	
		cinema.setVisibile(true);
		UtenteDatiPersonali utenteLoggato= (UtenteDatiPersonali) session.getAttribute("utenteSessione");
		cinema.setAutore(utenteLoggato);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(compressImage(immagine, 0.5f));
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			cinema.setCopertina(img);

		}

		repoCinema.save(cinema);
		saveLog("inserito un nuova serie/film", utenteLoggato);
		notificaTutti("ha inserito un nuovo film consigliato dalla redazione!", utenteLoggato, "MyLife");
		return "redirect:/myLife1/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Cinema cinema = repoCinema.findById(id).get();
		model.addAttribute("cinema", cinema);
		model.addAttribute("categorie", repoCategoria.findAll());
		return "cinemaFormUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, int categoria, @RequestParam(required=false) MultipartFile immagine, String link,
			HttpSession session, Model model) throws Exception {
		Cinema cinema = repoCinema.findById(id).get();
		cinema.setTitolo(titolo);
		CategoriaCinema categoriaCinema = repoCategoria.findById(categoria).get();
		cinema.setCategoria(categoriaCinema);
		cinema.setLink(link);
		UtenteDatiPersonali utenteLoggato= (UtenteDatiPersonali) session.getAttribute("utenteSessione");
		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			img.setData(immagine.getBytes());
			if(!serviceFileImmagine.contains(img.getData())) {
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				cinema.setCopertina(img);
			}else {
				cinema.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}

		}

		repoCinema.save(cinema);
		model.addAttribute("cinema", cinema);
		saveLog("modificato le informazioni di un cinema", utenteLoggato);
		return "redirect:/myLife1/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Cinema cinema = repoCinema.findById(id).get();
		cinema.setVisibile(false);
		cinema.setTimestampEliminazione(Instant.now().getEpochSecond());
		repoCinema.save(cinema);
		saveLog("cancellato un cinema", (UtenteDatiPersonali) session.getAttribute("utenteSessione"));
		return "redirect:/myLife1/";
	}
	
	@RequestMapping("/cancellaFilm")
	public String cancellaFilm(int id, HttpSession session) {
		repoCinema.deleteById(id);
		return "redirect:/profile/mostraEliminati";
		}
	
	@RequestMapping("/ripristinaFilm")
	public String ripristinaFilm(int id, HttpSession session) {
		Cinema cinema = repoCinema.findById(id).get();
		cinema.setVisibile(true);
		repoCinema.save(cinema);
		return "redirect:/profile/mostraEliminati";
		}
	
}
