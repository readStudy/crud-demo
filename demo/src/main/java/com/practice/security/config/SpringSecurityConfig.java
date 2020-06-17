package com.practice.security.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.practice.security.handler.MyAuthenctiationFailureHandler;
import com.practice.security.handler.MyLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	
	@Autowired
	private MyAuthenctiationFailureHandler myAuthenctiationFailureHandler;

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
    	    .oauth2Login();
    	;
    	
    	
        httpSecurity.authorizeRequests()
            .antMatchers("/user/login**", "/user/perform_login**", "/oauth2/my_oauth_login**").permitAll()
            .anyRequest()
            .authenticated();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(customAuthenticationProvider);
        auth.inMemoryAuthentication()
                .withUser("mu")
                    .password("{noop}ps")
                    .roles("USER");
        
        auth.userDetailsService(userDetailsService);
    }
    
    
}
