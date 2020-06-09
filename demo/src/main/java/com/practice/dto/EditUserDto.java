package com.practice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EditUserDto {
	
	@NotNull
	private Integer id;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	@Email
	private String email;
	
}
