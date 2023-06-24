package com.example.formscreator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formscreator.dto.AuthenticationRequest;
import com.example.formscreator.dto.AuthenticationResponse;
import com.example.formscreator.service.AuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@Autowired
	private AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AuthenticationRequest request) {
		logger.info("Registering user: "+request.getEmail());
	    return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
		logger.info("Logging in user: "+request.getEmail());
	    return ResponseEntity.ok(service.authenticate(request));
	}
}
