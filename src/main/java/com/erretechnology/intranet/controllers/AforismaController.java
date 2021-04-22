package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erretechnology.intranet.models.Aforisma;

@Controller
@RequestMapping("aforisma")
public class AforismaController extends BaseController {
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String frase, String autore, Model model, HttpSession session, @RequestParam(required = false) MultipartFile immagine) throws IllegalStateException, IOException {
		String path = "/Users/giosi/OneDrive/Documenti/GitHub/Intranet/src/main/webapp";
		String folder;
		Aforisma aforisma = serviceAforisma.findById(id);
		aforisma.setFrase(frase);
		aforisma.setAutore(autore);
		if(!immagine.isEmpty()) {
			folder = aforisma.isLife() ? "/view/MyLife/images/" : "/view/MyWork/images/";
			folder += immagine.getOriginalFilename();
			path += folder;
			Path pathImg = Paths.get(path);
			Files.write(pathImg, immagine.getBytes());
			aforisma.setImmagine(folder);
			//immagine.transferTo(new File(path + immagine.getOriginalFilename()));
		}
		serviceAforisma.save(aforisma);
		model.addAttribute("aforisma", aforisma);
		saveLog("modificato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		
		if(aforisma.isLife()) {
	        return "redirect:/my-life/";
		} else {
	        return "redirect:/my-work/";
		}
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Aforisma aforisma = serviceAforisma.findById(id);
//		aforisma.setVisibile(false);
		aforisma.setAutore("");
		aforisma.setFrase("");
		serviceAforisma.save(aforisma);
		saveLog("eliminato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/aforisma/list";
	}
}
