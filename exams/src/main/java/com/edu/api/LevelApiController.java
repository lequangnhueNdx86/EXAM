package com.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dto.request.LevelCreateRequest;
import com.edu.dto.request.LevelEditRequest;
import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.LevelResponse;
import com.edu.dto.response.ListLevelResponse;
import com.edu.service.LevelService;

@RestController
@CrossOrigin
@RequestMapping("/api/levels")
public class LevelApiController {
	
	@Autowired
	private LevelService levelService;
	
	@GetMapping
	public BaseResponse<ListLevelResponse> getAllLevel() {
		ListLevelResponse listLevelResponse = levelService.getAllLevel();
		return BaseResponse.ofSucceeded(listLevelResponse);
	}
	
	@PostMapping
	public BaseResponse<LevelResponse> addLevel(@RequestBody LevelCreateRequest levelCreateRequest) {
		LevelResponse levelResponse = levelService.addLevel(levelCreateRequest);
		return BaseResponse.ofSucceeded(levelResponse);
	}

	@PutMapping("/{level_id}")
	public BaseResponse<LevelResponse> EditLevel(@RequestBody LevelEditRequest levelEditRequest) {
		LevelResponse levelResponse = levelService.editLevel(levelEditRequest);
		return BaseResponse.ofSucceeded(levelResponse);
	}
	
	@DeleteMapping("/{level_id}")
	public BaseResponse<Void> deleteLevel(@PathVariable("level_id") Integer levelId) {
		levelService.removeLevel(levelId);
		return BaseResponse.ofSucceeded();
	}
}
