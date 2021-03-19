package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.FileImmagine;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.Permesso;
import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
@RequestMapping(value = "profile")
public class UtenteController extends BaseController{

	@GetMapping(value = "/")
	public ModelAndView primaPagina(HttpSession session) {
		UtenteDatiPersonali u = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		if(serviceUtente.findById(u.getId()).getSetGruppi().stream().filter(x-> x.getNome().equals("ADMIN")).count() == 1){
			mav.setViewName("profilo_admin");
			mav.addObject("attivi",serviceUtente.getAll().stream().filter(x->x.getAttivo()).count());
			mav.addObject("log", serviceLog.findLastFive());
			mav.addObject("allLog", serviceLog.findAll());
		}
		else {
			mav.setViewName("profilo");
			mav.addObject("log", serviceLog.findLastFiveLogById(Integer.parseInt(session.getAttribute("id").toString())));
			mav.addObject("allLog", serviceLog.findLogById(Integer.parseInt(session.getAttribute("id").toString())));
		}
		mav.addObject("utente", u);
		return mav;
	}


	@PostMapping(value = "/modificaDescrizione")
	public String modificaDescrizione(@ModelAttribute("utente")UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(id_utente);
		utenteLoggato.setDescrizione(utente.getDescrizione());
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("aggiornato la descrizione", utenteLoggato);
		return "redirect:/profile/";
	}

	@PostMapping(value = "/eliminaFotoProfilo")
	public String eliminaFotoProfilo(@ModelAttribute("utente")UtenteDatiPersonali utente, HttpSession session) {
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(id_utente);
		utenteLoggato.setImmagine(serviceFileImmagine.getImmagine(63));
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("eliminato la foto profilo", utenteLoggato);
		return "redirect:/profile/";
	}


	@RequestMapping(value = "/cambioPassword", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView cambioPassword() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("cambiaPassword");
		mav.addObject("vecchiaPassword", new String());
		mav.addObject("nuovaPassword", new String());
		mav.addObject("cNuovaPassword", new String());
		return mav;
	}

	@PostMapping(value = "/paginaModificaPassword")
	public String cambiaPaginaModificaPagina(@RequestParam("vecchiaPassword") String vPsw, @RequestParam("nuovaPassword") String nPsw,
			@RequestParam("cNuovaPassword") String cNPsw, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		if(passwordEncoder.matches(vPsw, serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getPassword())){
			if(nPsw.equals(cNPsw)) {
				Utente u = serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString()));
				u.setPassword(nPsw);
				serviceUtente.saveUtente(u);
				UtenteDatiPersonali udp = serviceDatiPersonali.findById(u.getId());
				if(!udp.isPasswordCambiata()) {
					udp.setPasswordCambiata(true);
					serviceDatiPersonali.save(udp);
				}
				saveLog("modificato la password", udp);
				return "redirect:/profile/";
			}else {
				return "redirect:/profile/paginaModificaPassword";
			}
		}else{
			return "redirect:/profile/cambioPassword";
		}


		//System.out.println("ok ci sono");
		//return "cambiaPassword";
	}


	@PostMapping(value = "/cambiaFotoProfilo")
	public String modificaFoto(@RequestParam(required=false) MultipartFile immagine, HttpSession session) {
		int idUser = Integer.parseInt(session.getAttribute("id").toString());
		FileImmagine img = null;
		if(!immagine.getOriginalFilename().isEmpty()) {
			try {
				img = new FileImmagine();
				img.setData(immagine.getBytes());
				UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(idUser);
				if(!serviceFileImmagine.contains(img.getData())) {
					img.setAutore(utenteLoggato);
					img.setTimestamp(Instant.now().getEpochSecond());
					img.setNomeFile(StringUtils.cleanPath(immagine.getOriginalFilename()));
					serviceFileImmagine.insert(img);
					utenteLoggato.setImmagine(img);
				}else {
					utenteLoggato.setImmagine(serviceFileImmagine.getImmagineByData(img.getData()));
				}
				serviceDatiPersonali.save(utenteLoggato);
				saveLog("modificato l'immagine del profilo", utenteLoggato);
			} catch (Exception e) {
				System.err.println("Non riesco a caricare il file");
			}
		}
		return "redirect:/profile/";
	}


	@PostMapping(value = "/setVisualizzazione")
	public String modificaVisualizazzioneDataNascita(HttpSession session, /*HttpServletRequest request*/ @RequestParam("set") String value) {
		//String value = request.getParameter("set");
		int id_utente = Integer.parseInt(session.getAttribute("id").toString());
		UtenteDatiPersonali utenteLoggato= serviceDatiPersonali.findById(id_utente);
		if(value.equals("Si")) {
			utenteLoggato.setVisualizzaDataNascita(true);
		} else utenteLoggato.setVisualizzaDataNascita(false);
		serviceDatiPersonali.save(utenteLoggato);
		saveLog("modificato la visualizzazione del compleanno", utenteLoggato);
		return "redirect:/profile/";
	}

	@GetMapping(value = "/gestisciPermesso")
	public ModelAndView Permesso(/*int id*/) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("permissionManager");
		mav.addObject("utenti", serviceUtente.getAll().stream().map(x-> x.getEmail()).collect(Collectors.toList()));
		//List<String> permessiMancanti = servicePermesso.getAllName();
		//List<String> permessiUtente = serviceAuthority.getAllNameById(id);
		//permessiMancanti.removeAll(permessiUtente);
		//ModelAndView mav = new ModelAndView();
		//mav.setViewName("permissionManager");
		//mav.addObject("id_utente", id);
		//mav.addObject("permessiMancanti", permessiMancanti);
		//mav.addObject("permessiUtente", permessiUtente);
		return mav;
	}

	@RequestMapping(value = "/getPermessiMancanti")
	@ResponseBody
	public List<String> getPermessiMancanti(@RequestParam("email") String email){
		List<String> permessiMancanti = servicePermesso.getAllName();
		permessiMancanti.removeAll(getAllPermessi(email));
		return permessiMancanti;
	}

	@RequestMapping(value = "/getAllPermessi")
	@ResponseBody
	public List<String> getAllPermessi(@RequestParam("email") String email){
		return serviceAuthority.getAllNameById(serviceUtente.findByEmail(email).getId());
	}



	@PostMapping(value = "/addPermesso")
	public String addPermesso(String email,String flag,  @RequestParam(value = "pAdd" , required=false) String permesso,
			@RequestParam(value = "pRemove" , required=false) String permessoRimozione , HttpSession session) {
		if(permesso != "0" || permessoRimozione != "0") {
			int id_utente = Integer.parseInt(session.getAttribute("id").toString());
			UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
			Permesso p = null;
			Utente u = serviceUtente.findByEmail(email.substring(1));
			if(flag.equals(",1")) {
				p = servicePermesso.findById(permesso);
				u.addPermesso(p);
				serviceUtente.save(u);
				p.addUtente(u);
				saveLog("aggiunto il permesso" + p.getNome() +" a " + u.getEmail() , utenteLoggato);
			}else if(flag.equals(",0")) {
				p = servicePermesso.findById(permessoRimozione);
				u.removePermesso(p);
				serviceUtente.save(u);
				p.removeUtente(u);
				saveLog("rimosso il permesso" + p.getNome() + " a " + u.getEmail() , utenteLoggato);
			}

			servicePermesso.savePermesso(p);
		}
		return "redirect:/profile/gestisciPermesso";
	}

	@GetMapping(value = "/cancellaUtente")
	public ModelAndView rimuoviUtente(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		if(serviceUtente.findById(utenteLoggato.getId()).getSetGruppi().stream().filter(x-> x.getNome().equals("ADMIN")).count() == 1) {

			mav.setViewName("eliminaUtente");
			mav.addObject("utenti", serviceDatiPersonali.getAll().stream().filter(x -> x.getUtente().getAttivo()).collect(Collectors.toList()));
			return mav;

		}
		mav.setViewName("forbidden");
		return mav;

	}
	@PostMapping(value = "/elimina")
	public String rimuovi(@RequestParam("id_eliminato") int id) {
		Utente u = serviceUtente.findById(id);
		u.setAttivo(false);
		serviceUtente.save(u);
		return "redirect:/profile/cancellaUtente/";
	}
}
