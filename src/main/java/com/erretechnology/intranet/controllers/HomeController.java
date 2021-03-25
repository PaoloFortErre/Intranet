package com.erretechnology.intranet.controllers;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.models.Utility;

import net.bytebuddy.utility.RandomString;

@Controller
public class HomeController extends BaseController{
	
	@Autowired
    private JavaMailSender mailSender;
	
	@GetMapping("/")
	public String home() {
		return ("redirect:login");
	}

	@GetMapping("/forbidden")
	public ModelAndView admin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("forbidden");
		return mav;
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
	            + "<p>clicca sul seguente link per cambiare password:</p>"
	            + "<p><a href=\"" + link + "\">Cambia la tua password</a></p>"
	            + "<br>"
	            + "<p>Se non sei stato tu a richiedere il cambio password, "
	            + "sei pregato di contattare l'amministratore."
	            + "Si prega di non rispondere a questa mail."
	            + "Gruppo ErreTechnology</p>"
	             ;
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public ModelAndView showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    Utente utente = serviceUtente.findByResetPasswordToken(token);
	    ModelAndView mav = new ModelAndView();
	     
	    if (utente == null) {
	        
	    }else {
	    	mav.setViewName("reset_password_form");
	    	mav.addObject("token", token);
	    }
	     
	    return mav;
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, @RequestParam("password") String psw ,
			@RequestParam("cPassword") String cPsw) {
		if(psw.equals(cPsw)) {
			String token = request.getParameter("token");

		     
		    Utente utente = serviceUtente.findByResetPasswordToken(token);
		     
		    if (utente == null) {
		    	
		    } else {  
		    	utente.setPassword(psw);
		    	utente.setTokenResetPassword(null);
		        serviceUtente.saveUtente(utente);
		        saveLog("cambiato la password via \"Password dimenticata\"", serviceDatiPersonali.findByAutore(utente));
		    }
		     
		    return "redirect:login";
		}
	    return "redirect:forbidden";
	}
	
	//Permission manager
	@GetMapping(value = "/permission_manager")
	public ModelAndView permissionManager() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("permissionManager");
		return mav;
	}

	// Login form
	@GetMapping(value = "/homepage")
	public ModelAndView homepageAdmin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("homepage");
		return mav;
	}

	@PostMapping(value = "/setSession")
	public String setSession(HttpSession session) {
		final String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente u  = serviceUtente.findByEmail(currentUserName);
		session.setAttribute("id", u.getId());
		System.out.println(session.getAttribute("id"));
		if(serviceDatiPersonali.findById(u.getId()).getPasswordCambiata())
			return "redirect:/homepage";
		else return "redirect:/profile/cambioPassword";
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
	
		mav.addObject("utenteDati", u);
		mav.addObject("comunicazioni", serviceComunicazioni.getAll());
		mav.setViewName("comunicazioniHr");
		return mav;
	}
	
	@GetMapping (value= "/moduli")
	public ModelAndView moduli(HttpSession session) {
		UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString()));
		ModelAndView mav = new ModelAndView();
	
		mav.addObject("user", u);
		mav.addObject("filePdf", serviceFilePdf.findAll());
		mav.setViewName("moduli");
		return mav;
	}

}
