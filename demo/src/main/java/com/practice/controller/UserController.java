package com.practice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.dto.CreateUserDto;
import com.practice.dto.EditUserDto;
import com.practice.entity.User;
import com.practice.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value={"/", "/index"})
	public String list(Model model) {
		
		List<User> users = userService.findAll();
		
		model.addAttribute("users", users);
		
		return "index";
	}
	
	@GetMapping("/user/add")
	public String displayAddUser(Model model) {
		
		model.addAttribute("user", new CreateUserDto());
		
		return "user/add";
	}
	
	@PostMapping("/user/add")
	public String add(@Validated @ModelAttribute("user")CreateUserDto newUser, BindingResult result) {
		
		if(userService.findByUsername(newUser.getUsername()).isPresent()) {
			result.rejectValue("username", "error.username" ,newUser.getUsername() + " 已經使用，請換一個使用。");
		}
		
		if(result.hasErrors()) {
			return "user/add";
		}
		
		userService.save(CreateUserDto.dtoToUser(newUser));
		return "redirect:/";
	}
	
	@GetMapping("/user/edit")
	public String displayEditUser(Model model, @RequestParam Integer id) {
		
		userService.findById(id).ifPresent(user->{
			model.addAttribute("user", user);
		});
		
		return "user/edit";
	}
	
	@PostMapping("/user/edit")
	public String editUser(@Validated @ModelAttribute("user")EditUserDto editUser, BindingResult result) {
		
		if(result.hasErrors()) {
			return "user/edit";
		}
		
		userService.findById(editUser.getId()).ifPresent(user->{
			user.setEmail(editUser.getEmail());
			user.setPassword(editUser.getPassword());
			
			userService.save(user);
		});
		
		return "redirect:/";
	}
	
	@PostMapping("/user/delete")
	public String deleteUser(@RequestParam Integer id) {
		
		if(id == null) {
			return "redirect:/";
		}
		
		userService.deleteById(id);
		
		return "redirect:/";
	}

}
