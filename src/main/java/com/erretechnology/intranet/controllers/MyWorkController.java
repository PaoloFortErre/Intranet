package com.erretechnology.intranet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "myWork")
public class MyWorkController {

	@GetMapping(value = "/")
	public ModelAndView primaPagina() {
	//	List<Post> messaggi = service.getLastMessage();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("myWorkCompleanni");
	//	mav.addObject("messaggi", servicePost.getLastMessage());
		return mav;
	}
	
}
