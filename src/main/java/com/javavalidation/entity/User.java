package com.javavalidation.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS_TBL")
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue
	private int userId;
	private String name;
	private String email;
	private String mobile;
	private int age;
	private String nationality;
}
