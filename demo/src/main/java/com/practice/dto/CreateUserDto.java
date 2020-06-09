package com.practice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.practice.entity.User;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class CreateUserDto {
	
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	@Email
	private String email;
	
	public static User dtoToUser(CreateUserDto dto) {
		User newUser = new User();
		newUser.setEmail(dto.getEmail());
		newUser.setPassword(dto.getPassword());
		newUser.setUsername(dto.getUsername());
		
		return newUser;
	}
}
