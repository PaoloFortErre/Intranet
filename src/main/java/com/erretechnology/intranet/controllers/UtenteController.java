package com.erretechnology.intranet.controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.regex.*;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Commento;
import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.Evento;
import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.Notifica;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.Podcast;
import com.erretechnology.intranet.models.Post;
import com.erretechnology.intranet.models.Sondaggio;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;
import com.erretechnology.intranet.repositories.RepositoryCinema;
import com.erretechnology.intranet.repositories.RepositoryCliente;
import com.erretechnology.intranet.repositories.RepositoryEventoLife;
import com.erretechnology.intranet.repositories.RepositoryEventoWork;
import com.erretechnology.intranet.repositories.RepositoryLibro;
import com.erretechnology.intranet.repositories.RepositoryNews;

@Controller
@RequestMapping(value = "profile")
public class UtenteController extends BaseController {

	@Autowired
	RepositoryNews repositoryNews;
	@Autowired
	RepositoryEventoLife repositoryEventoLife;
	@Autowired
	RepositoryEventoWork repositoryEventoWork;
	@Autowired
	RepositoryLibro repositoryLibro;
	@Autowired
	RepositoryCinema repositoryFilm;
	@Autowired
	RepositoryCliente repositoryCliente;

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		if (serviceUtente.findById(u.getId()).getRuolo().getNome().equals("ADMIN")) {
			mav.addObject("log", serviceLog.findLastFive());
	//		mav.addObject("allLog", serviceLog.findAll());
			mav.addObject("admin", true);
		} else {
//			mav.addObject("allLog", serviceLog.findLogById(u));
			mav.addObject("admin", false);
			mav.addObject("log", serviceLog.findLastFiveLogById(u));
		}
		mav.addObject("post", servicePost.getByAutore(u));
		mav.setViewName("profilo_admin");
		mav.addObject("attivi", serviceUtente.getAll().stream().filter(x -> x.getAttivo()).count());
		mav.addObject("utente", u);
		return mav;
	}

	@GetMapping(value = "/userList")
	public ModelAndView userList() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("usersList");
		mav.addObject("utenti", serviceDatiPersonali.getAll().stream().filter(x -> x.getUtente().getAttivo())
				.collect(Collectors.toList()));
		return mav;
	}

	@GetMapping(value = "/viewProfile")
	public ModelAndView viewUserProfile(HttpSession session, int id) {
		ModelAndView mav = new ModelAndView();
		UtenteDatiPersonali u = serviceDatiPersonali.findById(id);
		if (u.getId() == serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())).getId())
			return primaPagina(session);
		mav.addObject("post", servicePost.getByAutore(u));
		mav.setViewName("profilo_admin");
		mav.addObject("utente", u);
		mav.addObject("utenteDati", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return mav;
	}
	
	@GetMapping(value = "/eliminaNotifica/{id}")
	public String elimina(HttpSession session, @PathVariable int id) {
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		Notifica n = serviceNotifica.findById(id);
		u.removeNotifica(n);
		n.removeUtente(u);
		serviceNotifica.save(n);
		serviceDatiPersonali.save(u);

		switch(n.getDestinazione()) {
			case "MyWork":
				return "redirect:/myWork/";
			case "MyLife":
				return "redirect:/myLife1/";
			case "HR":
				return "redirect:/comunicazioniHr/";
			case "Moduli":
				return "redirect:/moduli/";
		}
		return "redirect:/profile/";
	}

	@PostMapping(value = "/modificaDescrizione")
	public String modificaDescrizione(@ModelAttribute("utente") UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
		utenteLoggato.setDescrizione(utente.getDescrizione());
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("aggiornato la descrizione", utenteLoggato);
		return "redirect:/profile/";
	}

	@PostMapping(value = "/eliminaFotoProfilo")
	public String eliminaFotoProfilo(@ModelAttribute("utente") UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
		utenteLoggato.setImmagine(serviceFileImmagine.getImmagine(63));
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("eliminato la foto profilo", utenteLoggato);
		return "redirect:/profile/";
	}

	@RequestMapping(value = "/cambioPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView cambioPassword(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cambiaPassword");
		mav.addObject("vecchiaPassword", new String());
		mav.addObject("nuovaPassword", new String());
		mav.addObject("cNuovaPassword", new String());
		mav.addObject("obbligato", serviceDatiPersonali
				.findById(Integer.parseInt(session.getAttribute("id").toString())).getPasswordCambiata());
		return mav;
	}

	@PostMapping(value = "/paginaModificaPassword")
	public ModelAndView cambiaPaginaModificaPagina(@RequestParam("vecchiaPassword") String vPsw,
			@RequestParam("nuovaPassword") String nPsw, @RequestParam("cNuovaPassword") String cNPsw,
			HttpSession session, Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=\\S+$).{8,20}$";
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cambiaPassword");
		if (passwordEncoder.matches(vPsw,
				serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getPassword())) {
			if (nPsw.equals(cNPsw)) {
				if (nPsw.equals(vPsw)) {
					mav.addObject("messaggio", "la nuova password non può essere uguale alla precedente");
					return mav;
				} else {
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(nPsw);
					if (!m.matches()) {
						mav.addObject("messaggio",
								"La password deve contenere una lettera maiuscola, una lettera minuscola, un numero ed essere di almeno 8 caratteri");
					} else {
						Utente u = serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString()));
						u.setPassword(nPsw);
						serviceUtente.saveUtente(u);
						UtenteDatiPersonali udp = serviceDatiPersonali.findById(u.getId());
						if (!udp.isPasswordCambiata()) {
							udp.setPasswordCambiata(true);
							serviceDatiPersonali.save(udp);
						}
						saveLog("modificato la password", udp);
						return primaPagina(session);
					}

				}
			} else {
				mav.addObject("messaggio", "Le password non corrispondono");
			}
		} else {
			mav.addObject("messaggio", "la vecchia password è sbagliata");
		}
		mav.addObject("vecchiaPassword", new String());
		mav.addObject("nuovaPassword", new String());
		mav.addObject("cNuovaPassword", new String());
		mav.addObject("obbligato", serviceDatiPersonali
				.findById(Integer.parseInt(session.getAttribute("id").toString())).getPasswordCambiata());
		return mav;
	}

	@PostMapping(value = "/cambiaFotoProfilo")
	public String modificaFoto(@RequestParam(required = false) MultipartFile immagine, @RequestParam(required = false) String dati, 
			@RequestParam(required = false) String nome, HttpSession session)
			throws Exception {
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		FileImmagine img = null;
		if ((immagine != null && !immagine.getOriginalFilename().isEmpty()) || !dati.isEmpty()) {

			img = new FileImmagine();
			/*if(compressImage(immagine, 0.5f).length == 0)
				*/
			if(immagine == null) 
				img.setData(Base64.getDecoder().decode(dati.getBytes("UTF-8")));
			else
				img.setData(immagine.getBytes());
			
			/*else
				img.setData(compressImage(immagine, 0.5f));*/
			UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(idUser);
			if (!serviceFileImmagine.contains(img.getData())) {
				img.setAutore(utenteLoggato);
				img.setTimestamp(Instant.now().getEpochSecond());
				if(immagine == null)
					img.setNomeFile(nome);
				else
					img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
				serviceFileImmagine.insert(img);
				utenteLoggato.setImmagine(img);
			} else {
				utenteLoggato.setImmagine(serviceFileImmagine.getImmagineByData(img.getData()));
			}
			serviceDatiPersonali.save(utenteLoggato);
			saveLog("modificato l'immagine del profilo", utenteLoggato);

		}
		return "redirect:/profile/";
	}

	// PAGINA ATTIVITA' RECENTI
	@GetMapping(value = "/mostra_log")
	public ModelAndView mostraLog(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mostra_tutto_log");
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if (serviceUtente.findById(u.getId()).getRuolo().getNome().equals("ADMIN")) {
			mav.addObject("allLog", serviceLog.findAll());
			mav.addObject("admin", true);
		} else {
			mav.addObject("allLog", serviceLog.findLogById(u));
			mav.addObject("admin", false);
		}
		return mav;
	}

	@PostMapping(value = "/setVisualizzazione")
	public String modificaVisualizazzioneDataNascita(HttpSession session,
			/* HttpServletRequest request */ @RequestParam("set") String value) {
		// String value = request.getParameter("set");
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
		if (value.equals("Si")) {
			utenteLoggato.setVisualizzaDataNascita(true);
		} else
			utenteLoggato.setVisualizzaDataNascita(false);
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("modificato la visualizzazione del compleanno", utenteLoggato);
		return "redirect:/profile/";
	}

	@GetMapping(value = "/gestisciPermesso")
	public ModelAndView Permesso(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("gestisci_ruolo");
		mav.addObject("utenti", serviceDatiPersonali.getAll().stream()
				.filter(x->x.getId() != Integer.parseInt(session.getAttribute("id").toString())).collect(Collectors.toList()));
		return mav;
	}

	@RequestMapping(value = "/getPermessiMancanti")
	@ResponseBody
	public Map<String, String> getPermessiMancanti(@RequestParam("email") String email) {
		List<String> list1 = servicePermesso.getAll().stream().map(x -> x.getNome()).collect(Collectors.toList());
		List<String> list2 = servicePermesso.getAll().stream().map(x -> x.getDescrizione())
				.collect(Collectors.toList());
		Map<String, String> permessiMancanti = IntStream.range(0, Math.min(list1.size(), list2.size())).boxed()
				.collect(Collectors.toMap(list1::get, list2::get));
		permessiMancanti.keySet().removeAll(getAllPermessi(email).keySet());
		return permessiMancanti;
	}

	@RequestMapping(value = "/getAllPermessi")
	@ResponseBody
	public Map<String, String> getAllPermessi(@RequestParam("email") String email) {
		return serviceAuthority.getMapById(serviceUtente.findByEmail(email).getId());
	}
	
	@RequestMapping(value = "/isAdmin")
	@ResponseBody
	public Boolean[] isAdmin(@RequestParam("id") String id,@RequestParam("email") String email) {
		Boolean[] isAdmin = new Boolean[2];
		isAdmin[0] = serviceUtente.findById(Integer.parseInt(id)).getRuolo().getNome().equals("ADMIN");
		isAdmin[1] = serviceUtente.findByEmail(email).getRuolo().getNome().equals("ADMIN");
		return isAdmin;
	}

	@GetMapping(value = "/addPermesso")
	public String addPermesso(String email, String list, HttpSession session, Model model) throws Exception {
		String[] permessi = list.split(",");
		
		// Set<String> targetSet = new
		// CopyOnWriteArraySet<String>(Arrays.asList(permessi));
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
		if (utenteLoggato.getUtente().getRuolo().getNome().equals("ADMIN")
				|| utenteLoggato.getUtente().getSetPermessi().contains(servicePermesso.findById("AM"))) {
			Utente u = serviceUtente.findByEmail(email);
			if(Arrays.stream(permessi).anyMatch("admin"::equals)) {
				u.setRuolo(serviceRuolo.getById("ADMIN"));
				List<String> permessiList = Arrays.stream(permessi).filter(x-> !x.equals("admin")).collect(Collectors.toList());
				permessi = permessiList.toArray(new String[permessiList.size()]);
			}else {
				if(u.getRuolo().getNome().equals("ADMIN"))
					u.setRuolo(serviceRuolo.getById("USER"));
			}
				
			Set<Permesso> permessiUtente = new HashSet<Permesso>();
			boolean flag;
			Set<Permesso> pUtenti = u.getSetPermessi();
			Set<Permesso> temp = new HashSet<Permesso>();
			for (Permesso pUtente : pUtenti) {
				flag = false;
				for (String permesso : permessi) {
					if (flag)
						break;
					if (pUtente.getNome().equals(permesso)) {
						flag = true;
					}
				}
				if (!flag) {
					temp.add(pUtente);
				}
			}
			for (Permesso p : temp) {
				u.removePermesso(p);
				p.removeUtente(u);
				servicePermesso.savePermesso(p);
			}
			for (String per : permessi) {
				Permesso p = servicePermesso.findById(per.trim());
				if (u.getSetPermessi().contains(p)) {
				} else {
					permessiUtente.add(p);
					p.addUtente(u);
					servicePermesso.savePermesso(p);
				}
			}

			u.setSetPermessi(permessiUtente);
			serviceUtente.save(u);
			saveLog("modificato i permessi di " + serviceDatiPersonali.findByAutore(u).getNome() + " "
					+ serviceDatiPersonali.findByAutore(u).getCognome(), utenteLoggato);
			return "redirect:/profile/gestisciPermesso";
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	@GetMapping(value = "/cancellaUtente")
	public ModelAndView rimuoviUtente(HttpSession session, boolean flag, String messaggio, Model model)
			throws Exception {
		if (flag)
			model.addAttribute("messaggio", messaggio);
		ModelAndView mav = new ModelAndView();
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali
				.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if (serviceUtente.findById(utenteLoggato.getId()).getRuolo().getNome().equals("ADMIN")
				|| utenteLoggato.getUtente().getSetPermessi().contains(servicePermesso.findById("AM"))) {
			mav.setViewName("eliminaUtente");
			mav.addObject("utenti", serviceDatiPersonali.getAll().stream().filter(x -> x.getUtente().getAttivo() && 
					x.getId() != Integer.parseInt(session.getAttribute("id").toString()))
					.collect(Collectors.toList()));
			return mav;
		}
		throw new Exception("Non hai i permessi per svolgere quest'azione");
	}

	// form Registrazione
	@GetMapping(value = "/registra")
	public ModelAndView registrazione(Model model, String nome, String messaggio, boolean flag) {
		if (flag) {
			model.addAttribute(nome, messaggio);
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("registra_utente");
		mav.addObject("user", new UtenteDatiPersonali());
		mav.addObject("email", new String());
		mav.addObject("password", new String());
		mav.addObject("settore", new String());
		mav.addObject("date", new Utility());
		mav.addObject("utenti_non_attivi", serviceDatiPersonali.getInattivi());
		return mav;
	}

	@PostMapping(value = "/eseguiRegistrazione")
	public ModelAndView addUtente(@ModelAttribute("user") UtenteDatiPersonali utenteDP,
			@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("settore") String settore, Utility data, Model model) {
		if (serviceUtente.foundEmail(email)) {
			return registrazione(model, "aggiungi", "È gia presente un account con questa email", true);
		} else {
			String regex = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";

			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(email);
			if (!m.matches()) {
				return registrazione(model, "aggiungi", "L'email inserita non è valida", true);
			}

			utenteDP.setDataNascita(Timestamp.valueOf(data.getDate().atStartOfDay()).getTime() / 1000);
			serviceDatiPersonali.insert(password, email, settore, utenteDP);
			model.addAttribute("registrazione", "registrazione effettuata con successo");
			return registrazione(model, "aggiungi2", "Registrazione effettuata con successo", true);
		}
	}

	@PostMapping(value = "/riattiva")
	public ModelAndView riattiva(@RequestParam("id_riattivato") int id, Model model) {
		Utente u = serviceUtente.findById(id);
		UtenteDatiPersonali utente = serviceDatiPersonali.findByAutore(u);
		u.setAttivo(true);
		servicePost.getAllByAutore(utente).stream().filter(x->x.getTimestampEliminazione() == 0).forEach(x->{x.setVisibile(true);});
		serviceCommento.getAllByAutore(utente).stream().filter(x->x.getTimestampEliminazione() == 0).forEach(x->{x.setVisibile(true);});
		serviceUtente.save(u);

		return registrazione(model, "riattiva",
				"L'utente " + utente.getNome() + " " + utente.getCognome() + " è stato riattivato", true);
	}

	@PostMapping(value = "/elimina")
	public ModelAndView rimuovi(@RequestParam("id_eliminato") int id, Model model, HttpSession session)
			throws Exception {
		Utente u = serviceUtente.findById(id);
		UtenteDatiPersonali utente = serviceDatiPersonali.findByAutore(u);
		u.setAttivo(false);
		servicePost.getAllByAutore(utente).stream().forEach(x->{x.setVisibile(false);});
		serviceCommento.getAllByAutore(utente).stream().forEach(x->{x.setVisibile(false);});
		serviceUtente.save(u);
		return rimuoviUtente(session, true,
				"L'utente " + utente.getNome() + " " + utente.getCognome() + " è stato disattivato", model);
	}

	@GetMapping(value = "/mostraEliminati")
	public ModelAndView mostraNonAttivi(HttpSession session) throws Exception {
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if (!u.getUtente().getRuolo().getNome().equals("ADMIN"))
			throw new Exception("Fidati. Prima chiedi i permessi");
		List<Evento> eventi = repositoryEventoWork.getAllNotVisible();
		CompletableFuture<List<Post>> posts = servicePost.getAllNotVisible();
		CompletableFuture<List<Commento>> commenti = serviceCommento.getAllNotVisible();
		CompletableFuture<List<ComunicazioneHR>> comunicazioni = serviceComunicazioni.getAllNotVisible();
		CompletableFuture<List<FilePdf>> moduli = serviceFilePdf.getAllNotVisible();
		CompletableFuture<List<Podcast>> podcast = servicePodcast.getAllNotVisible();
		CompletableFuture<List<Sondaggio>> sondaggi = serviceSondaggio.getAllNotVisible();
		CompletableFuture.allOf(posts, commenti, comunicazioni, moduli,podcast, sondaggi).join();
		eventi.addAll(repositoryEventoLife.getAllNotVisible());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("gestione_suprema");
		mav.addObject("post", posts.get().stream().filter(x->x.getAutore().getUtente().getAttivo()).collect(Collectors.toList()));
		mav.addObject("commenti", commenti.get().stream().filter(x->x.getAutore().getUtente().getAttivo()).collect(Collectors.toList()));
		mav.addObject("news", repositoryNews.getAllNotVisible());
		mav.addObject("comunicazioniHR", comunicazioni.get());
		mav.addObject("moduli", moduli.get());
		mav.addObject("eventi", eventi);
		mav.addObject("podcast", podcast.get());
		mav.addObject("libri", repositoryLibro.getAllNotVisible());
		mav.addObject("film", repositoryFilm.getAllNotVisible());
		mav.addObject("client", repositoryCliente.getAllNotVisible());
		mav.addObject("sondaggi", sondaggi.get());
		return mav;
	}

}
