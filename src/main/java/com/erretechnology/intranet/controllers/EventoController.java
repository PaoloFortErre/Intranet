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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.Indirizzo;
import com.erretechnology.intranet.repositories.RepositoryEvento;
import com.erretechnology.intranet.repositories.RepositoryIndirizzo;
import com.erretechnology.intranet.repositories.RepositoryUtente;

@Controller
@RequestMapping("evento")
public class EventoController extends BaseController {
	@Autowired
	private RepositoryEvento repoEvento;
	@Autowired
	private RepositoryUtente repoUtente;
	@Autowired
	private RepositoryIndirizzo repoIndirizzo;
	private String imageFolder = "evento";
	
	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {
		
		model.addAttribute("evento", repoEvento.findById(id).get());
		return "evento";
	}
	
	@GetMapping("/list")
	public String getList(Model model) {
		model.addAttribute("eventoList", repoEvento.findAllOrderByIdDesc());
		return "eventoList";
	}
	
	@GetMapping("/next")
	public String getNext(Model model) {
		model.addAttribute("eventoList", repoEvento.findNextEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/recent")
	public String getRecent(Model model) {
		model.addAttribute("eventoList", repoEvento.findRecentEvents(Instant.now().getEpochSecond()));
		return "eventoList";
	}
	
	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("evento", new Evento()); 
		return "eventoForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(
			@ModelAttribute("evento") Evento evento, 
			@RequestParam(required=false) MultipartFile immagine, 
			@RequestParam("date") String date, 
			@RequestParam("via") String via,
			@RequestParam("citta") String citta, 
			@RequestParam("provincia") String provincia, 
			HttpSession session, ModelMap model) throws IOException, ParseException {
		
		Indirizzo indirizzo = new Indirizzo();
		indirizzo.setVia(via);
		indirizzo.setCitta(citta);
		indirizzo.setProvincia(provincia);
		repoIndirizzo.save(indirizzo);
		evento.setIndirizzo(indirizzo);
		
		Date formettedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
		Timestamp timestamp = new Timestamp(formettedDate.getTime()/1000);  
		evento.setData(timestamp.getTime());

		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		evento.setAutore(repoUtente.findById(idUser).get());
		evento.setVisibile(true);
		
		if(!immagine.isEmpty()) {
			try {
				String fileName = serviceFileSystem.saveImage(imageFolder, immagine, idUser);
				evento.setCopertina(fileName);
				System.err.println("File caricato");
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}	
		}
		
		repoEvento.save(evento);
		saveLog("inserito un nuovo post", serviceDatiPersonali.findById(idUser));
        return "evento";
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
		
		if(!immagine.isEmpty()) {
			int idUser = Integer.parseInt(session.getAttribute("id").toString());
			try {
				String fileName = serviceFileSystem.saveImage(imageFolder, immagine, idUser);
				evento.setCopertina(fileName);
				System.err.println("File caricato");
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}	
		}
		
		repoEvento.save(evento);
		model.addAttribute("evento", evento);
		saveLog("modificato un evento", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "evento";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Evento evento = repoEvento.findById(id).get();
		evento.setVisibile(false);
		repoEvento.save(evento);
		saveLog("eliminato un post", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/evento/list";
	}
}
