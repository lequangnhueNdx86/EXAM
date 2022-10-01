package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerGetResponse {
	private Integer id;
	private String answer;
	private boolean result;
	private QuestionResponse question;
	private Integer examId;
}
