package com.edu.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.dto.request.LessonCreateRequest;
import com.edu.dto.request.LessonLevelRequest;
import com.edu.dto.request.OptionCreateRequest;
import com.edu.dto.request.QuestionCreateRequest;
import com.edu.dto.request.QuestionEditRequest;
import com.edu.dto.response.LessonCreateResponse;
import com.edu.dto.response.LessonGenerateResponse;
import com.edu.dto.response.LessonGetResponse;
import com.edu.dto.response.LessonLevelResponse;
import com.edu.dto.response.LevelResponse;
import com.edu.dto.response.ListLessonGenerateResponse;
import com.edu.dto.response.ListLessonResponse;
import com.edu.dto.response.ListQuestionResponse;
import com.edu.dto.response.OptionResponse;
import com.edu.dto.response.QuestionResponse;
import com.edu.entity.lesson.Lesson;
import com.edu.entity.lesson.LessonLevel;
import com.edu.entity.lesson.Level;
import com.edu.entity.lesson.Option;
import com.edu.entity.lesson.Question;
import com.edu.entity.user.Lecturer;
import com.edu.repository.JpaExamRepository;
import com.edu.repository.JpaLecturerRepository;
import com.edu.repository.JpaLessonLevelRepository;
import com.edu.repository.JpaLessonRepository;
import com.edu.repository.JpaLevelRepository;
import com.edu.repository.JpaQuestionRepository;
import com.edu.utils.Constants;
import com.edu.utils.Utils;

@Service
public class LessonService {
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private JpaLecturerRepository useRepository;

	@Autowired
	private JpaLevelRepository levelRepository;

	@Autowired
	private JpaLessonRepository lessonRepository;

	@Autowired
	private JpaQuestionRepository questionRepository;

	@Autowired
	private JpaExamRepository examRepository;

	@Autowired
	private JpaLessonLevelRepository lessonLevelRepository;
	
	public LessonCreateResponse addLesson(LessonCreateRequest lessonCreateRequest) {
		// map request -> entity by same field name
		Lesson lesson = mapper.map(lessonCreateRequest, Lesson.class);
		// get lecturer from Auth
		Lecturer lecturer = useRepository.findByUsername(Utils.getUsernameFromAuthenticaiton());
		// set field else of entity
		lesson.setLecturer(lecturer);
		lesson.setRemoved(false);
		lesson.setMatrixQuestion(new ArrayList<>());
		// save db
		lesson = lessonRepository.saveAndFlush(lesson);
		// map entity -> response
		LessonCreateResponse response = mapper.map(lesson, LessonCreateResponse.class);
		return response;
	}

	public ListLessonResponse getAllLesson() {
		// get list lesson
		List<Lesson> listLesson = lessonRepository.findByRemovedFalse();
		List<LessonGetResponse> list = new ArrayList<>();
		// each lesson, do map -> response
		listLesson.forEach(lesson -> {
			// map entity -> response
			LessonGetResponse lessonGetResponse = mapLessonToLessonGetResponse(lesson);
			list.add(lessonGetResponse);
		});
		return new ListLessonResponse(list, Integer.valueOf(list.size()));
	}

	public LessonGetResponse getLessonById(Integer lessonId) {
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(lessonId).get();
		return mapLessonToLessonGetResponse(lesson);
	}

	public ListQuestionResponse getAllQuestionByLesson(Integer lessonId) {
		// get list question from db
		List<Question> listQuestion = questionRepository.findAllByLessonIdAndRemovedFalse(lessonId);
		List<QuestionResponse> listQuestionResponse = new ArrayList<>();
		// map each question -> question response
		listQuestion.forEach(question -> {
			listQuestionResponse.add(mapQuestionToQuestionResponse(question));
		});
		// get lesson from db
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(lessonId).get();
		return new ListQuestionResponse(listQuestionResponse, listQuestionResponse.size(), mapLessonToLessonGetResponse(lesson));
	}


	public LessonGetResponse addMatrixQuestion(Integer lessonId, LessonLevelRequest[] lessonLevelRequest) {
		List<LessonLevel> listLessonLevel = new ArrayList<>();
		// get lesson from db
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(lessonId).get();
		// each lesson level request, convert to lesson level entity
		for (int i = 0; i < lessonLevelRequest.length; i++) {
			Level level = levelRepository.findById(lessonLevelRequest[i].getLevelId()).get();
			listLessonLevel.add(new LessonLevel(lesson, level, lessonLevelRequest[i].getQuantity()));
		}
		lesson.setMatrixQuestion(listLessonLevel);
		// save db
		lesson = lessonRepository.saveAndFlush(lesson);
		return mapLessonToLessonGetResponse(lesson);
	}
	
	public void removeLesson(Integer lessonId) {
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(lessonId).get();
		lesson.setRemoved(true);
		lessonRepository.saveAndFlush(lesson);
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
	
	private QuestionResponse mapQuestionToQuestionResponse(Question question) {
		QuestionResponse questionResponse = mapper.map(question, QuestionResponse.class);
		LessonGetResponse lessonGetResponse = mapLessonToLessonGetResponse(question.getLesson());
		LevelResponse levelResponse = mapper.map(question.getLevel(), LevelResponse.class);
		questionResponse.setLesson(lessonGetResponse);
		questionResponse.setLevel(levelResponse);
		List<OptionResponse> list = new ArrayList<>();
		question.getListOption().forEach(option -> {
			OptionResponse optionResponse = mapper.map(option, OptionResponse.class);
			optionResponse.setQuestionId(question.getId());
			list.add(optionResponse);
		});
		questionResponse.setListOption(list);
		return questionResponse;
	}
	
	public ListLessonGenerateResponse getAllLessonForGenerateExam() {
		List<Lesson> lessons = lessonRepository.findByRemovedFalse();
		List<LessonGenerateResponse> list = new ArrayList<>();
		lessons.forEach(lesson -> {
			list.add(mapper.map(lesson, LessonGenerateResponse.class));
		});
		
		return new ListLessonGenerateResponse(list, list.size());
	}

}
