package com.practice.security.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.practice.service.UserService;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
    private UserService userService;
	
    @Override
    public Authentication authenticate(Authentication auth) 
      throws AuthenticationException {
    	
    	EmailCodeAuthenticationToken emailCodeAuthenticationToken = (EmailCodeAuthenticationToken)auth;
        UserDetails userDetails = userService.findByUsername((String)emailCodeAuthenticationToken.getPrincipal()).map(user->{
        	
        	return User.builder()
	                .username(user.getUsername())
	                .password("{noop}"+user.getPassword())
	                .roles("USER")
	                .build();
        }).orElseThrow(() ->  new InternalAuthenticationServiceException("無法獲取使用者資訊"));

        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        result.setDetails(emailCodeAuthenticationToken.getDetails());
        
        return result;
    }
 
    @Override
    public boolean supports(Class<?> auth) {
        return (EmailCodeAuthenticationToken.class
				.isAssignableFrom(auth));
    }
}