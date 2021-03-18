package com.erretechnology.intranet.models;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;

public class Utility {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	 public static String getSiteURL(HttpServletRequest request) {
	        String siteURL = request.getRequestURL().toString();
	        return siteURL.replace(request.getServletPath(), "");
	    }
}
