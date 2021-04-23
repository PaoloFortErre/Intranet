package com.erretechnology.intranet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import com.erretechnology.intranet.models.UtenteDatiPersonali;
import com.erretechnology.intranet.services.ServiceUtenteDatiPersonali;

public class ChangePasswordFilter extends GenericFilterBean {

	ServiceUtenteDatiPersonali serviceDatiPersonali;

	public ChangePasswordFilter(ServiceUtenteDatiPersonali serviceDatiPersonali) {
		this.serviceDatiPersonali = serviceDatiPersonali;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String changePassword = req.getContextPath() + "/profilo/cambio-password";
		if(req.getSession().getAttribute("id") != null) {
			int id_utente = Integer.parseInt(req.getSession().getAttribute("id").toString());
			UtenteDatiPersonali utenteLoggato = serviceDatiPersonali.findById(id_utente);
			if(utenteLoggato.isPasswordCambiata() || req.getRequestURI().equals(changePassword) || 
					req.getRequestURI().equals(req.getContextPath() + "/profilo/pagina-modifica-password") || 
							req.getRequestURI().contains("/view/"))
				chain.doFilter(request, response);
			else
				res.sendRedirect(changePassword);
		}else {
			chain.doFilter(request, response);
		}
		
		return;
	}

}
