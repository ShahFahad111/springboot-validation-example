package com.javavalidation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javavalidation.dto.UserRequest;
import com.javavalidation.entity.User;
import com.javavalidation.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@PostMapping("/signup")
	public ResponseEntity<User> saveUser(@RequestBody UserRequest userRequest){
		return new ResponseEntity<>(service.saveUser(userRequest),HttpStatus.CREATED);
	}
	
	@GetMapping("/fetchAll")
	public ResponseEntity<List<User>> getAllUsers(){
		return ResponseEntity.ok(service.getAllusers());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id){
		return ResponseEntity.ok(service.getUser(id));
	}
}
