package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonLevelResponse {
	private Integer lessonId;
	private String levelName;
	private Integer quantity;
}
