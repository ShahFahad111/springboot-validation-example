package com.javavalidation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javavalidation.entity.MessageTransitionData;

public interface MessageTransitionRepo extends JpaRepository<MessageTransitionData, Integer> {

}
