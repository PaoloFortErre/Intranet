package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public ModelAndView uploadMAV() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("podcast");
		mav.addObject("podcast", new Podcast());
		mav.addObject("date", new Utility());
		return mav;
	}
	
	@PostMapping(value = "/uploadPodcast")
	public String uploadPodcast(Utility data, @RequestParam("file") MultipartFile file,
			@ModelAttribute("podcast") Podcast podcast, HttpSession session) {
		if (file.isEmpty()) {
			System.out.println("file vuoto");
			return "redirect:/podcast/";
		}

		try {
			podcast.setTimestamp(Instant.now().getEpochSecond());
			podcast.setPodcast(file.getBytes());
			podcast.setUtente(serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
			podcast.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
			podcast.setDataPodcast(Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
			servicePodcast.save(podcast);
			saveLog("inserito un nuovo podcast",serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return "redirect:/myWork/";
	}
}
