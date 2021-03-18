package com.erretechnology.intranet.controllers;

import java.io.IOException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.Podcast;

@Controller
@RequestMapping("podcast")
public class PodcastController extends BaseController {

	@GetMapping(value = "/")
	public ModelAndView uploadMAV() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("podcast");
		mav.addObject("podcast", new Podcast());
		mav.addObject("audioPodcast", servicePodcast.getAll().get(0));
		return mav;
	}
	
	@PostMapping(value = "/uploadPodcast")
	public String uploadPdf(@RequestParam("file") MultipartFile file,
			@ModelAttribute("podcast") Podcast podcast, HttpSession session) {
		// check if file is empty
		if (file.isEmpty()) {
			System.out.println("file vuoto");
			return "redirect:/podcast/";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// save the file on the local file system
		try {
			podcast.setTimestamp(Instant.now().getEpochSecond());
			podcast.setPodcast(file.getBytes());
			podcast.setUtente(serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
			podcast.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
			servicePodcast.save(podcast);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/podcast/";
	}
}
