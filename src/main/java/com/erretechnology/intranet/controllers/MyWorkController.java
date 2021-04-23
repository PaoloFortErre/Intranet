package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.VideoDelGiorno;

@Controller
@RequestMapping(value = "my-work")
public class MyWorkController extends BaseController {

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) throws InterruptedException, ExecutionException {
		ModelAndView mav = new ModelAndView();
		List<ElementiMyWork> elementi = serviceElementiMyWork.findAll();
		CompletableFuture<List<Podcast>> listPodcast = servicePodcast.getAll();
		CompletableFuture<List<UtenteDatiPersonali>> utenti = serviceDatiPersonali.getAll();
		CompletableFuture<VideoDelGiorno> video = serviceVideo.getLastVideo("MyWork");
		CompletableFuture<List<ElementiMyWork>> clienti = findWorkElement(elementi, "cliente");
		CompletableFuture<List<ElementiMyWork>> news = findWorkElement(elementi, "news");
		CompletableFuture<List<ElementiMyWork>> sondaggi = findWorkElement(elementi, "sondaggi");
		CompletableFuture<List<ElementiMyWork>> aforismi = findWorkElement(elementi, "aphorism");
		CompletableFuture<List<ElementiMyWork>> eventi = findWorkElement(elementi, "event");
		UtenteDatiPersonali u1 = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		mav.addObject("utenteDati", u1);
		mav.setViewName("myWork");
		Calendar calendar = Calendar.getInstance(), calUtente = Calendar.getInstance();
		calendar.setTimeInMillis(Instant.now().getEpochSecond()*1000);
		List<UtenteDatiPersonali> utentiCompleanno = new LinkedList<UtenteDatiPersonali>();
		
		CompletableFuture.allOf(listPodcast, utenti, video, clienti, news, sondaggi, aforismi, eventi).join();
		
		mav.addObject("podcast", listPodcast.get());
		
		mav.addObject("nuoviClienti", clienti.get());
		mav.addObject("newsSlide", news.get());
		mav.addObject("sonadggi", sondaggi.get());
		mav.addObject("aforisma", aforismi.get());
		mav.addObject("eventi", eventi.get());
		
		
		mav.addObject("video", video.get());

		//mav.addObject("video", serviceVideo.getLastVideo("MyWork"));
		for(UtenteDatiPersonali u : utenti.get()) {
			calUtente.setTimeInMillis(u.getDataNascita()*1000);

			if((calendar.get(Calendar.MONTH))==(calUtente.get(Calendar.MONTH)) && (calendar.get(Calendar.DAY_OF_MONTH))==(calUtente.get(Calendar.DAY_OF_MONTH)) && u.isVisualizzaDataNascita() == true) {
				utentiCompleanno.add(u);
			}
		}
		mav.addObject("utente", utentiCompleanno);

		List<UtenteDatiPersonali> nuoviUtenti = utenti.get().stream()
				.sorted(Comparator.comparingInt(UtenteDatiPersonali::getId).reversed())
				.filter(x->x.getUtente().getAttivo()==true)
				.limit(3)
				.collect(Collectors.toList());
	//	setMAV(mav, nuoviUtenti, 0, 6, "nuoviAssunti");
		mav.addObject("nuoveAssunzioni", nuoviUtenti);
		return mav;
		
		
		
	}
	
	@GetMapping(value = "/aggiungi-video")
	public ModelAndView setVideo(HttpSession session, String link) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addVideo");
		mav.addObject("video", new VideoDelGiorno());
		return mav;
	}
	
	@PostMapping(value = "/video")
	public String video(@ModelAttribute("video") VideoDelGiorno video, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		video.setPagina("MyWork");
		serviceVideo.save(video);
		saveLog("aggiornato il video su myWork", autore);
		notificaTutti("ha inserito un nuovo video su MyWork!", autore, "MyWork");
		return "redirect:/my-work/";
	}
	
	@PostMapping(value = "/sondaggi")
	public String sondaggio(@ModelAttribute("sondaggio") Sondaggio sondaggio, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		sondaggio.setAutore(autore);
		sondaggio.setTimestamp(Instant.now().getEpochSecond());
		sondaggio.setVisibile(true);
		serviceSondaggio.save(sondaggio);
		saveLog("creato un nuovo sondaggio", autore);
		notificaTutti("ha inserito un nuovo sondaggio!", autore, "HR");
		return "redirect:/my-work/comunicazioni/";
	}

	
	@PostMapping(value = "/sondaggi/modifica/{id}")
	public String modificaSondaggio(@PathVariable int id,@ModelAttribute("sondaggio") Sondaggio sondaggio, HttpSession session) {
		//controllare permesso GS
		Sondaggio sondaggioOld = serviceSondaggio.findById(id);
		sondaggioOld.setNomeSondaggio(sondaggio.getNomeSondaggio());
		sondaggioOld.setLink(sondaggio.getLink());
		serviceSondaggio.save(sondaggioOld);
		saveLog("modificato un sondaggio", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/my-work/comunicazioni/";
	}
	
	@GetMapping (value= "/comunicazioni")
	public ModelAndView comunicazioniHr(HttpSession session) throws InterruptedException, ExecutionException {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		List<ElementiMyWork> elementi = serviceElementiMyWork.findAll();
		List<ElementiMyWork> sondaggi = elementi.stream().filter(x -> x.getTipo().equals("sondaggio")).limit(7).collect(Collectors.toList());
		
		mav.addObject("sondaggi", sondaggi);
		
		mav.addObject("utenteDati", u);
		mav.addObject("comunicazioni", serviceComunicazioni.findFirst10ByVisibileTrueOrderByIdDesc());
		mav.setViewName("comunicazioniHr");
		return mav;
	}
	
	@PostMapping(value = "/sondaggi/delete/{id}")
	public String deleteMessaggio(@PathVariable int id, HttpSession session) {
		//controllare permesso GS
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
			Sondaggio s =serviceSondaggio.findById(id);
			s.setVisibile(false);
			s.setTimestampEliminazione(Instant.now().getEpochSecond());
			serviceSondaggio.save(s);
			saveLog("cancellato un sondaggio", autore);
			return "redirect:/my-work/comunicazioni/";
	}

	@GetMapping(value = "/sondaggi/aggiungi")
	public ModelAndView addSondaggio() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addSondaggio");
		mav.addObject("sondaggio", new Sondaggio());
		return mav;		
	}
	
	@GetMapping(value ="/sondaggi/cancella")
	public String eliminaSondaggio(HttpSession session, int id) {
		Sondaggio s = serviceSondaggio.findById(id);
		serviceSondaggio.delete(s);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@GetMapping(value ="/sondaggi/ripristina")
	public String ripristinaNews(HttpSession session, int id) {
		Sondaggio s = serviceSondaggio.findById(id);
		s.setVisibile(true);
		s.setTimestampEliminazione(0);
		serviceSondaggio.save(s);
		return "redirect:/profilo/mostra-eliminati";
	}
}
