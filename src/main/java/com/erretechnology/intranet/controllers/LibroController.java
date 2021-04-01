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

	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {

		model.addAttribute("libro", repoLibro.findById(id).get());
		return "libro";
	}	

	@GetMapping("/new")
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

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(immagine.getBytes());
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			libro.setCopertina(img);

		}

		repoLibro.save(libro);
		saveLog("inserito un libro", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Libro libro = repoLibro.findById(id).get();
		model.addAttribute("libro", libro);
		return "libroFormUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String scrittore ,@RequestParam(required=false) MultipartFile immagine, 
			HttpSession session, Model model) throws Exception{
		Libro libro = repoLibro.findById(id).get();
		libro.setTitolo(titolo);
		libro.setScrittore(scrittore);

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
				libro.setCopertina(img);
			}else {
				libro.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}

		}

		repoLibro.save(libro);
		model.addAttribute("libro", libro);
		saveLog("modificato le informazioni di un libro", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Libro libro = repoLibro.findById(id).get();
		libro.setVisibile(false);
		libro.setTimestampEliminazione(Instant.now().getEpochSecond());
		repoLibro.save(libro);
		saveLog("cancellato un libro", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myLife/";
	}
	
	@RequestMapping("/cancellaLibro")
	public String cancellaLibro(HttpSession session, int id) {
		repoLibro.deleteById(id);
		return "redirect:/profile/mostraEliminati";
	}
	
	@RequestMapping("/ripristinaLibro")
	public String ripristinaLibro(HttpSession session, int id) {
		Libro l = repoLibro.findById(id).get();
		l.setVisibile(true);
		repoLibro.save(l);
		return "redirect:/profile/mostraEliminati";
	}
	
	
}
