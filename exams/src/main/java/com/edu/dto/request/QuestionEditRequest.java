package com.edu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEditRequest {
	private Integer id;
	private String content;
	private String correctValue;
	private OptionEditRequest[] listOption;
}
