package com.edu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
	public static final Integer OK_CODE = 200;

	private T data;
	private Integer statusCode;
	private String message;

	public static <T> BaseResponse<T> ofSucceeded(T data) {
		BaseResponse<T> response = new BaseResponse<>();
		response.data = data;
		response.statusCode = OK_CODE;
		response.message = "OK";
		return response;
	}
	
	public static <T> BaseResponse<T> ofSucceeded() {
        BaseResponse<T> response = new BaseResponse<>();
        response.statusCode = OK_CODE;
        return response;
    }
}
