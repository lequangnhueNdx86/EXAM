package com.edu.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGenerateResponse {
	private Integer id;
	private String content;
	private String questionType; //FILL_BLANK, SELECTION
	private LevelResponse level;
	private LessonGetResponse lesson;
	private List<OptionResponse> listOption = new ArrayList<>();
}
