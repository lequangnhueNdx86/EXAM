package com.edu.dto.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamGetResponse {
	private Integer id;
	private String studentCode;
	private String studentName;
	private Timestamp createTime;
	private Integer totalQuestion;
	private Integer correctQuestion;
	private Integer timeExam; //min
	private Double timeComplete; //min
	private Integer totalScore;
	private Integer score;
	private String result; // PASS, FAIL
	private LessonGetResponse lesson;
}
