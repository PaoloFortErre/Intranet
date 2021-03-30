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

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username ,password, enabled from auth where username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		//TODO update e delete di tutti 
		.antMatchers("/podcast/*", "/cliente/*", "/evento/newWork/*", "/news/new/*", "/myWork/addSondaggio", "/myWork/sondaggi"
				, "/myWork/modificaSondaggio/*" , "/myWork/deleteSondaggio/*" , "/myWork/sondaggiFormUpdate/*" , "/myWork/addSondaggio").access("hasAuthority('GMW')")
		.antMatchers("/evento/newLife/", "/cinema/new/", "/libro/new").access("hasAuthority('GML')")
		.antMatchers("/file/hr" , "file/uploadHR" , "/file/deleteFileHR").access("hasAuthority('GHR')")
		.antMatchers("/file/" , "file/upload" , "/file/deleteFilePdf").access("hasAuthority('GM')")
		.antMatchers("/profile/gestisciPermesso" , "/profile/registra" , "/profile/cancellaUtente").access("hasAuthority('AM')")
		.antMatchers("/myLife/*", "/profile/*", "/homepage", "/file/*", "/myWork/*").access("isAuthenticated()")
		.antMatchers("/").permitAll()
		.and().formLogin()
		.loginPage("/login").failureUrl("/login-error").successForwardUrl("/setSession")
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
