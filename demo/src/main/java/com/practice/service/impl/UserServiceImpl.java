package com.practice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.practice.dto.UserDto;
import com.practice.entity.User;
import com.practice.repository.UserRepository;
import com.practice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
		
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Page<User> findAllBy(Pageable pageable, String searchBy, String searchValue) {
		
		Specification<User> specification = (root, query, cb) -> {

			if (StringUtils.isEmpty(searchBy) || StringUtils.isEmpty(searchValue)) {
				return null;
			}

			List<Predicate> predicatesList = new ArrayList<>();
			if (searchBy.equalsIgnoreCase("username")) {
				Predicate namePredicate = cb.like(cb.lower(root.get("username")),
						"%" + searchValue.toLowerCase() + "%");
				predicatesList.add(namePredicate);
			}

			if (searchBy.equalsIgnoreCase("id")) {
				Predicate idPredicate = cb.equal(root.get("id"), searchValue);
				predicatesList.add(idPredicate);
			}
			Predicate[] predicates = new Predicate[predicatesList.size()];
			return cb.and(predicatesList.toArray(predicates));
		};

		Page<User> users = userRepository.findAll(specification, pageable);
		
		return users;
	}

	@Transactional
	@Override
	public Optional<User> update(UserDto editUser) {
		userRepository.findById(editUser.getId()).map(user->{
			user.setEmail(editUser.getEmail());
			user.setPassword(editUser.getPassword());
			
			return Optional.ofNullable(userRepository.save(user));
		});
		return Optional.empty();
	}

}
