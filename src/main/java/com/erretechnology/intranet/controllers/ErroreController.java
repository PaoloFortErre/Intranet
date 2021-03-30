package com.erretechnology.intranet.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.erretechnology.intranet.models.UtenteDatiPersonali;

@Controller
public class ErroreController extends BaseController implements ErrorController {
	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest httpRequest, HttpSession session) throws Exception {
		ModelAndView errorPage = new ModelAndView();
        //System.err.println("error");
        String errorMsg = "";
        Integer httpErrorCode;
		if(httpRequest.getAttribute("javax.servlet.error.status_code") != null) {
			httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
			if( httpErrorCode == 404)
				errorPage.setViewName("error");
			else 
				errorPage.setViewName("erroreGenerico");
		}else {
        	httpErrorCode = 403;
        	errorPage.setViewName("erroreGenerico");
        	UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())); 
        }
        errorPage.addObject("errorNumber", httpErrorCode);
        return errorPage;
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}