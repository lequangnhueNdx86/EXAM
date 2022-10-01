package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponse {
	private Integer id;
	private String key;
	private String value;
	private Integer questionId;
}
