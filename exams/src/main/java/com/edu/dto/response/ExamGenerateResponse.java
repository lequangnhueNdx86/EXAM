package com.edu.dto.response;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamGenerateResponse {
	private Integer id;
	private String studentCode;
	private String studentName;
	private Timestamp createTime;
	private Integer totalQuestion;
	private Integer timeExam; //min
	private Integer totalScore;
	private LessonGetResponse lesson;
	private List<QuestionGenerateResponse> listQuestion;
}
