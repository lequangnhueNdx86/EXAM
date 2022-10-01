package com.edu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequest {
	private String content;
	private String correctValue;
	private String questionType; //FILL_BLANK || SELECTION
	private Integer level;
	private Integer lesson;
	private OptionCreateRequest[] listOption;
}
