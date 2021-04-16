package com.erretechnology.intranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class IntranetApplication extends SpringBootServletInitializer {

	public static void main (String[] args) {
		SpringApplication.run(IntranetApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder 
	   application) {
	      return application.sources(IntranetApplication.class);
	   }
	
	

}
