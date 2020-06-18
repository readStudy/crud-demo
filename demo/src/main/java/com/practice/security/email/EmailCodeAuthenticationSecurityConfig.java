package com.practice.security.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class EmailCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private EmailAuthenticationProvider emailAuthenticationProvider;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        AuthenticationManager authManager = httpSecurity.getSharedObject(AuthenticationManager.class);
    	EmailCodeAuthenticationFilter emailCodeAuthenticationFilter = new EmailCodeAuthenticationFilter();
    	emailCodeAuthenticationFilter.setAuthenticationManager(authManager);
    	emailCodeAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/authentication/email", "POST"));
    	
    	httpSecurity
	        .authenticationProvider(emailAuthenticationProvider)
	        .addFilterBefore(emailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	    ;
    }
}
