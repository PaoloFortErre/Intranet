package com.erretechnology.intranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * Pagina per avvio applicazione e builder 
 */

@SpringBootApplication
public class IntranetApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder 
	   application) {
	      return application.sources(IntranetApplication.class);
	   }
	
	
	public static void main (String[] args) {
		SpringApplication.run(IntranetApplication.class, args);
	}
	
	
	

}
