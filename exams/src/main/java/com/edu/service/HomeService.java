package com.edu.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.dto.response.ExamGetResponse;
import com.edu.dto.response.HomeResponse;
import com.edu.dto.response.LessonGetResponse;
import com.edu.dto.response.LessonLevelResponse;
import com.edu.dto.response.ListExamGetResponse;
import com.edu.entity.exam.Exam;
import com.edu.entity.lesson.Lesson;
import com.edu.entity.lesson.LessonLevel;
import com.edu.repository.JpaExamRepository;
import com.edu.repository.JpaLessonRepository;
import com.edu.repository.JpaQuestionRepository;

@Service
public class HomeService {
	@Autowired
	private JpaExamRepository examRepository;
	
	@Autowired
	private JpaLessonRepository lessonRepository;
	
	@Autowired
	private JpaQuestionRepository questionRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	public HomeResponse getDataForHomePoage() {
		Integer lessonNum = (int) lessonRepository.count();
		Integer questionNum = questionRepository.countByRemovedFalse();
		Integer examNum = (int) examRepository.count();
		Integer studentNum = examRepository.listDistinctStudentCompletedExam().size();
		List<Exam> listExam = examRepository.findByOrderByScoreDescAndLimitedTo(10);
		List<ExamGetResponse> list = new ArrayList<>();
		listExam.forEach(exam -> {
			list.add(mapExamToExamGetResponse(exam, mapLessonToLessonGetResponse(exam.getLesson())));
		});
		return new HomeResponse(lessonNum, questionNum, examNum, studentNum, new ListExamGetResponse(list, list.size(), new LessonGetResponse()));
	}
	
	private ExamGetResponse mapExamToExamGetResponse(Exam exam, LessonGetResponse lessonGetResponse) {
		ExamGetResponse examGetResponse = mapper.map(exam, ExamGetResponse.class);
		examGetResponse.setLesson(lessonGetResponse);
		return examGetResponse;

	}
	
	private LessonGetResponse mapLessonToLessonGetResponse(Lesson lesson) {
		// map entity -> response by same field name
		LessonGetResponse lessonGetResponse = mapper.map(lesson, LessonGetResponse.class);
		// handle set field else of response
		Integer numberOfQuestions = questionRepository.countByLessonIdAndRemovedFalse(lesson.getId());
		Integer numberOfCompletedExams = examRepository.countByLessonId(lesson.getId());
		List<LessonLevel> lessonLevels = lesson.getMatrixQuestion();

		List<LessonLevelResponse> lessonLevelResponses = new ArrayList<>();
		lessonLevels.forEach(lessonLevel -> {
			lessonLevelResponses.add(new LessonLevelResponse(lessonLevel.getLesson().getId(),
					lessonLevel.getLevel().getName(), lessonLevel.getQuantity()));
		});
		lessonGetResponse.setNumberOfQuestion(numberOfQuestions);
		lessonGetResponse.setNumberOfCompletedExams(numberOfCompletedExams);
		lessonGetResponse.setMatrixQuestion(lessonLevelResponses.toArray(new LessonLevelResponse[0]));
		return lessonGetResponse;
	}

}
