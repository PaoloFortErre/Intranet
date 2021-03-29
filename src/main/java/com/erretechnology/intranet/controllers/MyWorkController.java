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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Cliente;
import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.MyWorkBean;
import com.erretechnology.intranet.models.News;
import com.erretechnology.intranet.models.NewsModificato;
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
		//System.out.println(utentiCompleanno.size());
		mav.addObject("utente", utentiCompleanno);

		List<UtenteDatiPersonali> nuoviUtenti = utenti.stream()
				.sorted(Comparator.comparingInt(UtenteDatiPersonali::getId).reversed())
				.filter(x->x.getUtente().getAttivo()==true)
				.limit(6)
				.collect(Collectors.toList());
		//System.out.println(nuoviUtenti.size());
		setMAV(mav, nuoviUtenti, 0, 6, "nuoviAssunti");

		
		List<Podcast> listPodcast = servicePodcast.getAll();
		if(listPodcast.size() != 0) {
			Podcast primoPodcast = listPodcast.get(listPodcast.size()-1);
			mav.addObject("primoPodcast", primoPodcast);
			listPodcast.remove(listPodcast.size()-1);
			System.out.println(listPodcast.stream().limit(3).collect(Collectors.toList()).size() + " numero podcast");
			mav.addObject("altriPodcast", listPodcast.stream().limit(3).collect(Collectors.toList()));
		}
		
		List<Cliente> clienti = repoCliente.findLimit(3);
		if(clienti.size() > 0) {
			mav.addObject("nuoviClienti", clienti);
		} else {
			mav.addObject("nuoviClienti", null);
		}

		List<News> news= repoNews.findAllOrderByDataPubblicazioneDesc(); 
		//System.out.println(news.size());

		setMAV(mav, news, 0, 6, "news");

		if (news.size()==0) {
			mav.addObject( "newsSlide", null);
			mav.addObject( "newsSlide2", null);
		}else if(news.size()>=2) {
			mav.addObject( "newsSlide", news.get(0));
			mav.addObject( "newsSlide2", news.get(1));
		}else if (news.size()<2) {
			mav.addObject( "newsSlide", news.get(0));
			mav.addObject( "newsSlide2", null);
		}

		
		List<Sondaggio> sondaggi = serviceSondaggio.findAll();
		System.out.println(sondaggi.size());
		if (sondaggi.size()==0) {
			mav.addObject("sondaggi",null);
			mav.addObject("sondaggi1",null);

			
		}
		else if (sondaggi.size()>0 && sondaggi.size()<=3) {
			mav.addObject("sondaggi", sondaggi.subList(0, sondaggi.size()));
			mav.addObject("sondaggi1",null);
		}
		else if (sondaggi.size()>3 && sondaggi.size()<6){
			mav.addObject("sondaggi", sondaggi.subList(0, 3));
			mav.addObject("sondaggi1", sondaggi.subList(3, sondaggi.size()));
		}
		else {
			mav.addObject("sondaggi", sondaggi.subList(0, 3));
			mav.addObject("sondaggi1", sondaggi.subList(3, 6));
		}
		
		List<ComunicazioneHR> comunicazione = serviceComunicazioni.getAll();
		if(comunicazione.size()==0) {
			mav.addObject("comunicazioni", null);		
		}
		else {
			mav.addObject("comunicazioni", comunicazione.get(0));
		}
		
		List<Evento> eventi = (List<Evento>) repoEvento.findNextWorkEvents(Instant.now().getEpochSecond());
		//System.out.println(eventi.size());
		setMAV(mav, eventi, 0, 4, "eventi");
		return mav;
		
	}

	private void setMAV(ModelAndView mav, List<? extends MyWorkBean> list, int inizio, int fine, String nomeOggetto) {
		if (list.size()==0) {
			mav.addObject(nomeOggetto, null);
			mav.addObject(nomeOggetto + "2", null);
		}else if(list.size() >= fine) {
			mav.addObject(nomeOggetto, list.subList(inizio, ((fine + inizio) / 2)));
			mav.addObject(nomeOggetto + "2", list.subList(((fine + inizio) / 2), fine));
		}else if(list.size() <= ((fine + inizio) / 2)) {
			mav.addObject(nomeOggetto, list.subList(inizio, list.size()));
			mav.addObject(nomeOggetto + "2", null);
		}else if(list.size() > ((fine + inizio) / 2) && list.size() < fine) {
			mav.addObject(nomeOggetto, list.subList(inizio, ((fine + inizio) / 2)));
			mav.addObject(nomeOggetto + "2", list.subList(((fine + inizio) / 2), list.size()));
		}
	}

//	@GetMapping(value = "/sondaggi")
//	public ModelAndView sondaggi() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("sondaggi");
//		mav.addObject("sondaggi", serviceSondaggio.findAll());
//		return mav;		
//	}

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
		model.addAttribute("sondaggio", serviceSondaggio.findById(id));
		return "sondaggioFormUpdate";
	}
	@PostMapping(value = "/sondaggioFormUpdate/{id}")
	public String paginaModificaSondaggio(@PathVariable int id, @RequestParam("titolo") String titolo, @RequestParam("link") String link, HttpSession session, Model model) {
		Sondaggio sondaggio = serviceSondaggio.findById(id);
		sondaggio.setLink(link);
		if(titolo!=null)
		sondaggio.setNomeSondaggio(titolo);
		serviceSondaggio.save(sondaggio);
		model.addAttribute("sondaggio", serviceSondaggio.findById(id));

		saveLog("modificato un sondaggio", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
	
		return "redirect:/myWork/";
	}
	@PostMapping(value = "/deleteSondaggio")
	public String deleteMessaggio(int id, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if(serviceSondaggio.findByAutore(autore).stream().filter(x-> x.getId() == id).count() > 0) {
			Sondaggio s =serviceSondaggio.findById(id);
			s.setVisibile(false);
			serviceSondaggio.save(s);
			saveLog("cancellato un sondaggio", autore);
			return "redirect:/myWork/";
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
