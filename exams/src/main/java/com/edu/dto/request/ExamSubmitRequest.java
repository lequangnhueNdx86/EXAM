package com.edu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubmitRequest {
	private Integer examId;
	private AnswerRequest[] listAnswer;
}
