package com.edu.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.dto.request.OptionCreateRequest;
import com.edu.dto.request.OptionEditRequest;
import com.edu.dto.request.QuestionCreateRequest;
import com.edu.dto.request.QuestionEditRequest;
import com.edu.dto.response.LessonGetResponse;
import com.edu.dto.response.LessonLevelResponse;
import com.edu.dto.response.LevelResponse;
import com.edu.dto.response.ListQuestionResponse;
import com.edu.dto.response.OptionResponse;
import com.edu.dto.response.QuestionResponse;
import com.edu.entity.lesson.Lesson;
import com.edu.entity.lesson.LessonLevel;
import com.edu.entity.lesson.Option;
import com.edu.entity.lesson.Question;
import com.edu.repository.JpaExamRepository;
import com.edu.repository.JpaLessonRepository;
import com.edu.repository.JpaLevelRepository;
import com.edu.repository.JpaQuestionRepository;
import com.edu.utils.Constants;

@Service
public class QuestionService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private JpaLessonRepository lessonRepository;
	
	@Autowired
	private JpaLevelRepository levelRepository;
	
	@Autowired
	private JpaQuestionRepository questionRepository;
	
	@Autowired
	private JpaExamRepository examRepository;
	
	
	public QuestionResponse createQuestion(QuestionCreateRequest questionCreateRequest) {
		// map request -> entity by same field
		Question question = mapper.map(questionCreateRequest, Question.class);
		// handle set field else of entity
		question.setRemoved(false);
		question.setLesson(lessonRepository.findByIdAndRemovedFalse(questionCreateRequest.getLesson()).get());
		question.setLevel(levelRepository.findByIdAndRemovedFalse(questionCreateRequest.getLevel()).get());
		List<Option> listOption = new ArrayList<>();
		// if select question -> handle option
		if (questionCreateRequest.getQuestionType().equals(Constants.SELECTION)) {
			OptionCreateRequest[] optionArr = questionCreateRequest.getListOption();
			for (int i = 0; i < optionArr.length; i++) {
				Option option = mapper.map(optionArr[i], Option.class);
				option.setQuestion(question);
				listOption.add(option);
			}
			question.setListOption(listOption);	
		}
		question = questionRepository.save(question);
		return mapQuestionToQuestionResponse(question);
	}
	
	public QuestionResponse getQuestionById(Integer questionId) {
		Question question = questionRepository.findByIdAndRemovedFalse(questionId).get();
		return mapQuestionToQuestionResponse(question);
	}
	
	public void removeQuestion(Integer questionId) {
		// get question from db
		Question question = questionRepository.findByIdAndRemovedFalse(questionId).get();
		// set removed = true
		question.setRemoved(true);
		// save db
		questionRepository.save(question);
	}
	
	
	public void editQuestion(Integer questionId, QuestionEditRequest questionEditRequest) {
		Question question = questionRepository.findByIdAndRemovedFalse(questionId).get();
		question.setContent(questionEditRequest.getContent());
		question.setCorrectValue(questionEditRequest.getCorrectValue());
		if (question.getQuestionType().equals(Constants.SELECTION)) {
			OptionEditRequest[] listOption = questionEditRequest.getListOption();
			for (int i = 0; i < listOption.length; i++) {
				for (Option option : question.getListOption()) {
					if (listOption[i].getId().equals(option.getId())) {
						option.setValue(listOption[i].getValue());
					}
				}
			}
		}
		questionRepository.saveAndFlush(question);		
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

	public ListQuestionResponse getAllQuestion() {
		List<Question> listQuestion = questionRepository.findByRemovedFalse();
		List<QuestionResponse> list = new ArrayList<>();
		listQuestion.forEach((question) ->{
			list.add(mapQuestionToQuestionResponse(question));
		});
		return new ListQuestionResponse(list, Integer.valueOf(list.size()), new LessonGetResponse());
	}

}
