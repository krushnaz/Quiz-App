package com.quiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="admin")
public class Admins {
	@Id
	private int id;
	private String username;
	private String password;
}
