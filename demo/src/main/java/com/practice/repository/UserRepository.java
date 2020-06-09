package com.practice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.practice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
	
	Page<User> findAll(Pageable pageable);
	
}
