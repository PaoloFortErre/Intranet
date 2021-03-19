package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.time.Instant;

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
	
	@GetMapping("/{id}")
	public String get(@PathVariable int id, Model model) {
		
		model.addAttribute("aforisma", repoAforisma.findById(id).get());
		return "aforisma";
	}
	
	@GetMapping("/list")
	public String getList(Model model) {
		model.addAttribute("aforismaList", repoAforisma.findAll());
		return "aforismaList";
	}
	
	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("aforisma", new Aforisma()); 
		return "aforismaForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("aforisma") Aforisma aforisma, HttpSession session, ModelMap model) throws IOException {
		
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		aforisma.setUtente(repoUtente.findById(idUser).get());
		aforisma.setVisibile(true);
		aforisma.setVisibile(true);
		
		repoAforisma.save(aforisma);
		saveLog("aggiunto una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
        return "aforisma";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		Aforisma aforisma = repoAforisma.findById(id).get();
		model.addAttribute("aforisma", aforisma);
		return "aforismaFormUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String frase, String autore, Model model, HttpSession session) {
		Aforisma aforisma = repoAforisma.findById(id).get();
		aforisma.setFrase(frase);
		aforisma.setAutore(autore);
		
		repoAforisma.save(aforisma);
		model.addAttribute("aforisma", aforisma);
		saveLog("modificato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "aforisma";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		Aforisma aforisma = repoAforisma.findById(id).get();
		aforisma.setVisibile(false);
		repoAforisma.save(aforisma);
		saveLog("eliminato una aforisma", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/aforisma/list";
	}
}
