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
import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryNews;

@Controller
@RequestMapping("news")
public class NewsController extends BaseController {
	@Autowired
	private RepositoryNews repoNews;
	
	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("news", new News()); 
		return "newsForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("news") News news, @RequestParam(required=false) MultipartFile immagine, 
			HttpSession session, ModelMap model) throws IOException {
		
		Long date = Instant.now().getEpochSecond();
		news.setDataPubblicazione(date);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		news.setAutore(utenteLoggato);
		news.setVisibile(true);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(compressImage(immagine, 0.5f));
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			news.setCopertina(img);
		} else {
			news.setCopertina(serviceFileImmagine.getImmagine(334));
		}

		repoNews.save(news);
		saveLog("aggiunto una news", utenteLoggato);
		notificaTutti("ha inserito una news su MyWork!", utenteLoggato, "MyWork");
		return "redirect:/myWork/";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		News news = repoNews.findById(id).get();
		model.addAttribute("news", news);
		return "newsFormUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String contenuto,
			@RequestParam(required=false) MultipartFile immagine, HttpSession session, Model model) throws Exception {
		News news = repoNews.findById(id).get();
		news.setTitolo(titolo);
		news.setContenuto(contenuto);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			img.setData(compressImage(immagine, 0.5f));
			if(!serviceFileImmagine.contains(img.getData())) {
				int idUser = Integer.parseInt(session.getAttribute("id").toString());
				UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				news.setCopertina(img);
			}else {
				news.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}
		}

		repoNews.save(news);
		model.addAttribute("news", news);
		saveLog("modificato una news", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myWork/";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		News news = repoNews.findById(id).get();
		news.setVisibile(false);
		news.setTimestampEliminazione(Instant.now().getEpochSecond());
		repoNews.save(news);
		saveLog("eliminato una news", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myWork/";
	}
	
	@GetMapping(value ="/cancellaNews")
	public String eliminaNews(HttpSession session, int id) {
		News n = repoNews.findById(id).get();
		repoNews.delete(n);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaNews")
	public String ripristinaNews(HttpSession session, int id) {
		News n = repoNews.findById(id).get();
		n.setVisibile(true);
		repoNews.save(n);;
		return "redirect:/profile/mostraEliminati";
	}
	
}
