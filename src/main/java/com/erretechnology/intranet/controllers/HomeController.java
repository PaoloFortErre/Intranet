package com.erretechnology.intranet.controllers;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.ComunicazioneHR;
import com.erretechnology.intranet.models.ElementiMyLife;
import com.erretechnology.intranet.models.ElementiMyWork;
import com.erretechnology.intranet.models.FilePdf;
import com.erretechnology.intranet.models.Manutenzione;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;
import com.erretechnology.intranet.models.VideoDelGiorno;
import com.erretechnology.intranet.repositories.RepositoryCategoriaCinema;
import com.erretechnology.intranet.repositories.RepositoryLinkedin;
import com.erretechnology.intranet.repositories.RepositoryManutenzione;

import net.bytebuddy.utility.RandomString;

@Controller
public class HomeController extends BaseController{
	
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private RepositoryLinkedin repositoryLinkedin;
	@Autowired
	RepositoryManutenzione repositoryManutenzione;
	@Autowired
	private RepositoryCategoriaCinema repoCategoria;

	
	@GetMapping("/")
	public String home() {
		return ("redirect:login");
	}

	// Login form
	@GetMapping(value = "/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("loginPage");
		return mav;
	}
	
	// Password Dimenticata da login
	@GetMapping(value = "/password_dimenticata")
	public ModelAndView passwordLost() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("password_lost");
		return mav;
	}
	
	
	
	@PostMapping(value = "/password_dimenticata")
	public String processForgotPassword(HttpServletRequest request,@RequestParam("email") String email, Model model) {
	    String token = RandomString.make(30);
	    if(serviceUtente.foundEmail(email)) {
	    	try {
		        serviceUtente.updateResetPasswordToken(token, email);
		        String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
		        sendEmail(email, resetPasswordLink);
		        model.addAttribute("message", "Ti abbiamo inviato una mail con il link per resettare la password!");
		    } catch (UnsupportedEncodingException | MessagingException e) {
		    	model.addAttribute("error", "Si è verificato un errore nell'inviare la email");
		    }
	     }else {
	    	 model.addAttribute("emailNF", "Non è stato trovato nessun account con la mail " + email);
	     }
	    return "password_lost";
	}
	
	public void sendEmail(String recipientEmail, String link)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom("noreply@test.com", "No Reply Erre Technology");
	    helper.setTo(recipientEmail);
	     
	    String subject = "Modulo di cambio password per Intranet The Gate";
	     
	    String content = "<p>Car* collega</p>"
	            + "<p>Questa mail è stata inviata automaticamente in seguito alla sua richiesta.</p>"
	            + "<p>Hai un'ora di tempo per settare la nuova password, clicca sul seguente link per cambiare password:</p>"
	            + "<h3><a href=\"" + link + "\">Cambia la tua password</a></h3>"
	            + "<br>"
	            + "<p>Se non sei stato tu a richiedere il cambio password, "
	            + "sei pregato di contattare l'amministratore."
	            + "Si prega di non rispondere a questa mail.</p>"
	            + "<br><p>Gruppo ErreTechnology</p>"
	             ;
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public ModelAndView showResetPasswordForm(@Param(value = "token") String token) {
		ModelAndView mav = new ModelAndView();
	    Utente utente = serviceUtente.findByResetPasswordToken(token);
	    if (utente == null || (Instant.now().getEpochSecond() - utente.getTimestampToken() > 3600)) {
	    	mav.setViewName("password_lost");
	    	mav.addObject("error", "Si è verificato un problema con il token, riprovare o richiederne uno nuovo");
	    }else {
	    	mav.setViewName("reset_password_form");
	    	mav.addObject("token", token);
	    }
	    
		if(token == "") {
			mav.setViewName("password_lost");
	    	mav.addObject("error", "Non è più possibile effettuare il cambio password con questo token");
	    	return mav;
		}
	//    Utente utente = serviceUtente.findByResetPasswordToken(token);
	    
	    return mav;
	}
 
	@PostMapping("/reset_password")
	public ModelAndView processResetPassword(HttpServletRequest request, @RequestParam("password") String psw ,
			@RequestParam("cPassword") String cPsw) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("reset_password_form");
		if(psw.equals(cPsw)) {
			String regex = "^(?=.*[0-9])"
					+ "(?=.*[a-z])(?=.*[A-Z])"
					+ "(?=\\S+$).{8,20}$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(psw);
			if(!m.matches()) {
		    	mav.addObject("erroreUtente", "La password deve contenere una lettera maiuscola, una lettera minuscola, un numero ed essere di almeno 8 caratteri");
		    	return mav;
			}
			String token = request.getParameter("token");
		    Utente utente = serviceUtente.findByResetPasswordToken(token);
		     
		    if (utente == null) {
		    	mav.addObject("erroreUtente", "Non è più presente nessun token per cambiare la password");
		    	return mav;
		    } else {  
		    	utente.setPassword(psw);
		    	utente.setTokenResetPassword(null);
		    	utente.setTimestampToken(0);
		        serviceUtente.saveUtente(utente);
		        saveLog("cambiato la password via \"Password dimenticata\"", serviceDatiPersonali.findByAutore(utente));
		        mav.setViewName("redirect:login");
			    return mav;
		    }
		}
    	mav.addObject("errorePassword", "Le due password non corrispondono");
    	return mav;
	}
	
	//Permission manager
	@GetMapping(value = "/permission_manager")
	public ModelAndView permissionManager() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("permissionManager");
		return mav;
	}

	// Homepage
	@GetMapping(value = "/homepage")
	public ModelAndView homepageAdmin(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("homepage");
		mav.addObject("linkedinPost", repositoryLinkedin.findLimit(4));
		mav.addObject("utente", serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())));
		return mav;
	}

	@PostMapping(value = "/setSession")
	public String setSession(HttpSession session) {
		final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente u  = serviceUtente.findByEmail(currentUserName);
		session.setAttribute("id", u.getId());
		if(serviceDatiPersonali.findById(u.getId()).getPasswordCambiata())
			return "redirect:/homepage";
		return "redirect:/profile/cambioPassword";
	}

	// Login form with error
	@GetMapping("/login-error")
	public String loginError(Model model, Utente utente) {
		model.addAttribute("loginError", true);
		return "loginPage";
	}
	
	@GetMapping (value= "/comunicazioniHr")
	public ModelAndView comunicazioniHr(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		List<ElementiMyWork> elementi = serviceElementiMyWork.findAll();
		List<ElementiMyWork> sondaggi = elementi.stream().filter(x -> x.getTipo().equals("sondaggio")).limit(7).collect(Collectors.toList());
		
		mav.addObject("sondaggi", sondaggi);
		
		mav.addObject("utenteDati", u);
		mav.addObject("comunicazioni", serviceComunicazioni.findFirst10ByVisibileTrueOrderByIdDesc());
		mav.setViewName("comunicazioniHr");
		return mav;
	}
	
	@GetMapping (value= "/myLife1")
	public ModelAndView MyLife1(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		List<ElementiMyLife> elementi = serviceElementiMyLife.findAll();
	
		elementi.stream().filter(x -> x.getTipo().equals("cinema")).forEach(x->x.setCinema(repoCategoria.findById(Integer.parseInt(x.getContenuto())).get()));
		elementi.stream().filter(x -> x.getFoto() != null).forEach(x -> x.setImmagine(serviceFileImmagine.getImmagine(x.getFoto())));
		List<ElementiMyLife> evento = elementi.stream().filter(x -> x.getTipo().equals("eventi")).collect(Collectors.toList());
		List<ElementiMyLife> aforismi = elementi.stream().filter(x -> x.getTipo().equals("aphorism")).collect(Collectors.toList());
		List<ElementiMyLife> film = elementi.stream().filter(x -> x.getTipo().equals("cinema")).collect(Collectors.toList());
		List<ElementiMyLife> libri = elementi.stream().filter(x -> x.getTipo().equals("book")).collect(Collectors.toList());
		VideoDelGiorno video = serviceVideo.getLastVideo("MyLife"); 
		
		mav.addObject("film", film);
		mav.addObject("eventilife", evento);
		mav.addObject("aforisma", aforismi);
		mav.addObject("libri", libri);
		mav.addObject("video", video);
		mav.addObject("utenteDati", u);
		
		mav.setViewName("myLife1");
		return mav;
	}
	
	@GetMapping (value= "/moduli")
	public ModelAndView moduli(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
		
	
		mav.addObject("user", u);
		mav.addObject("filePdf", serviceFilePdf.findAll().stream().filter(x->x.isVisibile()).sorted(Comparator.comparingLong(FilePdf::getTimestamp).reversed()).collect(Collectors.toList()));
		mav.setViewName("moduli");
		return mav;
	}
	
	@GetMapping("/maintain-enable")
    public String enableMaintanince(HttpSession session) throws Exception {
		if(!serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getRuolo().getNome().equals("ADMIN")) {
			throw new Exception("non hai i permessi per svolgere quest'azione");
		}
		System.err.println("manutenzione attiva");
		Manutenzione m = repositoryManutenzione.findById(1031).get();
		m.setManutenzione(true);
		repositoryManutenzione.save(m);
		return "redirect:/manutenzione";
    }
	@GetMapping("/maintain-disable")
    public String disableMaintanince(HttpSession session) throws Exception {
		if(!serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString())).getRuolo().getNome().equals("ADMIN")) {
			throw new Exception("non hai i permessi per svolgere quest'azione");
		}
		System.err.println("manutenzione disattivata");
		Manutenzione m = repositoryManutenzione.findById(1031).get();
		m.setManutenzione(false);
		repositoryManutenzione.save(m);
		return "redirect:/";
    }

}
