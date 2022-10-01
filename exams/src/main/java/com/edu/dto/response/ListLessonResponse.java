package com.edu.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLessonResponse {
	private List<LessonGetResponse> listLesson;
	private Integer size;
}
