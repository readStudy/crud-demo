package com.practice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practice.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	public UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userService.findByUsername(username).map(user->{
			UserDetails userDetails = User.builder()
	                .username(user.getUsername())
	                .password("{noop}"+user.getPassword())
	                .roles("USER")
	                .build();
			return userDetails;
		}).orElseThrow(()-> new AuthenticationServiceException("登入失敗"));

	}

}
