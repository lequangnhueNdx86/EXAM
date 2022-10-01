package com.edu.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLevelResponse {
	private List<LevelResponse> listLevel;
	private Integer size;
}
