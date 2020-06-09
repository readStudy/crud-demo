package com.practice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.practice.entity.User;

public interface UserService {

	List<User> findAll();

	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);

	void save(User dtoToUser);

	Optional<User> findById(Integer id);

	void deleteById(Integer id);

	Page<User> findAll(Pageable pageable);

}