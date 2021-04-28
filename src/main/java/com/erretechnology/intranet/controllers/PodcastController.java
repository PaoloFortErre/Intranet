package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;
import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;

/*
 * In questo controller si possono modificare, aggiungere, cancellare i podcast di Rosario
 */

@Controller
@RequestMapping("/my-work/podcast")
public class PodcastController extends BaseController {

	/*
	 * Funzione che reindirizza al form
	 * che permette di inserire un podcast, 
	 * viene passato un podcast vuoto
	 * e la funzione per inserire la data della pubblicazione 
	 */
	@GetMapping(value = "/")
	public ModelAndView uploadMAV(boolean flag, String messaggio, Model model) {
		if(flag) {
			model.addAttribute("errore", messaggio);
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("podcast");
		mav.addObject("podcast", new Podcast());
		mav.addObject("date", new Utility());
		return mav;
	}

	/*
	 * La funzione di caricamento podcast. Si controlla prima di tutto la correttezza dei dati inseriti nel form.
	 * Dopodiché si inserisce il podcast in DB e si indirizza l'utente sulla pagina MYWORK
	 */
	
	@PostMapping(value = "/upload")
	public ModelAndView uploadPodcast(Utility data, @RequestParam("file") MultipartFile file,
			@ModelAttribute("podcast") Podcast podcast, HttpSession session, Model model) throws Exception{
		if (file.isEmpty()) {
			return uploadMAV(true, "Non è stato inserito nessun file audio", model);
		}

		long timestampPodcast = (Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
		if(timestampPodcast > Instant.now().getEpochSecond()) {
			return uploadMAV(true, "La data del podcast non può essere nel futuro", model);
		}
		if(servicePodcast.contains(file.getBytes())) {
			podcast = servicePodcast.getpodcastByData(file.getBytes());
		}else {
			podcast.setTimestamp(Instant.now().getEpochSecond());
			podcast.setPodcast(file.getBytes());
		}
		UtenteDatiPersonali utente = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		podcast.setUtente(utente);
		podcast.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
		podcast.setDataPodcast(timestampPodcast);
		podcast.setVisibile(true);
		servicePodcast.save(podcast);
		saveLog("inserito un nuovo podcast",utente);
		notificaTutti("ha inserito un nuovo podcast di Rosario! Buon ascolto!", utente, "MyWork");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/my-work/");
		return mav;
	}

	/*
	 * Funzione per il soft delete del podcast
	 */
	@GetMapping(value = "/delete/{id}")
	public String deletePodcast(@PathVariable int id) {
		Podcast p = servicePodcast.getById(id);
		p.setTimestampEliminazione(Instant.now().getEpochSecond());
		p.setVisibile(false);
		servicePodcast.save(p);
		saveLog("eliminato il podcast con "  + p.getNome(), p.getUtente());
		return "redirect:/my-work/";
	}
	
	/*
	 * Cancellazione fisica del podcast dal DB, solo l'admin può farlo
	 */
	@GetMapping(value ="/cancella")
	public String eliminaPodcast(HttpSession session, int id) {
		Podcast p = servicePodcast.getById(id);
		servicePodcast.remove(p);
		return "redirect:/profilo/mostra-eliminati";
	}
	/*
	 * Ripristino podcast in soft delete, solo l'admin può farlo
	 */
	@GetMapping(value ="/ripristina")
	public String ripristinaModulo(HttpSession session, int id) {
		Podcast p = servicePodcast.getById(id);
		p.setVisibile(true);
		p.setTimestampEliminazione(0);
		servicePodcast.save(p);;
		return "redirect:/profilo/mostra-eliminati";
	}
}
