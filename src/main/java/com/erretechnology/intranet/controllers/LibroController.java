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
import com.erretechnology.intranet.models.Libro;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryLibro;

@Controller
@RequestMapping("libro")
public class LibroController extends BaseController {
	@Autowired
	private RepositoryLibro repoLibro;	

	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("libro", new Libro()); 
		return "libroForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("libro") Libro libro, @RequestParam(required=false) MultipartFile immagine, ModelMap model, 
			HttpSession session) throws Exception {	
		libro.setVisibile(true);
		UtenteDatiPersonali utenteLoggato= (UtenteDatiPersonali) session.getAttribute("utenteSessione");
		libro.setAutore(utenteLoggato);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(compressImage(immagine, 0.5f));
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			libro.setCopertina(img);

		}

		repoLibro.save(libro);
		saveLog("inserito un libro", utenteLoggato);
		notificaTutti("ha inserito un nuovo libro consigliato dalla redazione!", utenteLoggato, "MyLife");
		return "redirect:/myLife1/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Libro libro = repoLibro.findById(id).get();
		model.addAttribute("libro", libro);
		return "libroFormUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String scrittore ,@RequestParam(required=false) MultipartFile immagine, String link,
			HttpSession session, Model model) throws Exception{
		Libro libro = repoLibro.findById(id).get();
		libro.setTitolo(titolo);
		libro.setScrittore(scrittore);
		libro.setLink(link);
		UtenteDatiPersonali utenteLoggato = (UtenteDatiPersonali) session.getAttribute("utenteSessione");

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			img.setData(compressImage(immagine, 0.5f));
			if(!serviceFileImmagine.contains(img.getData())) {
				
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				libro.setCopertina(img);
			}else {
				libro.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}

		}

		repoLibro.save(libro);
		model.addAttribute("libro", libro);
		saveLog("modificato le informazioni di un libro", utenteLoggato);
		return "redirect:/myLife1/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Libro libro = repoLibro.findById(id).get();
		libro.setVisibile(false);
		libro.setTimestampEliminazione(Instant.now().getEpochSecond());
		repoLibro.save(libro);
		saveLog("cancellato un libro", (UtenteDatiPersonali) session.getAttribute("utenteSessione"));
		return "redirect:/myLife1/";
	}
	
	@RequestMapping("/cancellaLibro")
	public String cancellaLibro(int id) {
		repoLibro.deleteById(id);
		return "redirect:/profile/mostraEliminati";
	}
	
	@RequestMapping("/ripristinaLibro")
	public String ripristinaLibro(int id) {
		Libro l = repoLibro.findById(id).get();
		l.setVisibile(true);
		repoLibro.save(l);
		return "redirect:/profile/mostraEliminati";
	}
	
	
}
