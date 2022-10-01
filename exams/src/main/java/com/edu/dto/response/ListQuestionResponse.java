package com.edu.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListQuestionResponse {
	private List<QuestionResponse> listQuestion;
	private Integer size;
	private LessonGetResponse lesson;
}
