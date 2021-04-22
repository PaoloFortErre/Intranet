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
		.authorizeRequests()
		.antMatchers("/my-work/podcast/**", "/my-work/clienti/**", "/my-work/eventi/**", "/my-work/news/**", "/linkedin/**").access("hasAuthority('GMW')")
		.antMatchers("/my-work/comunicazioni/sondaggi/**").access("hasAuthority('GS')")
		.antMatchers("/my-life/eventi/**", "/my-life/film-serie/**", "/my-life/libri/**").access("hasAuthority('GML')")
		.antMatchers("/my-work/comunicazioni/hr/upload" , "/my-work/comunicazioni/hr" , "/my-work/comunicazioni/hr/delete").access("hasAuthority('GHR')")
		.antMatchers("/my-work/comunicazioni/moduli/aggiungi" , "/my-work/comunicazioni/moduli/upload", "/my-work/comunicazioni/moduli/delete").access("hasAuthority('GM')")
		.antMatchers("/profilo/gestisci-permesso" , "/profilo/registra" , "/profilo/cancella-utente", "/maintain-enable" ,
				"/maintain-disable").access("hasAuthority('AM')")
		.antMatchers("/my-life/**", "/profilo/**", "/homepage", "/my-work/**" , "/homepage/**", "/my-life1/**" , "/aforisma/**",
				"/linkedin/**" , "/my-work/**").access("isAuthenticated()")
		.antMatchers("/", "/login" , "/manutenzione").permitAll()
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
