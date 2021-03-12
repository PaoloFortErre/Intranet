package com.erretechnology.intranet.controllers;

import java.sql.PseudoColumnUsage;
import java.time.Instant;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.CommentoModificato;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.PostModificato;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.services.ServiceAuthority;
import com.erretechnology.intranet.services.ServiceCommento;
import com.erretechnology.intranet.services.ServiceCommentoModificato;
import com.erretechnology.intranet.services.ServicePost;
import com.erretechnology.intranet.services.ServicePostModificato;
import com.erretechnology.intranet.services.ServiceUtente;

@Controller
@RequestMapping(value = "bacheca")
public class BachecaController {
	@Autowired
	ServicePost servicePost;
	
	@Autowired
	ServiceUtente serviceUtente;
	
	@Autowired
	ServiceCommento serviceCommento;
	
	@Autowired
	ServiceAuthority serviceAuthority;
	
	@Autowired
	ServicePostModificato servicePostModificato;
	
	@Autowired
	ServiceCommentoModificato serviceCommentoModificato;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView primaPagina() {
	//	List<Post> messaggi = service.getLastMessage();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bacheca");
		mav.addObject("messaggi", servicePost.getLastMessage());
		return mav;
	}
	
	@RequestMapping(value = "/editPost", method = RequestMethod.POST)
	public String updatePost(int id, HttpSession session, Post post) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		Post p = servicePost.findById(id);
		Utente autore = p.getAutore();
		if(sessionId == autore.getId()) {
			String tmp = p.getTesto();
			p.setTesto(post.getTesto());
			PostModificato pm = new PostModificato();
			pm.setTesto(tmp);
			pm.setTimestamp(p.getTimestamp());
			pm.setPost(p);
			pm.setTimestamp(p.getTimestamp());
			p.setTimestamp(Instant.now().getEpochSecond());
			servicePost.save(p);
			servicePostModificato.save(pm);
			
			return "redirect:/bacheca/";
		}
		return "redirect:/forbidden";
	}
	
	@RequestMapping(value = "/editCommento", method = RequestMethod.POST)
	public String updateCommento(int id, HttpSession session, Commento commento) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		Commento c = serviceCommento.findById(id);
		Utente autore = c.getAutore();
		if(sessionId == autore.getId()) {
			String tmp = c.getTesto();
			c.setTesto(commento.getTesto());
			CommentoModificato cm = new CommentoModificato();
			cm.setTesto(tmp);
			cm.setTimestamp(c.getTimestamp());
			c.setTimestamp(Instant.now().getEpochSecond());
			cm.setCommento(c);
			cm.setTimestamp(c.getTimestamp());
			c.setTimestamp(Instant.now().getEpochSecond());
			serviceCommento.save(c);
			serviceCommentoModificato.save(cm);
			return "redirect:/bacheca/";
		}
		return "redirect:/forbidden";
	}
	
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public String deletePost(/*@ModelAttribute Post post*/ int id, HttpSession session) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		//int autoreId = servicePost.findById(id).getAutore().getId();
		Utente autore = servicePost.findById(id).getAutore();
		if(sessionId == autore.getId() || serviceAuthority.findByUsertId(sessionId).stream().filter(x -> x.getIdPermesso().equals("DPS")).count() == 1) {
			Post p = servicePost.findById(id);
			p.setVisibile(false);
			servicePost.save(p);
			System.out.println("cancellazione post: RIUSCITO");
			return "redirect:/bacheca/";
		}
		System.out.println("cancellazione post: PERMESSO NEGATO");	
		return "redirect:/forbidden";
		
	}
	
	@RequestMapping(value = "/deleteCommento", method = RequestMethod.POST)
	public String deleteCommento(/*@ModelAttribute Post post*/ int id, HttpSession session) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		// int autoreId = serviceCommento.findById(id).getAutore().getId();
		Utente autoreCommento = serviceCommento.findById(id).getAutore();
		Utente autorePost = serviceCommento.findById(id).getPost().getAutore();
		//Utente utenteLoggato = serviceUtente.findById(sessionId);
		if(sessionId == autoreCommento.getId() || 
				serviceAuthority.findByUsertId(sessionId).stream().filter(x-> x.getIdPermesso().equals("DCS")).count() == 1 ||
				autorePost.getId() == sessionId) {
			Commento c = serviceCommento.findById(id);
			c.setVisibile(false);
			serviceCommento.save(c);
			System.out.println("cancellazione post: RIUSCITO");
			return "redirect:/bacheca/";
		}
		System.out.println("cancellazione post: PERMESSO NEGATO");	
		return "redirect:/forbidden";
		
	}
	
	@RequestMapping(value = "/addCommento", method = RequestMethod.POST)
	public String inserisciCommento(Commento commento, HttpSession session, int idPost) {
		int id = Integer.parseInt(session.getAttribute("id").toString());
		Utente autore = serviceUtente.findById(id);
		commento.setAutore(autore);
		commento.setTimestamp(Instant.now().getEpochSecond());
		commento.setPost(servicePost.findById(idPost));
		commento.setVisibile(true);
		serviceCommento.save(commento);
		return "redirect:/bacheca/";
	}
	
	@RequestMapping(value = "/addPost", method = RequestMethod.POST)
	public String inserisciPost(Post post, HttpSession session) {
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
