package com.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.authentication.CustomUserDetails;
import com.edu.authentication.JwtTokenProvider;
import com.edu.dto.request.LoginRequest;
import com.edu.dto.request.RegisterRequest;
import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.LoginResponse;
import com.edu.dto.response.RegisterResponse;
import com.edu.service.LecturerSevice;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserApiController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private LecturerSevice lecturerSevice;
	
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		// Xác thực thông tin người dùng Request lên
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
		);
     // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
	}
	
	@PostMapping("/signup")
	public BaseResponse<RegisterResponse> register(@RequestBody RegisterRequest request) {
		RegisterResponse registerResponse = lecturerSevice.regist(request);
		return BaseResponse.ofSucceeded(registerResponse);
		
	}
}
