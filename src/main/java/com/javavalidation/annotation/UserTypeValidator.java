package com.javavalidation.annotation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserTypeValidator implements ConstraintValidator<ValidateUserType, String>{

	@Override
	public boolean isValid(String userType, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		List<String> userTypes = Arrays.asList("admin", "vendor", "user");
		return userTypes.contains(userType.toLowerCase());
	}

}
