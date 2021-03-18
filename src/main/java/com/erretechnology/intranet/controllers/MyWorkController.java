package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryCliente;
import com.erretechnology.intranet.repositories.RepositoryNews;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
@RequestMapping(value = "myWork")
public class MyWorkController extends BaseController {
	@Autowired
	private RepositoryCliente repoCliente;
	
	@Autowired
	private RepositoryNews repoNews;

	@GetMapping(value = "/")
	public ModelAndView primaPagina() {
		ModelAndView mav = new ModelAndView();
		/*
		 * PARTE CARICAMENTO FORM COMPLEANNI
		 * 
		 * */
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
		Podcast primoPodcast = listPodcast.get(0);
		mav.addObject("primoPodcast", primoPodcast);
		listPodcast.remove(0);
		mav.addObject("altriPodcast", listPodcast);

		/*
		 * 
		 * 
		 * PARTE FORM NUOVI CLIENTI
		 */
		mav.addObject("nuoviClienti", repoCliente.findLimit(3));
		
		/*
		 * PARTE NEWS
		 * 
		 */
		mav.addObject("news", repoNews.findAllOrderByDataPubblicazioneDesc());
		return mav;

	}
}
