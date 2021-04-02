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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Cliente;
import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryCliente;

@Controller
@RequestMapping(value = "myWork")
public class MyWorkController extends BaseController {
	@Autowired
	private RepositoryCliente repoCliente;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<ElementiMyWork> elementi = serviceElementiMyWork.findAll();
		elementi.stream().filter(x -> x.getFoto() != null).forEach(x -> x.setImmagine(serviceFileImmagine.getImmagine(x.getFoto())));
		UtenteDatiPersonali u1 = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		mav.addObject("utenteDati", u1);
		mav.setViewName("myWork");
		Calendar calendar = Calendar.getInstance(), calUtente = Calendar.getInstance();
		calendar.setTimeInMillis(Instant.now().getEpochSecond()*1000);
		List<UtenteDatiPersonali> utenti = serviceDatiPersonali.getAll();
		List<UtenteDatiPersonali> utentiCompleanno = new LinkedList<UtenteDatiPersonali>();

		for(UtenteDatiPersonali u : utenti) {
			calUtente.setTimeInMillis(u.getDataNascita()*1000);

			if((calendar.get(Calendar.MONTH))==(calUtente.get(Calendar.MONTH)) && (calendar.get(Calendar.DAY_OF_MONTH))==(calUtente.get(Calendar.DAY_OF_MONTH)) && u.isVisualizzaDataNascita() == true) {
				utentiCompleanno.add(u);
			}
		}
		mav.addObject("utente", utentiCompleanno);

		List<UtenteDatiPersonali> nuoviUtenti = utenti.stream()
				.sorted(Comparator.comparingInt(UtenteDatiPersonali::getId).reversed())
				.filter(x->x.getUtente().getAttivo()==true)
				.limit(1)
				.collect(Collectors.toList());
	//	setMAV(mav, nuoviUtenti, 0, 6, "nuoviAssunti");
		mav.addObject("nuoveAssunzioni", nuoviUtenti);
		
		/*List<Podcast> listPodcast = elementi.stream().map(x -> x.getTipo().equals("aphorism"));
		if(listPodcast.size() != 0 && listPodcast!= null) {
			Podcast primoPodcast = listPodcast.get(listPodcast.size()-1);
			mav.addObject("primoPodcast", primoPodcast);
			listPodcast.remove(listPodcast.size()-1);
			mav.addObject("altriPodcast", listPodcast.stream().limit(3).sorted(Comparator.comparingInt(Podcast::getId).reversed()).collect(Collectors.toList()));
		}*/
		
		List<Cliente> clienti = repoCliente.findLimit(1);
		if(clienti.size() > 0) {
			mav.addObject("nuoviClienti", clienti);
		} else {
			mav.addObject("nuoviClienti", null);
		}

		List<ElementiMyWork> news= elementi.stream().filter(x -> x.getTipo().equals("news")).collect(Collectors.toList());
		mav.addObject("newsSlide", news);

		
		List<ElementiMyWork> sondaggi = elementi.stream().filter(x -> x.getTipo().equals("sondaggi")).collect(Collectors.toList());
		/*setMAV(mav, sondaggi , 0, 6, "sondaggi");*/
		mav.addObject("sonadggi", sondaggi);
		
		List<ComunicazioneHR> comunicazione = serviceComunicazioni.getAll();
		if(comunicazione.size()==0) {
			mav.addObject("comunicazioni", null);		
		}
		else {
			mav.addObject("comunicazioni", comunicazione.get(comunicazione.size()-1));
		}
		List<ElementiMyWork> aforismi = elementi.stream().filter(x -> x.getTipo().equals("aphorism")).collect(Collectors.toList());
		mav.addObject("aforisma", aforismi);
		
		List<ElementiMyWork> eventi = elementi.stream().filter(x -> x.getTipo().equals("event")).collect(Collectors.toList());
		mav.addObject("eventi", eventi);
		

		mav.addObject("video", serviceVideo.getLastVideo("MyWork"));
		
		return mav;
		
		
		
	}
	
	@GetMapping(value = "/addVideo")
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
		return "redirect:/myWork/";
	}
	
	@PostMapping(value = "/sondaggi")
	public String sondaggio(@ModelAttribute("sondaggio") Sondaggio sondaggio, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		sondaggio.setAutore(autore);
		sondaggio.setTimestamp(Instant.now().getEpochSecond());
		sondaggio.setVisibile(true);
		serviceSondaggio.save(sondaggio);
		saveLog("creato un nuovo sondaggio", autore);
		return "redirect:/myWork/";
	}

	
	@GetMapping(value = "/modificaSondaggio/{id}")
	public String modificaSondaggio(@PathVariable int id, Model model) {
		//controllare permesso GS
		model.addAttribute("sondaggio", serviceSondaggio.findById(id));
		return "sondaggiFormUpdate";
	}
	@PostMapping(value = "/sondaggiFormUpdate/{id}")
	public String paginaModificaSondaggio(@PathVariable int id,String nomeSondaggio, String link, HttpSession session, Model model) {
		//controllare permesso GS
		Sondaggio sondaggio = serviceSondaggio.findById(id);
		sondaggio.setLink(link);
		if(nomeSondaggio!=null)
		sondaggio.setNomeSondaggio(nomeSondaggio);
		serviceSondaggio.save(sondaggio);
		model.addAttribute("sondaggio", serviceSondaggio.findById(id));

		saveLog("modificato un sondaggio", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
	
		return "redirect:/myWork/";
	}
	@PostMapping(value = "/deleteSondaggio/{id}")
	public String deleteMessaggio(@PathVariable int id, HttpSession session) {
		//controllare permesso GS
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
			Sondaggio s =serviceSondaggio.findById(id);
			s.setVisibile(false);
			s.setTimestampEliminazione(Instant.now().getEpochSecond());
			serviceSondaggio.save(s);
			saveLog("cancellato un sondaggio", autore);
			return "redirect:/myWork/";
	}

	@GetMapping(value = "/addSondaggio")
	public ModelAndView addSondaggio() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addSondaggio");
		mav.addObject("sondaggio", new Sondaggio());
		return mav;		
	}
	
	@GetMapping(value ="/cancellaSondaggio")
	public String eliminaSondaggio(HttpSession session, int id) {
		Sondaggio s = serviceSondaggio.findById(id);
		serviceSondaggio.delete(s);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaSondaggio")
	public String ripristinaNews(HttpSession session, int id) {
		Sondaggio s = serviceSondaggio.findById(id);
		s.setVisibile(true);
		serviceSondaggio.save(s);
		return "redirect:/profile/mostraEliminati";
	}
}
