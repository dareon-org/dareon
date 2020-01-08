package org.dareon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/error", "/help", "/auth/*", "resources/**").permitAll();
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated().and()
				.formLogin().loginPage("/login").usernameParameter("email").permitAll().and().logout()
				.logoutSuccessUrl("/login?logout").permitAll();
		http.csrf().disable();
	}

	@Autowired
	private UserDetailsService userService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Remove Ant matchers for the resources folders
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/error/**");
		web.ignoring().antMatchers("/images/**");
		web.ignoring().antMatchers("/fonts/**");
		web.ignoring().antMatchers("/external/**");
	}

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").anyRequest
	 * ().authenticated().and()
	 * .formLogin().loginPage("/login").usernameParameter("email").permitAll().and()
	 * .logout() .logoutSuccessUrl("/login?logout").permitAll();
	 * http.authorizeRequests().antMatchers("/webjars/**").permitAll();
	 * http.csrf().disable(); }
	 */
}