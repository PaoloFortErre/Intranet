package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.CommentoModificato;
import com.erretechnology.intranet.models.ElementiMyLife;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.PostModificato;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;

@Controller
@RequestMapping(value = "my-life")
public class MyLifeController extends BaseController {
	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) throws InterruptedException, ExecutionException {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		List<ElementiMyLife> elementi = serviceElementiMyLife.findAll();
	
		CompletableFuture<List<ElementiMyLife>> evento = findLifeElement(elementi, "eventi");// elementi.stream().filter(x -> x.getTipo().equals("eventi")).collect(Collectors.toList());
		CompletableFuture<List<ElementiMyLife>> aforismi = findLifeElement(elementi, "aphorism");//elementi.stream().filter(x -> x.getTipo().equals("aphorism")).collect(Collectors.toList());
		CompletableFuture<List<ElementiMyLife>> film = findLifeElement(elementi, "cinema");// elementi.stream().filter(x -> x.getTipo().equals("cinema")).collect(Collectors.toList());
		CompletableFuture<List<ElementiMyLife>> libri = findLifeElement(elementi, "book");// elementi.stream().filter(x -> x.getTipo().equals("book")).collect(Collectors.toList());
		CompletableFuture<VideoDelGiorno> video = serviceVideo.getLastVideo("MyLife");
		
		CompletableFuture.allOf(evento, aforismi, video, film, libri).join();

		mav.addObject("film", film.get());
		mav.addObject("eventilife", evento.get());
		mav.addObject("aforisma", aforismi.get());
		mav.addObject("libri", libri.get());
		mav.addObject("video", video.get());
		mav.addObject("categorie", repoCategoria.findAll());
		mav.setViewName("myLife1");
		mav.addObject("utenteDati", u);
		return mav;
	}
	
	@GetMapping(value = "/aggiungi-video")
	public ModelAndView setVideo(HttpSession session, String link) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addVideoMyLife");
		mav.addObject("video", new VideoDelGiorno());
		notificaTutti("ha inserito un nuovo video su MyLife!", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())),"MyLife");

		return mav;
	}
	
	@GetMapping(value = "/autore-post")
	@ResponseBody
	public int autorePost(int id, HttpSession session) {
		Post p = servicePost.findById(id);
		UtenteDatiPersonali utenteSessione = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		return p.getSetUtenti().contains(utenteSessione) ? 1 : 0;
	}
	
	
	@PostMapping(value = "/video")
	public String video(@ModelAttribute("video") VideoDelGiorno video, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		video.setPagina("MyLife");
		serviceVideo.save(video);
		saveLog("aggiornato il video su myLife", autore);
		return "redirect:/my-life/";
	}
	
	@PostMapping(value = "/like-post")
	public String likePost(int id, HttpSession session) {
		UtenteDatiPersonali utente = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		Post p = servicePost.findById(id);
		boolean flag = p.getSetUtenti().contains(utente);
		if(flag) 
			p.removeUtente(utente);
		else
			p.addUtente(utente);
		servicePost.save(p);
		if(flag) {
			utente.removePostPiaciuto(p);
			saveLog("rimosso mi piace a un post", utente);
		}
			
		else {
			utente.addPostPiaciuto(p);
			saveLog("messo mi piace a un post", utente);
			notificaSingola("ha messo mi piace al tuo post", p.getAutore(), utente, "MyLife");
		}
			
		return "redirect:/my-life1/";
	}

	@PostMapping(value = "/edit-post")
	public String updatePost(int id, HttpSession session, Post post,  @RequestParam(required=false) MultipartFile document) throws Exception {
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
			p.setTimestamp(Instant.now().getEpochSecond());
			//controllo se ?? stata passata un'immagine
			if(!document.getOriginalFilename().isEmpty()) 
				p.setImmagine(salvaImmagine(document, serviceDatiPersonali.findById(sessionId)));
			servicePost.save(p);
			servicePostModificato.save(pm);
			saveLog("modificato un post in bacheca", autore);
			return "redirect:/my-life1/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	@PostMapping(value = "/edit-commento")
	public String updateCommento(int id, HttpSession session, Commento commento) throws Exception {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		Commento c = serviceCommento.findById(id);
		UtenteDatiPersonali autore = c.getAutore();
		if(sessionId == autore.getId()) {
			String tmp = c.getTesto();
			c.setTesto(commento.getTesto());
			CommentoModificato cm = new CommentoModificato();
			cm.setTesto(tmp);
			cm.setTimestamp(c.getTimestamp());
			cm.setCommento(c);
			cm.setTimestamp(c.getTimestamp());
			c.setTimestamp(Instant.now().getEpochSecond());
			serviceCommento.save(c);
			serviceCommentoModificato.save(cm);
			saveLog("modificato un commento in bacheca" ,  autore);
			return "redirect:/my-life1/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	@PostMapping(value = "/delete-post")
	public String deletePost(int id, HttpSession session) throws Exception {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = servicePost.findById(id).getAutore();
		if(sessionId == autore.getId() || 
		   serviceAuthority.findByUserId(sessionId).stream().filter(x -> x.getIdPermesso().equals("DS")).count() == 1) {
			Post p = servicePost.findById(id);
			p.setTimestampEliminazione(Instant.now().getEpochSecond());
			p.setVisibile(false);
			servicePost.save(p);
			saveLog("cancellato un post in bacheca", autore);
			return "redirect:/my-life1/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");

	}

	@PostMapping(value = "/delete-commento")
	public String deleteCommento(int id, HttpSession session) throws Exception {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autoreCommento = serviceCommento.findById(id).getAutore();
		UtenteDatiPersonali autorePost = serviceCommento.findById(id).getPost().getAutore();
		if(sessionId == autoreCommento.getId() ||
		   serviceAuthority.findByUserId(sessionId).stream().filter(x-> x.getIdPermesso().equals("DS")).count() == 1 ||
		   autorePost.getId() == sessionId) {
			Commento c = serviceCommento.findById(id);
			c.setVisibile(false);
			c.setTimestampEliminazione(Instant.now().getEpochSecond());
			serviceCommento.save(c);
			saveLog("cancellato un commento in bacheca", serviceDatiPersonali.findById(sessionId));
			return "redirect:/my-life1/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");

	}

	@PostMapping(value = "/add-commento")
	public String inserisciCommento(Commento commento, HttpSession session, int idPost) {
		int id = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(id);
		UtenteDatiPersonali autorePost = servicePost.findById(idPost).getAutore();
		commento.setAutore(autore);
		commento.setTimestamp(Instant.now().getEpochSecond());
		commento.setPost(servicePost.findById(idPost));
		commento.setVisibile(true);
		serviceCommento.save(commento);
		notificaSingola("ha commentato il tuo post", autorePost, autore, "MyLife");
		saveLog("risposto a un post in bacheca", autore);
		return "redirect:/my-life1/";
	}

	@PostMapping(value = "/add-post")
	public String inserisciPost(Post post, HttpSession session, @RequestParam(required=false) MultipartFile document) throws Exception {
		int id = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(id);
		post.setAutore(autore);
		post.setTimestamp(Instant.now().getEpochSecond());
		post.setVisibile(true);
		//controllo se ?? stata passata un'immagine
		if(!document.getOriginalFilename().isEmpty()) 
			post.setImmagine(salvaImmagine(document, autore));

		servicePost.save(post);
		saveLog("scritto in bacheca", autore);
		return "redirect:/my-life1/";

	} 

	@GetMapping (value= "/profili-utenti")
	public ModelAndView comunicazioniHr(HttpSession session) throws InterruptedException, ExecutionException {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();

		mav.addObject("utenteDati", u);
		mav.addObject("utenti", serviceDatiPersonali.getAll().get().stream().filter(x->x.getUtente().getAttivo()).collect(Collectors.toList()));
		mav.setViewName("profiliUtenti");
		return mav;
	}
	
	@GetMapping(value ="/cancella-post")
	public String eliminaPost(HttpSession session, int id) {
		Post p = servicePost.findById(id);
		servicePost.remove(p);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@GetMapping(value ="/ripristina-post")
	public String ripristinaPost(HttpSession session, int id) {
		Post p = servicePost.findById(id);
		p.setVisibile(true);
		p.setTimestampEliminazione(0);
		servicePost.save(p);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@GetMapping(value ="/cancella-commento")
	public String eliminaCommento(HttpSession session, int id) {
		Commento c = serviceCommento.findById(id);
		serviceCommento.delete(c);
		return "redirect:/profilo/mostra-eliminati";
	}
	
	@GetMapping(value ="/ripristina-commento")
	public String ripristinaCommento(HttpSession session, int id) {
		Commento c = serviceCommento.findById(id);
		c.setVisibile(true);
		c.setTimestampEliminazione(0);
		serviceCommento.save(c);;
		return "redirect:/profilo/mostra-eliminati";
	}

}
