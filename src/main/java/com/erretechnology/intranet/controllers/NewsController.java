package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.repositories.RepositoryNews;
import com.erretechnology.intranet.repositories.RepositoryUtente;

@Controller
@RequestMapping("news")
public class NewsController {
	@Autowired
	private RepositoryNews repoNews;
	@Autowired
	private RepositoryUtente repoUtente;
	
	@RequestMapping("/{id}")
	public String get(@PathVariable int id, Model model) {
		
		model.addAttribute("news", repoNews.findById(id).get());
		return "news";
	}
	
	@RequestMapping("/list")
	public String getList(Model model) {
		model.addAttribute("newsList", repoNews.findByOrderByDataPubblicazioneDesc());
		return "newsList";
	}
	
	@RequestMapping("/new")
	public String form(Model model) {
		model.addAttribute("news", new News()); 
		return "newsForm";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String post(@ModelAttribute("news") News news, @RequestParam("immagine") MultipartFile image, HttpSession session) throws IOException {
		
		news.setDataPubblicazione(Instant.now().getEpochSecond());
		int id = Integer.parseInt(session.getAttribute("id").toString());
		news.setAutore(repoUtente.findById(id).get());
		
		if(!image.isEmpty()) {
			System.out.println();
			String fileName = StringUtils.cleanPath(image.getOriginalFilename());
			news.setCopertina(fileName);
			//immagineController.saveImage(image);
		}
		
		repoNews.save(news);
		return "news";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, @RequestParam("titolo") String titolo, @RequestParam("contenuto") String contenuto) {
		News news = repoNews.findById(id).get();
		news.setTitolo(titolo);
		news.setContenuto(contenuto);
		return "news";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		repoNews.deleteById(id);
		return "newsForm";
	}
}
