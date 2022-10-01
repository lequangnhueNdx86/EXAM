package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelResponse {
	private Integer id;
	private String name;
	private Integer score;
	private Integer time; // min
}
