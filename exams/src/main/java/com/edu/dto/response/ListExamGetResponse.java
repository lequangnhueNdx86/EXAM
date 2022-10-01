package com.edu.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListExamGetResponse {
	private List<ExamGetResponse> listExam;
	private Integer size;
	private LessonGetResponse lessonGetResponse;
}
