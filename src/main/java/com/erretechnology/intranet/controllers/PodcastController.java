package com.erretechnology.intranet.controllers;

import java.io.IOException;
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
import com.erretechnology.intranet.models.Utility;

@Controller
@RequestMapping("podcast")
public class PodcastController extends BaseController {

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

	@PostMapping(value = "/uploadPodcast")
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
		podcast.setUtente(serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		podcast.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
		podcast.setDataPodcast(timestampPodcast);
		podcast.setVisibile(true);
		servicePodcast.save(podcast);
		saveLog("inserito un nuovo podcast",serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		notificaTutti("È stato inserito un nuovo podcast di Rosario! Buon ascolto!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/myWork/");
		return mav;
	}

	@GetMapping(value = "/deletePodcast/{id}")
	public String deletePodcast(@PathVariable int id) {
		Podcast p = servicePodcast.getById(id);
		p.setTimestampEliminazione(Instant.now().getEpochSecond());
		p.setVisibile(false);
		servicePodcast.save(p);
		saveLog("eliminato il podcast con "  + p.getNome(), p.getUtente());
		return "redirect:/myWork/";
	}

	@GetMapping(value = "/podcastFormUpdate/{id}")
	public ModelAndView updatePodcast(@PathVariable int id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("podcastFormUpdate");
		mav.addObject("podcast", servicePodcast.getById(id));
		mav.addObject("date", new Utility());
		return mav;
	}

	@GetMapping(value = "/updatePodcast/{id}")
	public String updatePodcast(@ModelAttribute("date") Utility data, @RequestParam(name = "file" , required=false) MultipartFile file,
			@ModelAttribute("podcast") Podcast podcast, @PathVariable int id) {
		Podcast oldPodcast = servicePodcast.getById(id);
		try {
			oldPodcast.setTimestamp(Instant.now().getEpochSecond());
			if(file != null)
				oldPodcast.setPodcast(file.getBytes());
			if(podcast.getNome() != null)
				oldPodcast.setNome(podcast.getNome());
			if(data.getDate() != null)
				oldPodcast.setDataPodcast(Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
			servicePodcast.save(oldPodcast);
			saveLog("modificato un podcast",oldPodcast.getUtente());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/myWork/";
	}
	
	@GetMapping(value ="/cancellaPodcast")
	public String eliminaPodcast(HttpSession session, int id) {
		Podcast p = servicePodcast.getById(id);
		servicePodcast.remove(p);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaPodcast")
	public String ripristinaModulo(HttpSession session, int id) {
		Podcast p = servicePodcast.getById(id);
		p.setVisibile(true);
		servicePodcast.save(p);;
		return "redirect:/profile/mostraEliminati";
	}
}
