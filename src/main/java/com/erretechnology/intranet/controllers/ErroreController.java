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
	public ModelAndView handleError(HttpServletRequest httpRequest, HttpSession session) {
        ModelAndView errorPage = new ModelAndView("error");
        //System.err.println("error");
        String errorMsg = "";
        int httpErrorCode = -1;
        if(httpRequest.getAttribute("javax.servlet.error.status_code") != null)
        	 httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        else {
        	UtenteDatiPersonali u  = serviceDatiPersonali.findById(Integer.parseInt(session.getAttribute("id").toString())); 
        	errorMsg = "Tentativo di accedere ad una pagina senza i permessi necessari da parte di " + u.getNome() + " " + u.getCognome();
        }
        	  
        switch (httpErrorCode) {
		    case 400: {
		        errorMsg = "Http Error Code: 400. Bad Request";
		        break;
		    }
		    case 404: {
		        errorMsg = "Http Error Code: 404. Resource not found";
		        break;
		    }
		    case 500: {
		        errorMsg = "Http Error Code: 500. Internal Server Error";
		        break;
		    }
		}
        errorPage.addObject("message", errorMsg);
        return errorPage;
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}