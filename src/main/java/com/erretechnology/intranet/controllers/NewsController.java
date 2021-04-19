package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpSession;

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

@Controller
@RequestMapping("news")
public class NewsController extends BaseController {
	
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
			if(compressImage(immagine, 0.5f).length == 0)
				img.setData(immagine.getBytes());
			else
				img.setData(compressImage(immagine, 0.5f));			
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			news.setCopertina(img);
		} else {
			news.setCopertina(serviceFileImmagine.getImmagine(334));
		}

		serviceNews.save(news);
		saveLog("aggiunto una news", utenteLoggato);
		notificaTutti("ha inserito una news su MyWork!", utenteLoggato, "MyWork");
		return "redirect:/myWork/";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		News news = serviceNews.findById(id);
		model.addAttribute("news", news);
		return "newsFormUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String contenuto,
			@RequestParam(required=false) MultipartFile immagine, HttpSession session, Model model) throws Exception {
		News news = serviceNews.findById(id);
		news.setTitolo(titolo);
		news.setContenuto(contenuto);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();			
			if(compressImage(immagine, 0.5f).length == 0)
				img.setData(immagine.getBytes());
			else
				img.setData(compressImage(immagine, 0.5f));	
			if(!serviceFileImmagine.contains(img.getData())) {
				
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				news.setCopertina(img);
			}else {
				news.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}
		}

		serviceNews.save(news);
		model.addAttribute("news", news);
		saveLog("modificato una news", utenteLoggato);
		return "redirect:/myWork/";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		News news = serviceNews.findById(id);
		news.setVisibile(false);
		news.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceNews.save(news);
		saveLog("eliminato una news", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/myWork/";
	}
	
	@GetMapping(value ="/cancellaNews")
	public String eliminaNews(HttpSession session, int id) {
		serviceNews.deleteById(id);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaNews")
	public String ripristinaNews(HttpSession session, int id) {
		News n = serviceNews.findById(id);
		n.setVisibile(true);
		n.setTimestampEliminazione(0);
		serviceNews.save(n);;
		return "redirect:/profile/mostraEliminati";
	}
	
}
