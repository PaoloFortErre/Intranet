package com.erretechnology.intranet.controllers;

import java.io.IOException;
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

import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.repositories.RepositoryNews;
import com.erretechnology.intranet.repositories.RepositoryUtente;
import com.erretechnology.intranet.services.ServiceFileSystem;

@Controller
@RequestMapping("news")
public class NewsController {
	@Autowired
	private RepositoryNews repoNews;
	@Autowired
	private RepositoryUtente repoUtente;
	@Autowired
	private ServiceFileSystem fileSystemService;
	private String imageFolder = "news";
	
	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {
		
		model.addAttribute("news", repoNews.findById(id).get());
		return "news";
	}
	
	@GetMapping("/list")
	public String getList(Model model) {
		model.addAttribute("newsList", repoNews.findByOrderByDataPubblicazioneDesc());
		return "newsList";
	}
	
	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("news", new News()); 
		return "newsForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("news") News news, @RequestParam(required=false) MultipartFile immagine, HttpSession session, ModelMap model) throws IOException {
		
		Long date = Instant.now().getEpochSecond();
		news.setDataPubblicazione(date);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		news.setAutore(repoUtente.findById(idUser).get());
		
		if(!immagine.isEmpty()) {
			try {
				String fileName = fileSystemService.saveImage(imageFolder, immagine, idUser);
				news.setCopertina(fileName);
				System.err.println("File caricato");
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}	
		}
		
		repoNews.save(news);
        return "news";
	}
	
	@PostMapping(value = "/update/{id}")
	public String update(@PathVariable int id, @RequestParam("titolo") String titolo, @RequestParam("contenuto") String contenuto) {
		News news = repoNews.findById(id).get();
		news.setTitolo(titolo);
		news.setContenuto(contenuto);
		return "news";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		repoNews.deleteById(id);
		return "newsForm";
	}
}
