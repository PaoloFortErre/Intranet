package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCliente;
import com.erretechnology.intranet.repositories.RepositoryEvento;
import com.erretechnology.intranet.repositories.RepositoryNews;

@Controller
@RequestMapping(value = "myWork")
public class MyWorkController extends BaseController {
	@Autowired
	private RepositoryCliente repoCliente;
	
	@Autowired
	private RepositoryNews repoNews;
	
	@Autowired
	private RepositoryEvento repoEvento;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		/*
		 * PARTE CARICAMENTO FORM COMPLEANNI
		 * 
		 * */
		UtenteDatiPersonali u1 = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		mav.addObject("utenteDati", u1);
		mav.setViewName("myWork");
		Calendar calendar = Calendar.getInstance(), calUtente = Calendar.getInstance();
		calendar.setTimeInMillis(Instant.now().getEpochSecond()*1000);
		List<UtenteDatiPersonali> utenti = serviceDatiPersonali.getAll(), utentiCompleanno = new LinkedList<UtenteDatiPersonali>();
		
		for(UtenteDatiPersonali u : utenti) {
			calUtente.setTimeInMillis(u.getDataNascita()*1000);
			
			if((calendar.get(Calendar.MONTH))==(calUtente.get(Calendar.MONTH)) && (calendar.get(Calendar.DAY_OF_MONTH))==(calUtente.get(Calendar.DAY_OF_MONTH)) /*&& u.isVisualizzaDataNascita() == true*/) {
				utentiCompleanno.add(u);
			}
		}
		System.out.println(utentiCompleanno.size());
		mav.addObject("utente", utentiCompleanno);
		/*
		 * PARTE CARICAMENTO FORM NUOVI UTENTI
		 * 
		 * */
		
		List<UtenteDatiPersonali> nuoviUtenti = utenti.stream()
				.sorted(Comparator.comparingInt(UtenteDatiPersonali::getId).reversed())
				.limit(6)
				.collect(Collectors.toList());
		
		mav.addObject("nuoviAssunti", nuoviUtenti.subList(0, 3));
		mav.addObject("nuoviAssunti2", nuoviUtenti.subList(3, 6));
		
		///////////
		/*
		 * PARTE PODCAST
		 * 
		
		 */	
		List<Podcast> listPodcast = servicePodcast.getAll();
		if(listPodcast.size() != 0) {
			Podcast primoPodcast = listPodcast.get(0);
			mav.addObject("primoPodcast", primoPodcast);
			listPodcast.remove(0);
			mav.addObject("altriPodcast", listPodcast);
		}
		/*
		 * 
		 * 
		 * PARTE FORM NUOVI CLIENTI
		 */
		mav.addObject("nuoviClienti", repoCliente.findLimit(3));
		//alternativa con pagination
		/*
		 *  mav.addObject("nuoviClienti1", repoCliente.findPagination(PageRequest.of(0, 3)));
		 *	mav.addObject("nuoviClienti2", repoCliente.findPagination(PageRequest.of(1, 3)));
		 * 
		 */
		
		/*
		 * PARTE NEWS
		 * 
		 */	List<News> news= repoNews.findAllOrderByDataPubblicazioneDesc(); 
			
			mav.addObject("news", news.subList(0, 3));
			mav.addObject("news2", news.subList(3, 6));
		
		
		
		/*
		 * PARTE EVENTI
		 * 
		 */
		
		mav.addObject("eventi", repoEvento.findNextWorkEvents(Instant.now().getEpochSecond()).stream().limit(2).collect(Collectors.toList()));
		return mav;

	}
	
	@GetMapping(value = "/sondaggi")
	public ModelAndView sondaggi() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sondaggi");
		mav.addObject("sondaggi", serviceSondaggio.findAll());
		return mav;		
	}
	
	@PostMapping(value = "/sondaggi")
	public String sondaggio(@ModelAttribute("sondaggio") Sondaggio sondaggio, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		sondaggio.setAutore(autore);
		sondaggio.setTimestamp(Instant.now().getEpochSecond());
		sondaggio.setVisibile(true);
		serviceSondaggio.save(sondaggio);
		saveLog("creato un nuovo sondaggio", autore);
		return "redirect:/myWork/sondaggi";
	}
	
	@PostMapping(value = "/deleteSondaggio")
	public String deleteMessaggio(int id, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if(serviceSondaggio.findByAutore(autore).stream().filter(x-> x.getId() == id).count() > 0) {
			Sondaggio s =serviceSondaggio.findById(id);
			s.setVisibile(false);
			serviceSondaggio.save(s);
			saveLog("cancellato un sondaggio", autore);
			return "redirect:/myWork/sondaggi";
		}return "redirect:forbidden";
		
	}
	
	@GetMapping(value = "/addSondaggio")
	public ModelAndView addSondaggio() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addSondaggio");
		mav.addObject("sondaggio", new Sondaggio());
		return mav;		
	}
}
