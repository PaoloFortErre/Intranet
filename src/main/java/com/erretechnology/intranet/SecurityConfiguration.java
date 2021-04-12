package com.erretechnology.intranet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.erretechnology.intranet.repositories.RepositoryManutenzione;
import com.erretechnology.intranet.services.ServiceUtente;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	@Autowired
	ServiceUtente serviceUtente;
	@Autowired
	RepositoryManutenzione repositoryManutenzione;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username ,password, enabled from auth where username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.formLogin()
		.loginPage("/login").failureUrl("/login-error").successForwardUrl("/setSession").and()
		.addFilterAfter(new MaintenanceFilter(serviceUtente, repositoryManutenzione), BasicAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers("/podcast/*", "/cliente/*", "/evento/newWork/*", "/news/new/*").access("hasAuthority('GMW')")
		.antMatchers("/myWork/addSondaggio", "/myWork/sondaggi", "/myWork/modificaSondaggio/*" , 
				     "/myWork/deleteSondaggio/*" , "/myWork/sondaggiFormUpdate/*").access("hasAuthority('GS')")
		.antMatchers("/evento/newLife/", "/cinema/new/", "/libro/new").access("hasAuthority('GML')")
		.antMatchers("/file/hr" , "file/uploadHR" , "/file/deleteFileHR").access("hasAuthority('GHR')")
		.antMatchers("/file/" , "file/upload" , "/file/deleteFilePdf").access("hasAuthority('GM')")
		.antMatchers("/profile/gestisciPermesso" , "/profile/registra" , "/profile/cancellaUtente", "/maintain-enable" ,
				"/maintain-disable").access("hasAuthority('AM')")
		.antMatchers("/myLife/*", "/profile/*", "/homepage", "/file/*", "/myWork/*" , "/homepage/*", "/myLife1/*" , "/moduli/*", "/aforisma/*",
				"/cinema/*","/cliente/*", "/eventolife/*", "/eventowork/*", "/categoriacinema/*", "/libro/*", "/linkedin/*" , "/news/*",
				"/podcast/*").access("isAuthenticated()")
		.antMatchers("/", "/login").permitAll()
		.antMatchers("/maintain-enable", "/maintain-disable").access("isAuthenticated")
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
		.and()
		.exceptionHandling().accessDeniedPage("/error")
		;
		http.csrf().disable();

	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
