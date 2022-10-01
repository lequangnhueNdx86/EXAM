package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonGetResponse {
	private Integer id;
	private String name;
	private Integer scorePass; //%
	private Integer numberOfQuestion;
	private Integer numberOfCompletedExams;
	private LessonLevelResponse[] matrixQuestion;
}
