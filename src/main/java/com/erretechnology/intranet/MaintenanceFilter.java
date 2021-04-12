package com.erretechnology.intranet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import com.erretechnology.intranet.models.Utente;
import com.erretechnology.intranet.repositories.RepositoryManutenzione;
import com.erretechnology.intranet.services.ServiceUtente;

/**
 * Servlet Filter implementation class MaintenanceFilter
 */
@Component
public class MaintenanceFilter implements Filter{
	RepositoryManutenzione repositoryManutenzione;
	ServiceUtente serviceUtente;

    /**
     * Default constructor. 
     */
    public MaintenanceFilter(ServiceUtente serviceUtente, RepositoryManutenzione repositoryManutenzione) {
        this.repositoryManutenzione = repositoryManutenzione;
        this.serviceUtente = serviceUtente;
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpSession session = ((HttpServletRequest) request).getSession();
		if(session.getAttribute("id") != null) {
			Utente u = serviceUtente.findById(Integer.parseInt(session.getAttribute("id").toString()));
			Boolean isAdmin  = u.getRuolo().getNome().equals("ADMIN");
		    if(repositoryManutenzione.findById(1031).get().getManutenzione() && !isAdmin) {
		    	System.out.println("pagina di manutenzione");
		    	//redirect
		    }else {
		    	chain.doFilter(request, response);
		    }
		}else {
			chain.doFilter(request, response);
		}
	    
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	            fConfig.getServletContext());
	}

}
