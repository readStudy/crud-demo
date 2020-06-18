package com.practice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.practice.security.email.EmailCodeAuthenticationSecurityConfig;
import com.practice.security.handler.MyAuthenctiationFailureHandler;
import com.practice.security.handler.MyLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	
	@Autowired
	private MyAuthenctiationFailureHandler myAuthenctiationFailureHandler;
	
	@Autowired
	private EmailCodeAuthenticationSecurityConfig emailCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
    	httpSecurity
    	    .formLogin()
    	        .loginPage("/user/login")
    	        .loginProcessingUrl("/user/perform_login")
    	        .failureUrl("/user/login?error")
    	        .failureHandler(myAuthenctiationFailureHandler)
    	.and()
    	    .logout()
    	        .logoutUrl("/user/perform_logout")
    	        .invalidateHttpSession(true)
    	        .deleteCookies("JSESSIONID")
    	        .logoutSuccessHandler(myLogoutSuccessHandler)
    	.and()
    	    .oauth2Login()
    	;
    	
    	
        httpSecurity.authorizeRequests()
            .antMatchers("/user/login","/user/login/email", "/user/perform_login", "/oauth2/my_oauth_login", "/authentication/*", "/user/emailcode").permitAll()
            .anyRequest()
            .authenticated();
        
        httpSecurity.apply(emailCodeAuthenticationSecurityConfig);
    	
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
