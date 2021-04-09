package com.erretechnology.intranet.controllers;

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

import com.erretechnology.intranet.models.CategoriaCinema;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;

@Controller
@RequestMapping("categoriacinema")
public class CategoriaCinemaController extends BaseController{
	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("categoriaCinema", new CategoriaCinema()); 
		return "categoriaCinemaForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("categoriaCinema") CategoriaCinema categoriaCinema, ModelMap model, HttpSession session) {
				
		categoriaCinema.setVisibile(true);	
		repoCategoria.save(categoriaCinema);
		saveLog("inserito una nuova categoria cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
        return "categoriaCinema";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable int id, Model model) {
		CategoriaCinema categoriaCinema = repoCategoria.findById(id).get();
		model.addAttribute("categoriaCinema", categoriaCinema);
		return "categoriaCinemaFormUpdate";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String update(@PathVariable int id, String nome, HttpSession session, Model model) {
		CategoriaCinema categoriaCinema = repoCategoria.findById(id).get();
		categoriaCinema.setNome(nome);
		
		repoCategoria.save(categoriaCinema);
		model.addAttribute("categoriaCinema", categoriaCinema);
		saveLog("modificato le informazioni di una categoria cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "categoriaCinema";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		CategoriaCinema categoriaCinema = repoCategoria.findById(id).get();
		categoriaCinema.setVisibile(false);
		repoCategoria.save(categoriaCinema);
		saveLog("cancellato una categoria cinema", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return "redirect:/categoriaCinema/list";
	}
}
