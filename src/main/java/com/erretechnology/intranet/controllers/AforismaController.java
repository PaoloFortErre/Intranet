package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.erretechnology.intranet.models.Aforisma;

@Controller
@RequestMapping("aforisma")
public class AforismaController extends BaseController {
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String frase, String autore, Model model, HttpSession session) {
		Aforisma aforisma = serviceAforisma.findById(id);
		aforisma.setFrase(frase);
		aforisma.setAutore(autore);
		serviceAforisma.save(aforisma);
		model.addAttribute("aforisma", aforisma);
		saveLog("modificato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		
		if(aforisma.isLife()) {
	        return "redirect:/myLife1/";
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
