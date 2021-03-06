package com.practice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.practice.entity.User;

import lombok.Data;

@Data
public class UserDto {
	
	public interface NewUser{}
	
	public interface EditUser{}

	@NotNull(groups = {EditUser.class})
	private Integer id;
	@NotBlank(groups = {EditUser.class, NewUser.class})
	private String username;
	@NotBlank(groups = {EditUser.class, NewUser.class})
	@Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30}$",message="密碼為至少一大寫字母，至少一小寫字母，至少一數字，全長最少八碼", groups = {EditUser.class, NewUser.class}) 
	private String password;
	@NotBlank(groups = {EditUser.class, NewUser.class})
	@Email(groups = {EditUser.class, NewUser.class})
	private String email;
	
	
	public static User dtoToUser(UserDto dto) {
		User newUser = new User();
		newUser.setEmail(dto.getEmail());
		newUser.setPassword(dto.getPassword());
		newUser.setUsername(dto.getUsername());
		
		return newUser;
	}
	
}
