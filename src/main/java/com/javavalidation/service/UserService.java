package com.javavalidation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javavalidation.dto.UserRequest;
import com.javavalidation.entity.User;
import com.javavalidation.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository; 
	
	public User saveUser(UserRequest userRequest) {
		User user = User.build(0, userRequest.getName(), userRequest.getEmail(), userRequest.getMobile(), userRequest.getAge(), userRequest.getNationality()); 
		return userRepository.save(user);
	}
	
	public List<User> getAllusers(){
		 return userRepository.findAll();
	}
	
	public User getUser(int id) {
		return userRepository.findByUserId(id);
	}
}
 