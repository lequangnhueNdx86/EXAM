package com.edu.authentication;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.authentication.CustomUserDetails;
import com.edu.dto.request.RegisterRequest;
import com.edu.dto.response.RegisterResponse;
import com.edu.entity.user.Lecturer;
import com.edu.repository.JpaLecturerRepository;
import com.edu.utils.Constants;

@Service
public class UserSevice implements UserDetailsService{
	@Autowired
	private JpaLecturerRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Lecturer user = userRepository.findByUsername(username);
		if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
	}

	// JWTAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    public UserDetails loadUserById(Long id) {
        Lecturer user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new CustomUserDetails(user);
    }

}
