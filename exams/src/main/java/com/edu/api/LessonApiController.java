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

import com.edu.dto.request.LessonCreateRequest;
import com.edu.dto.request.LessonLevelRequest;
import com.edu.dto.request.QuestionCreateRequest;
import com.edu.dto.request.QuestionEditRequest;
import com.edu.dto.response.BaseResponse;
import com.edu.dto.response.ExamDetailGetResponse;
import com.edu.dto.response.LessonCreateResponse;
import com.edu.dto.response.LessonGetResponse;
import com.edu.dto.response.ListExamGetResponse;
import com.edu.dto.response.ListLessonResponse;
import com.edu.dto.response.ListQuestionResponse;
import com.edu.dto.response.QuestionResponse;
import com.edu.service.ExamService;
import com.edu.service.LessonService;

@RestController
@CrossOrigin
@RequestMapping("/api/lessons")
public class LessonApiController {
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private ExamService examService;

	@PostMapping
	public BaseResponse<LessonCreateResponse> addLesson(@RequestBody LessonCreateRequest lessonCreateRequest) {
		LessonCreateResponse lessonCreateResponse = lessonService.addLesson(lessonCreateRequest);
		return BaseResponse.ofSucceeded(lessonCreateResponse);
	}
	
	@PostMapping("/{lesson_id}/matrix")
	public BaseResponse<LessonGetResponse> addMatrixQuestion(@PathVariable("lesson_id") Integer lessonId, @RequestBody LessonLevelRequest[] lessonLevelRequest) {
		LessonGetResponse lessonGetResponse = lessonService.addMatrixQuestion(lessonId, lessonLevelRequest);
		return BaseResponse.ofSucceeded(lessonGetResponse);
	}

	@GetMapping
	public BaseResponse<ListLessonResponse> getAllLesson() {
		ListLessonResponse listLessonResponse = lessonService.getAllLesson();
		return BaseResponse.ofSucceeded(listLessonResponse);
	}

	@GetMapping("/{lesson_id}")
	public BaseResponse<LessonGetResponse> getLesson(@PathVariable("lesson_id") Integer lessonId) {
		LessonGetResponse lesson = lessonService.getLessonById(lessonId);
		return BaseResponse.ofSucceeded(lesson);
	}

	@GetMapping("/{lesson_id}/questions")
	public BaseResponse<ListQuestionResponse> getAllQuestionByLesson(@PathVariable("lesson_id") Integer lessonId) {
		ListQuestionResponse listQuestionResponse = lessonService.getAllQuestionByLesson(lessonId);
		return BaseResponse.ofSucceeded(listQuestionResponse);
	}

	@GetMapping("/{lesson_id}/exams")
	public BaseResponse<ListExamGetResponse> getAllExamByLesson(@PathVariable("lesson_id") Integer lessonId) {
		ListExamGetResponse listExamGetResponse = examService.getAllExamByLesson(lessonId);
		return BaseResponse.ofSucceeded(listExamGetResponse);
	}

	@DeleteMapping("/{lesson_id}")
	public BaseResponse<Void> removeLesson(@PathVariable("lesson_id") Integer lessonId) {
		lessonService.removeLesson(lessonId);
		return BaseResponse.ofSucceeded();
	}

}
