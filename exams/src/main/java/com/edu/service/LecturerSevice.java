package com.edu.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.dto.request.RegisterRequest;
import com.edu.dto.response.RegisterResponse;
import com.edu.entity.user.Lecturer;
import com.edu.repository.JpaLecturerRepository;
import com.edu.utils.Constants;

@Service
public class LecturerSevice{
	@Autowired
	private JpaLecturerRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public RegisterResponse regist(RegisterRequest request) {
		Lecturer lecturer = new Lecturer();
		lecturer.setRole(Constants.ROLE_ADMIN);
		lecturer.setUsername(request.getUsername());
		lecturer.setPassword(passwordEncoder.encode(request.getPassword()));
		lecturer = userRepository.save(lecturer);
		return new RegisterResponse(lecturer.getUsername(), lecturer.getPassword());
	}
	

}
