package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.erretechnology.intranet.models.PostLinkedin;

@Controller
@RequestMapping("linkedin")
public class LinkedinController extends BaseController {
	
	@GetMapping("/new")
	public String form(Model model) {
		model.addAttribute("linkedin", new PostLinkedin()); 
		return "linkedinForm";
	}
	
	@PostMapping(value = "/insert")
	public String post(@ModelAttribute("post") PostLinkedin post, HttpSession session, ModelMap model) {
		serviceLinkedin.save(post);
		saveLog("aggiunto un post linkedin", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));

		return "redirect:/homepage";
	}
	
	@RequestMapping("/cancella")
	public String cancellaCliente(@RequestParam int id, HttpSession session) {
		serviceLinkedin.deleteById(id);
		return "redirect:/homepage";
	}
}
