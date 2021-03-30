package com.erretechnology.intranet.controllers;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import com.erretechnology.intranet.models.ErrorLog;
import com.erretechnology.intranet.repositories.RepositoryErrorLog;

@ControllerAdvice
class ExceptionController {
	@Autowired
	private RepositoryErrorLog repoErrorLog;

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest httpRequest, Exception e) throws Exception {
		ErrorLog log = new ErrorLog();
		log.setTimestamp(Instant.now().getEpochSecond());
		log.setErrore(e.toString());
		e.printStackTrace();
		repoErrorLog.save(log);
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "Internal error");
		mav.setViewName("error");

		return mav;
	}
}