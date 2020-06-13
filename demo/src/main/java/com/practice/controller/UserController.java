package com.practice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.common.NumberUtils;
import com.practice.dto.UserDto;
import com.practice.entity.User;
import com.practice.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value={"/", "/index"})
	public String index(Model model) {
		
		List<User> users = userService.findAll();
		
		model.addAttribute("users", users);
		
		return "index";
	}
	
	@GetMapping("/list")
	public String list(Model model,
			@RequestParam(name = "page", defaultValue = "1") Integer currentPage, 
		    @RequestParam(name = "size", defaultValue = "2") Integer size,
		    @RequestParam(name = "searchBy", defaultValue = "") String searchBy,
		    @RequestParam(name = "searchValue", defaultValue = "") String searchValue) {
		
		if(size > 5 || size < 1) { size=5; }
		
		if("id".equals(searchBy) && !searchValue.isEmpty()) {
			if(!NumberUtils.isNumeric(searchValue)) {
				searchValue = "1";
			}
		}
		
		Page<User> users = userService.findAllBy(PageRequest.of(currentPage - 1, size), searchBy, searchValue);
		
		model.addAttribute("users", users);
		
		model.addAttribute("searchBy", searchBy);
		model.addAttribute("searchValue", searchValue);
		
		return "user/list";
	}
	
	@GetMapping("/user/add")
	public String displayAddUser(Model model) {
		
		model.addAttribute("user", new UserDto());
		
		return "user/add";
	}
	
	@PostMapping("/user/add")
	public String add(@Validated @ModelAttribute("user")UserDto newUser, BindingResult result, RedirectAttributes redirectAttributes) {
		
		if(userService.findByUsername(newUser.getUsername()).isPresent()) {
			result.rejectValue("username", "error.username" ,newUser.getUsername() + " 已經使用，請換一個使用。");
		}
		
		if(result.hasErrors()) {
			return "user/add";
		}
		
		if(userService.save(UserDto.dtoToUser(newUser)) != null) {
			redirectAttributes.addFlashAttribute("message", "添加使用者成功");
		}
		
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
	public String editUser(@Validated({UserDto.EditUser.class}) @ModelAttribute("user")UserDto editUser, BindingResult result, RedirectAttributes redirectAttributes) {
		
		if(result.hasErrors()) {
			return "user/edit";
		}
		userService.update(editUser);
		redirectAttributes.addFlashAttribute("message", "修改使用者成功");
		
		return "redirect:/";
	}
	
	@ResponseBody
	@PostMapping("/user/delete")
	public Map<String, String> deleteUser(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		
		Map<String, String> map = new HashMap<>();
		
		if(id == null) {
			map.put("message", "刪除使用者失敗");
			return map;
		}
		
		userService.deleteById(id);
		
		map.put("message", "刪除使用者成功");
		
		return map;
	}

}
