package com.javavalidation.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.javavalidation.annotation.ValidateUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UserRequest {
	
	@NotNull(message = "User Name should not be null")
	private String name;
	
	@Email(message = "Invalid email Address")
	private String email;
	
	@Pattern(regexp = "^\\d{10}$", message = "Invalid Pattern")
	private String mobile;
	
	@Max(60)
	@Min(18)
	private int age;
	
	@NotBlank
	private String nationality;
	
	@ValidateUserType(message = "Please provide correct user type")
	private String userType; // admin, vendor, user
}
