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
        String errorMsg = "";
        int httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
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