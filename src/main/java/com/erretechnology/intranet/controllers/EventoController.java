package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Indirizzo;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryEvento;
import com.erretechnology.intranet.repositories.RepositoryIndirizzo;

@Controller
@RequestMapping("evento")
public class EventoController extends BaseController {
	@Autowired
	private RepositoryEvento repoEvento;
	@Autowired
	private RepositoryIndirizzo repoIndirizzo;
	
	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {
		
		model.addAttribute("evento", repoEvento.findById(id).get());
		return "evento";
	}
	
	@GetMapping("/lifeList")
	public String getLifeList(Model model) {
		model.addAttribute("eventoList", repoEvento.findLifeOrderByIdDesc());
		return "eventoList";
	}
	
	@GetMapping("/lifeNext")
	public String getLifeNext(Model model) {
		model.addAttribute("eventoList", repoEvento.findNextLifeEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/lifeRecent")
	public String getLifeRecent(Model model) {
		model.addAttribute("eventoList", repoEvento.findRecentLifeEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/workList")
	public String getWorkList(Model model) {
		model.addAttribute("eventoList", repoEvento.findWorkOrderByIdDesc());
		return "eventoList";
	}
	
	@GetMapping("/workNext")
	public String getWorkNext(Model model) {
		model.addAttribute("eventoList", repoEvento.findNextWorkEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/workRecent")
	public String getWorkRecent(Model model) {
		model.addAttribute("eventoList", repoEvento.findRecentWorkEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/newLife")
	public String formLife(Model model) {
		model.addAttribute("evento", new Evento()); 
		return "eventoLifeForm";
	}
	
	@GetMapping("/newWork")
	public String formWork(Model model) {
		model.addAttribute("evento", new Evento()); 
		return "eventoWorkForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(
			@ModelAttribute("evento") Evento evento, @RequestParam(required=false) MultipartFile immagine, 
			String date, String via, String citta, String provincia, String isLife, 
			HttpSession session, ModelMap model) throws IOException, ParseException {
		
		if(!via.isEmpty() || !citta.isEmpty() || !provincia.isEmpty()) {
			Indirizzo indirizzo = new Indirizzo();
			indirizzo.setVia(via);
			indirizzo.setCitta(citta);
			indirizzo.setProvincia(provincia);
			repoIndirizzo.save(indirizzo);
			evento.setIndirizzo(indirizzo);
		}
		
		Date formettedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
		Timestamp timestamp = new Timestamp(formettedDate.getTime()/1000);  
		evento.setData(timestamp.getTime());

		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		evento.setAutore(utenteLoggato);
		evento.setVisibile(true);
		
		if(!immagine.getOriginalFilename().isEmpty()) {
			try {
				FileImmagine img = new FileImmagine();
				img.setData(immagine.getBytes());
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				evento.setCopertina(img);
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}
		} else {
			evento.setCopertina(serviceFileImmagine.getImmagine(284));
		}
		
		if(isLife.equals("true")) {
			evento.setLife(true);
		} else {
			evento.setLife(false);
		}
		
		repoEvento.save(evento);
		saveLog("inserito un nuovo post", serviceDatiPersonali.findById(idUser));
		
		if(isLife.equals("true")) {
			return "redirect:/myLife/";
		} else {
			return "redirect:/myWork/";
		}
        
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Evento evento = repoEvento.findById(id).get();
		model.addAttribute("evento", evento);
		Timestamp date = new Timestamp(evento.getData()*1000);
		model.addAttribute("date", date);
		Indirizzo indirizzo = repoIndirizzo.findById(evento.getIndirizzo().getId()).get();
		model.addAttribute("indirizzo", indirizzo);
		return "EventoFormUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String contenuto, String date, String via, String citta, String provincia, @RequestParam(required=false) MultipartFile immagine, HttpSession session, Model model) throws ParseException {
		Evento evento = repoEvento.findById(id).get();
		evento.setTitolo(titolo);
		evento.setContenuto(contenuto);

		Indirizzo indirizzo = repoIndirizzo.findById(evento.getIndirizzo().getId()).get();
		indirizzo.setVia(via);
		indirizzo.setCitta(citta);
		indirizzo.setProvincia(provincia);
		repoIndirizzo.save(indirizzo);
		
		Date formettedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
		Timestamp timestamp = new Timestamp(formettedDate.getTime()/1000);  
		evento.setData(timestamp.getTime());
		
		if(!immagine.getOriginalFilename().isEmpty()) {
			try {
				FileImmagine img = new FileImmagine();			
				img.setData(immagine.getBytes());
				if(!serviceFileImmagine.contains(img.getData())) {
					int idUser = Integer.parseInt(session.getAttribute("id").toString());
					UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
					img.setAutore(utenteLoggato);
					img.setTimestamp(Instant.now().getEpochSecond());
					img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
					serviceFileImmagine.insert(img);
					evento.setCopertina(img);
				}else {
					evento.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
				}
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}
		}
		
		repoEvento.save(evento);
		model.addAttribute("evento", evento);
		saveLog("modificato un evento", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		if(!evento.isLife())
			return "redirect:/myWork/";
		return "redirect:/myLife/";

	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Evento evento = repoEvento.findById(id).get();
		evento.setVisibile(false);
		repoEvento.save(evento);
		saveLog("eliminato un post", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		
		if(evento.isLife()) {
			return "redirect:/myLife/";
		} else {
			return "redirect:/myWork/";
		}
        
		
	}
}
