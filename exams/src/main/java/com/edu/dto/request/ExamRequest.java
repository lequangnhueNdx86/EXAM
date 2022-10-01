package com.edu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamRequest {
	private String studentCode;
	private String studentName;
	private Integer lesson;
}
