package com.practice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.practice.security.handler.MyLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
    	httpSecurity
    	    .formLogin()
    	.and()
    	    .logout()
    	        .logoutUrl("/user/perform_logout")
    	        .invalidateHttpSession(true)
    	        .deleteCookies("JSESSIONID")
    	        .logoutSuccessHandler(myLogoutSuccessHandler);
    	
        httpSecurity.authorizeRequests()
                .antMatchers("/login").permitAll();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("mu")
                    .password("{noop}ps")
                    .roles("USER");
        
        auth.userDetailsService(userDetailsService);
    }
    
}
