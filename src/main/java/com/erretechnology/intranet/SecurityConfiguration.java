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

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//super.configure(auth);
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select email as username, password, attivo as enabled "
				+ "from users "
				+ "where email = ?")
		.authoritiesByUsernameQuery("SELECT email as username, permissions.nome as role "
		        +"FROM users "
		        +"INNER JOIN users_permissions ON users.id = users_permissions.id_user "
		        +"INNER JOIN permissions ON permissions.nome = users_permissions.id_permission "
		        +"WHERE users.email = ?  ");
		/*
		.authoritiesByUsernameQuery("select username, authority "
				+ "from authorities "
				+ "where username = ?");
		*/
		/*
		.withDefaultSchema()
		.withUser(
				User.withUsername("user")
				.password("pass")
				.roles("USER")
				)
		.withUser(
				User.withUsername("admin")
				.password("pass")
				.roles("ADMIN")
				);
		*/
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		http.authorizeRequests()
		.antMatchers("/admin").hasRole("ADMIN")
		.antMatchers("/user").hasAnyRole("ADMIN","USER")
		.antMatchers("/").permitAll()
		.and().formLogin()
        .loginPage("/login")
        .failureUrl("/login-error")
        .successForwardUrl("/setSession")
        .and()
        .logout()
        .logoutSuccessUrl("/user");;
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}