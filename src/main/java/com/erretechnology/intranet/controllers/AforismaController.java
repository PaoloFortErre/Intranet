package com.erretechnology.intranet.controllers;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Aforisma;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping("aforisma")
public class AforismaController extends BaseController {

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String frase, String autore, Model model, HttpSession session, @RequestParam(required = false) MultipartFile immagine) throws IllegalStateException, IOException {
		Aforisma aforisma = serviceAforisma.findById(id);
		aforisma.setFrase(frase);
		aforisma.setAutore(autore);
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		//controllo se è stata passata un'immagine
		if(!immagine.getOriginalFilename().isEmpty()) {
			FileImmagine img = salvaImmagine(immagine, utenteLoggato);
			aforisma.setImmagine(img.getData());
		}
		serviceAforisma.save(aforisma);
		model.addAttribute("aforisma", aforisma);
		saveLog("modificato una aforisma", utenteLoggato);

		if(aforisma.isLife()) {
			return "redirect:/my-life/";
		} else {
			return "redirect:/my-work/";
		}
	}
}
