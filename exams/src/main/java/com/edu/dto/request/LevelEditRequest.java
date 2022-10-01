package com.edu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelEditRequest {
	private Integer id;
	private String name;
	private Integer score;
	private Integer time; //min
}
