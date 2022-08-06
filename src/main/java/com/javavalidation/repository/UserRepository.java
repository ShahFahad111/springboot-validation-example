package com.javavalidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javavalidation.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUserId(int id);
}
