package com.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.HomeResponse;
import com.edu.service.HomeService;

@CrossOrigin
@RestController
@RequestMapping("/api/home")
public class HomeApiController {
	@Autowired
	private HomeService homeService;
	
	@GetMapping
	public BaseResponse<HomeResponse> loadHome() {
		HomeResponse homeResponse = homeService.getDataForHomePoage();
		return BaseResponse.ofSucceeded(homeResponse);
	}
}
