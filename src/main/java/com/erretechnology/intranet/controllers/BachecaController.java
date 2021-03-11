package com.erretechnology.intranet.controllers;

import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.services.ServicePost;
import com.erretechnology.intranet.services.ServiceUtente;

@Controller
@RequestMapping(value = "bacheca")
public class BachecaController {
	@Autowired
	ServicePost servicePost;
	
	@Autowired
	ServiceUtente serviceUtente;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView primaPagina() {
	//	List<Post> messaggi = service.getLastMessage();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bacheca");
		mav.addObject("messaggi", servicePost.getLastMessage());
		return mav;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteMessaggio(/*@ModelAttribute Post post*/ int id, HttpSession session) {
		if(Integer.parseInt(session.getAttribute("id").toString()) == servicePost.findById(id).getAutore().getId()) {
			Post p = servicePost.findById(id);
			p.setVisibile(false);
			servicePost.save(p);
			//servicePost.delete(servicePost.findById(id));
			return "redirect:/bacheca/";
		}
		System.out.println("ERRORE");	
		return "redirect:/";
		
	}
	
	@RequestMapping(value = "/addMessaggio", method = RequestMethod.POST)
	public String inserisci(Post post, BindingResult br, ModelMap model, HttpSession session) {
		System.out.println("test");
	//	String mail = session.getAttribute("email").toString();
		int id = Integer.parseInt(session.getAttribute("id").toString());
		Utente autore = serviceUtente.findById(id);
		post.setAutore(autore);
		
		post.setTimestamp(Instant.now().getEpochSecond());
		post.setVisibile(true);
		servicePost.save(post);
		return "redirect:/bacheca/";

	}
}
