package com.javavalidation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UserRequest {
	private String name;
	private String email;
	private String mobile;
	private int age;
	private String nationality;
}
