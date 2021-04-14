package com.erretechnology.intranet.controllers;

import java.io.IOException;

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
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Aforisma;
import com.erretechnology.intranet.repositories.RepositoryAforisma;
import com.erretechnology.intranet.repositories.RepositoryUtente;

@Controller
@RequestMapping("aforisma")
public class AforismaController extends BaseController {
	@Autowired
	private RepositoryAforisma repoAforisma;
	@Autowired
	private RepositoryUtente repoUtente;
	
	@GetMapping("/life/new")
	public String formLife(Model model) {
		model.addAttribute("aforisma", new Aforisma()); 
		return "aforismaForm";
	}
	
	@GetMapping("/work/new")
	public String formWork(Model model) {
		model.addAttribute("aforisma", new Aforisma()); 
		return "aforismaFormWork";
	}
	
	@PostMapping(value = "/life/insert")
	public String postLife(@ModelAttribute("aforisma") Aforisma aforisma, HttpSession session, ModelMap model) throws IOException {
		
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		aforisma.setUtente(repoUtente.findById(idUser).get());
//		aforisma.setVisibile(true);
//		aforisma.setVisibile(true);
		aforisma.setLife(true);
		
		repoAforisma.save(aforisma);
		saveLog("aggiunto una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
        return "redirect:/myLife1/";
	}
	
	@PostMapping(value = "/work/insert")
	public String postWork(@ModelAttribute("aforisma") Aforisma aforisma, HttpSession session, ModelMap model) throws IOException {
		
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		aforisma.setUtente(repoUtente.findById(idUser).get());
	//	aforisma.setVisibile(true);
//		aforisma.setVisibile(true);
		aforisma.setLife(false);
		
		repoAforisma.save(aforisma);
		saveLog("aggiunto una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
        return "redirect:/myWork/";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public ModelAndView updateForm(@PathVariable int id) {
		ModelAndView mav = new ModelAndView();
		Aforisma aforisma = repoAforisma.findById(id).get();
		mav.addObject("aforisma", aforisma);
		mav.addObject("isLife", aforisma.isLife());
		mav.setViewName("aforismaFormUpdate");
		return mav;
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String frase, String autore, Model model, HttpSession session) {
		Aforisma aforisma = repoAforisma.findById(id).get();
		aforisma.setFrase(frase);
		aforisma.setAutore(autore);
		repoAforisma.save(aforisma);
		model.addAttribute("aforisma", aforisma);
		saveLog("modificato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		
		if(aforisma.isLife()) {
	        return "redirect:/myLife1/";
		} else {
	        return "redirect:/myWork/";
		}
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Aforisma aforisma = repoAforisma.findById(id).get();
//		aforisma.setVisibile(false);
		aforisma.setAutore("");
		aforisma.setFrase("");
		repoAforisma.save(aforisma);
		saveLog("eliminato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/aforisma/list";
	}
}
