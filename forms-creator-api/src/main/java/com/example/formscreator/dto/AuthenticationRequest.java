package com.example.formscreator.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
	
	@NotBlank(message = "Email Cannot Be Empty")
	@Email(message = "Incorrect Email Format")
	private String email;

	@NotBlank(message = "Password Cannot Be Empty")
	@Size(min = 8, message = "Password Must Be More Than 8 Characters Length")
	private String password;

	public AuthenticationRequest() {
	}

	public AuthenticationRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
