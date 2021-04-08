package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;
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

import com.erretechnology.intranet.models.EventoLife;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryEventoLife;

@Controller
@RequestMapping("eventolife")
public class EventoLifeController extends BaseController {
	@Autowired
	private RepositoryEventoLife repoEvento;

	@GetMapping("/new")
	public String formLife(Model model) {
		model.addAttribute("evento", new EventoLife()); 
		return "eventoLifeForm";
	}

	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("evento") EventoLife evento, @RequestParam(required=false) MultipartFile immagine, 
			String date, HttpSession session, ModelMap model) throws Exception {

		Date formettedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
		Timestamp timestamp = new Timestamp(formettedDate.getTime()/1000);  
		evento.setData(timestamp.getTime());

		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
		evento.setAutore(utenteLoggato);
		evento.setVisibile(true);

		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			img.setData(compressImage(immagine, 0.5f));
			img.setAutore(utenteLoggato);
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			evento.setCopertina(img);

		} else {
			evento.setCopertina(serviceFileImmagine.getImmagine(284));
		}

		repoEvento.save(evento);
		saveLog("inserito un nuovo evento", utenteLoggato);
		notificaTutti("ha inserito un nuovo evento su MyLife!", utenteLoggato);
		return "redirect:/myLife1/";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		EventoLife evento = repoEvento.findById(id).get();
		model.addAttribute("evento", evento);
		Timestamp date = new Timestamp(evento.getData()*1000);
		model.addAttribute("date", date);
		return "EventoLifeUpdate";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String titolo, String contenuto, String date, String luogo, String link,
			@RequestParam(required=false) MultipartFile immagine, HttpSession session, Model model) throws Exception {
		EventoLife evento = repoEvento.findById(id).get();
		evento.setTitolo(titolo);
		evento.setLuogo(luogo);

		Date formettedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date); 
		Timestamp timestamp = new Timestamp(formettedDate.getTime()/1000);  
		evento.setData(timestamp.getTime());
		evento.setLink(link);

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
				evento.setCopertina(img);
			}else {
				evento.setCopertina(serviceFileImmagine.getImmagineByData(img.getData()));
			}

		}

		repoEvento.save(evento);
		model.addAttribute("evento", evento);
		saveLog("modificato un evento", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));

		return "redirect:/myLife1/";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		EventoLife evento = repoEvento.findById(id).get();
		evento.setVisibile(false);
		evento.setTimestampEliminazione(Instant.now().getEpochSecond());
		repoEvento.save(evento);
		saveLog("eliminato un evento", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));

		return "redirect:/myLife1/";
	}
	
	
	@GetMapping(value ="/cancellaEvento")
	public String eliminaEvento(HttpSession session, int id) {
		EventoLife e = repoEvento.findById(id).get();
		repoEvento.delete(e);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaEvento")
	public String ripristinaEvento(HttpSession session, int id) {
		EventoLife e = repoEvento.findById(id).get();
		e.setVisibile(true);
		repoEvento.save(e);;
		return "redirect:/profile/mostraEliminati";
	}
}
