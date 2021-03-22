package com.erretechnology.intranet.controllers;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.CommentoModificato;
import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.PostModificato;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.repositories.RepositoryEvento;

@Controller
@RequestMapping(value = "myLife")
public class MyLifeController extends BaseController {
	@Autowired
	private RepositoryEvento repoEvento;
	//	@GetMapping(value="/myLife")
	//	public ModelAndView myLife() {
	//		ModelAndView mav = new ModelAndView();
	//		mav.setViewName("myLife");
	//		return mav;
	//	}

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		//	List<Post> messaggi = service.getLastMessage();
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		List<Evento> evento = (List<Evento>) repoEvento.findNextLifeEvents(Instant.now().getEpochSecond());
		System.out.println(evento.size());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("myLife");
		mav.addObject("messaggi", servicePost.getLastMessage());
		mav.addObject("utenteDati", u);
		mav.addObject("eventilife", evento.get(0));
		mav.addObject("eventilife1", evento.get(1));
	//	mav.addObject("eventilife2", evento.get(2));
	//	mav.addObject("img", serviceFileImmagine.getLastImmagineByUtente(u));
		return mav;
	}

	@PostMapping(value = "/editPost")
	public String updatePost(int id, HttpSession session, Post post) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		Post p = servicePost.findById(id);
		UtenteDatiPersonali autore = p.getAutore();
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
			saveLog("modificato un post in bacheca", autore);
			return "redirect:/myLife/";
		}
		return "redirect:/forbidden";
	}

	@PostMapping(value = "/editCommento")
	public String updateCommento(int id, HttpSession session, Commento commento) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		Commento c = serviceCommento.findById(id);
		UtenteDatiPersonali autore = c.getAutore();
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
			saveLog("modificato un commento in bacheca" ,  autore);
			return "redirect:/myLife/";
		}
		return "redirect:/forbidden";
	}

	@PostMapping(value = "/deletePost")
	public String deletePost(/*@ModelAttribute Post post*/ int id, HttpSession session) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		//int autoreId = servicePost.findById(id).getAutore().getId();
		UtenteDatiPersonali autore = servicePost.findById(id).getAutore();
		if(sessionId == autore.getId() || serviceAuthority.findByUserId(sessionId).stream().filter(x -> x.getIdPermesso().equals("DPS")).count() == 1) {
			Post p = servicePost.findById(id);
			p.setVisibile(false);
			servicePost.save(p);
			System.out.println("cancellazione post: RIUSCITO");
			saveLog("cancellato un post in bacheca", autore);
			return "redirect:/myLife/";
		}
		System.out.println("cancellazione post: PERMESSO NEGATO");
		return "redirect:/forbidden";

	}

	@PostMapping(value = "/deleteCommento")
	public String deleteCommento(/*@ModelAttribute Post post*/ int id, HttpSession session) {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		// int autoreId = serviceCommento.findById(id).getAutore().getId();
		UtenteDatiPersonali autoreCommento = serviceCommento.findById(id).getAutore();
		UtenteDatiPersonali autorePost = serviceCommento.findById(id).getPost().getAutore();
		//Utente utenteLoggato = serviceUtente.findById(sessionId);
		if(sessionId == autoreCommento.getId() ||
				serviceAuthority.findByUserId(sessionId).stream().filter(x-> x.getIdPermesso().equals("DCS")).count() == 1 ||
				autorePost.getId() == sessionId) {
			Commento c = serviceCommento.findById(id);
			c.setVisibile(false);
			serviceCommento.save(c);
			System.out.println("cancellazione post: RIUSCITO");
			saveLog("cancellato un commento in bacheca", serviceDatiPersonali.findById(sessionId));
			return "redirect:/myLife/";
		}
		System.out.println("cancellazione post: PERMESSO NEGATO");
		return "redirect:/forbidden";

	}

	@PostMapping(value = "/addCommento")
	public String inserisciCommento(Commento commento, HttpSession session, int idPost) {
		int id = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(id);
		commento.setAutore(autore);
		commento.setTimestamp(Instant.now().getEpochSecond());
		commento.setPost(servicePost.findById(idPost));
		commento.setVisibile(true);
		serviceCommento.save(commento);
		saveLog("risposto a un post in bacheca", autore);
		return "redirect:/myLife/";
	}

	@PostMapping(value = "/addPost")
	public String inserisciPost(Post post, HttpSession session, @RequestParam(required=false) MultipartFile document) throws IOException {
		//	String mail = session.getAttribute("email").toString();
		int id = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(id);
		post.setAutore(autore);
		post.setTimestamp(Instant.now().getEpochSecond());
		post.setVisibile(true);
		if(!document.getOriginalFilename().isEmpty()) {
			try {
				//String imageFolder = "fotoPost";
				//String fileName = serviceFileSystem.saveImage(imageFolder, document, id);
				//post.setImmagine(fileName);
				FileImmagine img = new FileImmagine();
				UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(id);
				img.setAutore(utenteLoggato);
				img.setData(document.getBytes());
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(document.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				post.setImmagine(img);

				System.err.println("File caricato");
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}
		}
			servicePost.save(post);
			saveLog("scritto in bacheca", autore);
			return "redirect:/myLife/";
		
		}
		


}
