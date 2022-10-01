package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponse {
	private Integer lessonNum;
	private Integer questionNum;
	private Integer examNum;
	private Integer studentNum;
	private ListExamGetResponse listExam;
	
}
