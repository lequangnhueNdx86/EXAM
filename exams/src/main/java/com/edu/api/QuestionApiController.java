package com.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dto.request.QuestionCreateRequest;
import com.edu.dto.request.QuestionEditRequest;
import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.ListQuestionResponse;
import com.edu.dto.response.QuestionResponse;
import com.edu.service.QuestionService;

@CrossOrigin
@RestController
@RequestMapping("/api/questions")
public class QuestionApiController {
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping
	public BaseResponse<ListQuestionResponse> getAllQuestion() {
		ListQuestionResponse listQuestionResponse = questionService.getAllQuestion();
		return BaseResponse.ofSucceeded(listQuestionResponse);
	}
	
	@PostMapping
	public BaseResponse<QuestionResponse> addQuestion(@RequestBody QuestionCreateRequest questionCreateRequest) {
		QuestionResponse questionResponse = questionService.createQuestion(questionCreateRequest);
		return BaseResponse.ofSucceeded(questionResponse);
	}
	
	@GetMapping("/{question_id}")
	public BaseResponse<QuestionResponse> getQuestion(@PathVariable("question_id") Integer questionId) {
		QuestionResponse questionResponse = questionService.getQuestionById(questionId);
		return BaseResponse.ofSucceeded(questionResponse);

	}
	
	@DeleteMapping("/{question_id}")
	public BaseResponse<Void> deleleQuestion(@PathVariable("question_id") Integer questionId) {
		questionService.removeQuestion(questionId);
		return BaseResponse.ofSucceeded();
	}
	
	@PutMapping("/{question_id}")
	public BaseResponse<Void> editQuestion(@PathVariable("question_id") Integer questionId, @RequestBody QuestionEditRequest questionEditRequest) {
		questionService.editQuestion(questionId, questionEditRequest);
		return BaseResponse.ofSucceeded();
	}
}
