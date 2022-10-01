package com.edu.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.dto.request.LevelCreateRequest;
import com.edu.dto.request.LevelEditRequest;
import com.edu.dto.response.LevelResponse;
import com.edu.dto.response.ListLevelResponse;
import com.edu.entity.lesson.Level;
import com.edu.repository.JpaLevelRepository;

@Service
public class LevelService {
	@Autowired
	private JpaLevelRepository levelRepository;
	
	@Autowired
	private ModelMapper mapper;

	public ListLevelResponse getAllLevel() {
		// get list level from db
		List<Level> listLevel = levelRepository.findByRemovedFalse();
		List<LevelResponse> levelResponses = new ArrayList<>();
		// map each level -> level response
		listLevel.forEach(level -> {
			levelResponses.add(mapper.map(level, LevelResponse.class));
		});
		return new ListLevelResponse(levelResponses, levelResponses.size());
	}

	public LevelResponse addLevel(LevelCreateRequest levelCreateRequest) {
		Level level = mapper.map(levelCreateRequest, Level.class);
		level.setRemoved(false);
		level = levelRepository.save(level);
		return mapper.map(level, LevelResponse.class);
	}

	public void removeLevel(Integer levelId) {
		Level level = levelRepository.findByIdAndRemovedFalse(levelId).get();
		level.setRemoved(true);
		levelRepository.save(level);
	}

	// check later
	public LevelResponse editLevel(LevelEditRequest levelRequest) {
		Level level = mapper.map(levelRequest, Level.class);
		level.setRemoved(false);
		level = levelRepository.saveAndFlush(level);
		return mapper.map(level, LevelResponse.class);
	}

}
