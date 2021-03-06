package com.erretechnology.intranet.controllers;

import java.io.IOException;
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
import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping("my-work/news")
public class NewsController extends BaseController {
	
	@GetMapping("/aggiungi")
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
		
		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) 
			news.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceNews.save(news);
		saveLog("aggiunto una news", utenteLoggato);
		notificaTutti("ha inserito una news su MyWork!", utenteLoggato, "MyWork");
		return "redirect:/my-work/";
	}
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String contenuto,
			@RequestParam(required=false) MultipartFile immagine, HttpSession session, Model model) throws Exception {
		News news = serviceNews.findById(id);
		news.setTitolo(titolo);
		news.setContenuto(contenuto);
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);

		//controllo se immagine è già presente nel database
		if(!immagine.getOriginalFilename().isEmpty()) 
			news.setCopertina(salvaImmagine(immagine, utenteLoggato));

		serviceNews.save(news);
		model.addAttribute("news", news);
		saveLog("modificato una news", utenteLoggato);
		return "redirect:/my-work/";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		News news = serviceNews.findById(id);
		news.setVisibile(false);
		news.setTimestampEliminazione(Instant.now().getEpochSecond());
		serviceNews.save(news);
		saveLog("eliminato una news", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/my-work/";
	}
	
	@GetMapping(value ="/cancella")
	public String eliminaNews(HttpSession session, int id) {
		serviceNews.deleteById(id);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@GetMapping(value ="/ripristina")
	public String ripristinaNews(HttpSession session, int id) {
		News n = serviceNews.findById(id);
		n.setVisibile(true);
		n.setTimestampEliminazione(0);
		serviceNews.save(n);;
		return "redirect:/profilo/mostra-eliminati";
	}
	
}
