package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.CommentoModificato;
import com.erretechnology.intranet.models.ElementiMyLife;
import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.PostModificato;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;

@Controller
@RequestMapping(value = "myLife")
public class MyLifeController extends BaseController {
	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session, @RequestParam("page") Optional<Integer> page) {
		int currentPage = page.orElse(1); 

		//	List<Post> messaggi = service.getLastMessage();
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		List<ElementiMyLife> elementi = serviceElementiMyLife.findAll();
		elementi.stream().filter(x -> x.getTipo().equals("cinema")).forEach(x->x.setCinema(repoCategoria.findById(Integer.parseInt(x.getContenuto())).get()));
		elementi.stream().filter(x -> x.getFoto() != null).forEach(x -> x.setImmagine(serviceFileImmagine.getImmagine(x.getFoto())));
		List<ElementiMyLife> evento = elementi.stream().filter(x -> x.getTipo().equals("eventi")).collect(Collectors.toList());
		List<ElementiMyLife> aforismi = elementi.stream().filter(x -> x.getTipo().equals("aphorism")).collect(Collectors.toList());
		List<ElementiMyLife> film = elementi.stream().filter(x -> x.getTipo().equals("cinema")).collect(Collectors.toList());
		List<ElementiMyLife> libri = elementi.stream().filter(x -> x.getTipo().equals("book")).collect(Collectors.toList());
		ElementiMyLife video = elementi.stream().filter(x -> x.getTipo().equals("video")).collect(Collectors.toList()).get(0);
		
		Page<Post> postPage= servicePost.findPaginated(PageRequest.of(currentPage - 1, 5));


		//System.out.println(evento.size());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("myLife");
		mav.addObject("messaggi", postPage);
		mav.addObject("utenteDati", u);
		int totalPages = postPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());	
			mav.addObject("pageNumbers", pageNumbers);
		}
		
		mav.addObject("film", film);
		mav.addObject("eventilife", evento);
		mav.addObject("aforisma", aforismi);
		mav.addObject("libri", libri);
		mav.addObject("video", video);
		
		return mav;
	}
	
	@GetMapping(value = "/addVideo")
	public ModelAndView setVideo(HttpSession session, String link) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("addVideoMyLife");
		mav.addObject("video", new VideoDelGiorno());
		
		return mav;
	}
	
	@PostMapping(value = "/video")
	public String video(@ModelAttribute("video") VideoDelGiorno video, HttpSession session) {
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		video.setPagina("MyLife");
		serviceVideo.save(video);
		saveLog("aggiornato il video su myLife", autore);
		return "redirect:/myLife/";
	}

	@PostMapping(value = "/editPost")
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
			if(!document.getOriginalFilename().isEmpty()) {
				FileImmagine img = new FileImmagine();
				img.setAutore(autore);
				img.setData(compressImage(document, 0.5f));
				img.setTimestamp(Instant.now().getEpochSecond());
				img.setNomeFile(StringUtils.cleanPath(document.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				p.setImmagine(img);
			}
			servicePost.save(p);
			servicePostModificato.save(pm);
			saveLog("modificato un post in bacheca", autore);
			return "redirect:/myLife/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	@PostMapping(value = "/editCommento")
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
			c.setTimestamp(Instant.now().getEpochSecond());
			cm.setCommento(c);
			cm.setTimestamp(c.getTimestamp());
			c.setTimestamp(Instant.now().getEpochSecond());
			serviceCommento.save(c);
			serviceCommentoModificato.save(cm);
			saveLog("modificato un commento in bacheca" ,  autore);
			return "redirect:/myLife/";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	@PostMapping(value = "/deletePost")
	public String deletePost(/*@ModelAttribute Post post*/ int id, HttpSession session) throws Exception {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		//int autoreId = servicePost.findById(id).getAutore().getId();
		UtenteDatiPersonali autore = servicePost.findById(id).getAutore();
		if(sessionId == autore.getId() || 
		   serviceAuthority.findByUserId(sessionId).stream().filter(x -> x.getIdPermesso().equals("DS")).count() == 1) {
			Post p = servicePost.findById(id);
			p.setTimestampEliminazione(Instant.now().getEpochSecond());
			p.setVisibile(false);
			servicePost.save(p);
			//System.out.println("cancellazione post: RIUSCITO");
			saveLog("cancellato un post in bacheca", autore);
			return "redirect:/myLife/";
		}
		//System.out.println("cancellazione post: PERMESSO NEGATO");
		throw new Exception("Non hai i permessi per svolgere quest'azione");

	}

	@PostMapping(value = "/deleteCommento")
	public String deleteCommento(/*@ModelAttribute Post post*/ int id, HttpSession session) throws Exception {
		int sessionId = Integer.parseInt(session.getAttribute("id").toString());
		// int autoreId = serviceCommento.findById(id).getAutore().getId();
		UtenteDatiPersonali autoreCommento = serviceCommento.findById(id).getAutore();
		UtenteDatiPersonali autorePost = serviceCommento.findById(id).getPost().getAutore();
		//Utente utenteLoggato = serviceUtente.findById(sessionId);
		if(sessionId == autoreCommento.getId() ||
		   serviceAuthority.findByUserId(sessionId).stream().filter(x-> x.getIdPermesso().equals("DS")).count() == 1 ||
		   autorePost.getId() == sessionId) {
			Commento c = serviceCommento.findById(id);
			c.setVisibile(false);
			c.setTimestampEliminazione(Instant.now().getEpochSecond());
			serviceCommento.save(c);
			//System.out.println("cancellazione post: RIUSCITO");
			saveLog("cancellato un commento in bacheca", serviceDatiPersonali.findById(sessionId));
			return "redirect:/myLife/";
		}
		//System.out.println("cancellazione post: PERMESSO NEGATO");
		throw new Exception("Non hai i permessi per svolgere quest'azione");

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
	public String inserisciPost(Post post, HttpSession session, @RequestParam(required=false) MultipartFile document) throws Exception {
		//	String mail = session.getAttribute("email").toString();
		int id = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali autore = serviceDatiPersonali.findById(id);
		post.setAutore(autore);
		post.setTimestamp(Instant.now().getEpochSecond());
		post.setVisibile(true);
		if(!document.getOriginalFilename().isEmpty()) {
			FileImmagine img = new FileImmagine();
			UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(id);
			img.setAutore(utenteLoggato);
			img.setData(compressImage(document, 0.5f));
			img.setTimestamp(Instant.now().getEpochSecond());
			img.setNomeFile(StringUtils.cleanPath(document.getOriginalFilename()));
			serviceFileImmagine.insert(img);
			post.setImmagine(img);
		}

		servicePost.save(post);
		saveLog("scritto in bacheca", autore);
		return "redirect:/myLife/";

	} 

	@GetMapping (value= "/profiliUtenti")
	public ModelAndView comunicazioniHr(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();

		mav.addObject("utenteDati", u);
		mav.addObject("utenti", serviceDatiPersonali.getAll().stream().filter(x->x.getUtente().getAttivo()).collect(Collectors.toList()));
		mav.setViewName("profiliUtenti");
		return mav;
	}
	
	@GetMapping(value ="/cancellaPost")
	public String eliminaPost(HttpSession session, int id) {
		Post p = servicePost.findById(id);
		servicePost.remove(p);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaPost")
	public String ripristinaPost(HttpSession session, int id) {
		Post p = servicePost.findById(id);
		p.setVisibile(true);
		servicePost.save(p);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/cancellaCommento")
	public String eliminaCommento(HttpSession session, int id) {
		Commento c = serviceCommento.findById(id);
		serviceCommento.delete(c);
		return "redirect:/profile/mostraEliminati";
	}
	
	@GetMapping(value ="/ripristinaCommento")
	public String ripristinaCommento(HttpSession session, int id) {
		Commento c = serviceCommento.findById(id);
		c.setVisibile(true);
		serviceCommento.save(c);;
		return "redirect:/profile/mostraEliminati";
	}

}
