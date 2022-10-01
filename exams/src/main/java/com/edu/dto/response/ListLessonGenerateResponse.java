package com.edu.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLessonGenerateResponse {
	private List<LessonGenerateResponse> listLesson;
	private Integer size;
}
