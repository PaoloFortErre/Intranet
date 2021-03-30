package com.erretechnology.intranet.controllers;

import java.io.IOException;
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

	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {

		model.addAttribute("cinema", repoCinema.findById(id).get());
		return "cinema";
	}	

	@GetMapping("/list")
	public String getList(Model model) {
		model.addAttribute("cinemaList", repoCinema.findAll());
		return "cinemaList";
	}

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
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(immagine.getBytes());
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			cinema.setCopertina(img);

		}

		repoCinema.save(cinema);
		saveLog("inserito un cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Cinema cinema = repoCinema.findById(id).get();
		model.addAttribute("cinema", cinema);
		model.addAttribute("categorie", repoCategoria.findAll());
		return "cinemaFormUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, int categoria, @RequestParam(required=false) MultipartFile immagine, 
			HttpSession session, Model model) throws Exception {
		Cinema cinema = repoCinema.findById(id).get();
		cinema.setTitolo(titolo);
		CategoriaCinema categoriaCinema = repoCategoria.findById(categoria).get();
		cinema.setCategoria(categoriaCinema);
		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			img.setData(immagine.getBytes());
			if(!serviceFileImmagine.contains(img.getData())) {
				int idUser = Integer.parseInt(session.getAttribute("id").toString());
				UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
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
		saveLog("modificato le informazioni di un cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Cinema cinema = repoCinema.findById(id).get();
		cinema.setVisibile(false);
		repoCinema.save(cinema);
		saveLog("cancellato un cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}
}
