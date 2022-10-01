package com.edu.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dto.request.ExamRequest;
import com.edu.dto.request.ExamSubmitRequest;
import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.ExamDetailGetResponse;
import com.edu.dto.response.ExamGenerateResponse;
import com.edu.dto.response.ExamResultResponse;
import com.edu.dto.response.LessonGenerateResponse;
import com.edu.dto.response.ListExamGetResponse;
import com.edu.dto.response.ListLessonGenerateResponse;
import com.edu.dto.response.ListLessonResponse;
import com.edu.service.ExamService;
import com.edu.service.LessonService;

@CrossOrigin
@RestController
@RequestMapping("/api/exams")
public class ExamApiController {
	@Autowired
	private ExamService examService;
	
	@Autowired
	private LessonService lessonService;
	
	@PostMapping("/generate")
	public BaseResponse<ExamGenerateResponse> generateExam(@RequestBody ExamRequest examRequest) {
		ExamGenerateResponse examGenerateResponse = examService.generateExam(examRequest);
		return BaseResponse.ofSucceeded(examGenerateResponse);
	}
	
	@PostMapping("/submit")
	public BaseResponse<ExamResultResponse> submitExam(@RequestBody ExamSubmitRequest examSubmitRequest) {
		ExamResultResponse examResultResponse = examService.handleExam(examSubmitRequest);
		return BaseResponse.ofSucceeded(examResultResponse);
	}
	
	@GetMapping
	public BaseResponse<ListExamGetResponse> getAllExam() {
		ListExamGetResponse listExamResponse = examService.getAllExam();
		return BaseResponse.ofSucceeded(listExamResponse);
	}
	
	@GetMapping("/{exam_id}")
	public BaseResponse<ExamDetailGetResponse> getExam( @PathVariable("exam_id") Integer examId) {
		ExamDetailGetResponse examDetailGetResponse = examService.getExamById(examId);
		return BaseResponse.ofSucceeded(examDetailGetResponse);
	}
	
	@GetMapping("/lessons")
	public BaseResponse<ListLessonGenerateResponse> getAllLesson() {
		ListLessonGenerateResponse list = lessonService.getAllLessonForGenerateExam();
		return BaseResponse.ofSucceeded(list);
	}
}
