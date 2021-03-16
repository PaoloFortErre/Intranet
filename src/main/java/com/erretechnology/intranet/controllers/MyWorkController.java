package com.erretechnology.intranet.controllers;

import java.time.Instant;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

@Controller
@RequestMapping(value = "myWork")
public class MyWorkController {
	
	@Autowired
	ServiceUtenteDatiPersonali serviceUtenteDati;

	@GetMapping(value = "/")
	public ModelAndView primaPagina() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("myWorkCompleanni");
		long timestamp = Instant.now().getEpochSecond();
		Calendar calendar = Calendar.getInstance();
		Calendar calUtente = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp*1000);
		List<UtenteDatiPersonali> utenti = serviceUtenteDati.getAll();
		List<UtenteDatiPersonali> utentiCompleanno = new LinkedList<UtenteDatiPersonali>();
		for(UtenteDatiPersonali u : utenti) {
			calUtente.setTimeInMillis(u.getDataNascita()*1000);
			if((calendar.get(Calendar.MONTH))==(calUtente.get(Calendar.MONTH)) && (calendar.get(Calendar.DAY_OF_MONTH))==(calUtente.get(Calendar.DAY_OF_MONTH))) {
				utentiCompleanno.add(u);
			}
		}
		mav.addObject("utente", utentiCompleanno);
		return mav;
	}
	
}
