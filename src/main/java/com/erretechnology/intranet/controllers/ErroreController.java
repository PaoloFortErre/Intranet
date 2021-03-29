package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErroreController implements ErrorController {
	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");
        int httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");

        errorPage.addObject("message", "Errore " + httpErrorCode);
        return errorPage;
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}