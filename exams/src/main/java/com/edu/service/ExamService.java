package com.edu.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.dto.request.AnswerRequest;
import com.edu.dto.request.ExamRequest;
import com.edu.dto.request.ExamSubmitRequest;
import com.edu.dto.response.AnswerGetResponse;
import com.edu.dto.response.ExamDetailGetResponse;
import com.edu.dto.response.ExamGenerateResponse;
import com.edu.dto.response.ExamGetResponse;
import com.edu.dto.response.ExamResultResponse;
import com.edu.dto.response.LessonGetResponse;
import com.edu.dto.response.LessonLevelResponse;
import com.edu.dto.response.LevelResponse;
import com.edu.dto.response.ListExamGetResponse;
import com.edu.dto.response.ListLessonResponse;
import com.edu.dto.response.OptionResponse;
import com.edu.dto.response.QuestionGenerateResponse;
import com.edu.dto.response.QuestionResponse;
import com.edu.entity.exam.Answer;
import com.edu.entity.exam.Exam;
import com.edu.entity.lesson.Lesson;
import com.edu.entity.lesson.LessonLevel;
import com.edu.entity.lesson.Level;
import com.edu.entity.lesson.Question;
import com.edu.repository.JpaExamRepository;
import com.edu.repository.JpaLessonRepository;
import com.edu.repository.JpaQuestionRepository;
import com.edu.utils.Constants;

@Service
public class ExamService {
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private JpaExamRepository examRepository;

	@Autowired
	private JpaLessonRepository lessonRepository;

	@Autowired
	private JpaQuestionRepository questionRepository;

	public ListExamGetResponse getAllExamByLesson(Integer lessonId) {
		// get list exam by lesson id from db
		List<Exam> listExam = examRepository.findAllByLessonId(lessonId);
		// get lesson from db
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(lessonId).get();
		// map lesson -> lesson get response
		LessonGetResponse lessonGetResponse = mapLessonToLessonGetResponse(lesson);
		List<ExamGetResponse> listExamGetResponses = new ArrayList<>();
		// each exam, map exam -> exam get response
		listExam.forEach(exam -> {
			listExamGetResponses.add(mapExamToExamGetResponse(exam, lessonGetResponse));
		});
		return new ListExamGetResponse(listExamGetResponses, listExamGetResponses.size(), lessonGetResponse);
	}

	public ExamDetailGetResponse getExamById(Integer examId) {
		Exam exam = examRepository.findById(examId).get();
		// map exam -> exam detail by name & type
		ExamDetailGetResponse examDetailGetResponse = mapper.map(exam, ExamDetailGetResponse.class);
		examDetailGetResponse.setLesson(mapLessonToLessonGetResponse(exam.getLesson()));
		List<Answer> listAnswer = exam.getListAnswer();
		List<AnswerGetResponse> answerGetResponses = new ArrayList<>();
		listAnswer.forEach(answer -> {
			answerGetResponses.add(mapAnswerToAnswerGetResponse(answer));
		});
		examDetailGetResponse.setListAnswer(answerGetResponses);
		return examDetailGetResponse;
	}

	public ExamGenerateResponse generateExam(ExamRequest examRequest) {
		Exam exam = mapper.map(examRequest, Exam.class);
		Lesson lesson = lessonRepository.findByIdAndRemovedFalse(examRequest.getLesson()).get();
		// generate exam

		// totalQuestion
		Integer totalQuestion = getTotalQuestion(lesson);
		// totalTime
		Integer timeExam = getTotalTimeExam(lesson);
		// total score
		Integer totalScore = getTotalScore(lesson);
		// random question & set answer false
		List<Answer> listAnswer = randomQuestionAndSetAnswerFalse(lesson, exam);

		exam.setTotalQuestion(totalQuestion);
		exam.setCorrectQuestion(0);
		exam.setTimeExam(timeExam);
		exam.setTimeComplete(0.0);
		exam.setTotalScore(totalScore);
		exam.setScore(0);
		exam.setResult(Constants.FAIL);
		exam.setLesson(lesson);
		exam.setListAnswer(listAnswer);
		exam = examRepository.saveAndFlush(exam);
		return mapExamToExamGenerateResponse(exam);
	}

	public ExamResultResponse handleExam(ExamSubmitRequest examSubmitRequest) {
		Exam exam = examRepository.findById(examSubmitRequest.getExamId()).get();
		long timeout = 5000; // ms

		Timestamp submitTime = new Timestamp(new Date().getTime());
		// if submit time in allowed time
		if (submitTime
				.before(new Timestamp(exam.getCreateTime().getTime() + exam.getTimeExam() * 60 * 1000 + timeout))) {
			List<Answer> listAnswer = exam.getListAnswer();
			AnswerRequest[] listAnswerRequest = examSubmitRequest.getListAnswer();
			// check answer and handle result of each question
			for (int i = 0; i < listAnswerRequest.length; i++) {
				for (Answer answer : listAnswer) {
					if (answer.getQuestion().getId().equals(listAnswerRequest[i].getQuestionId())) {
						answer.setAnswer(listAnswerRequest[i].getAnswer());
						if (answer.getAnswer().equals(answer.getQuestion().getCorrectValue())) {
							answer.setResult(true);
						}
					}
				}
			}
			exam.setListAnswer(listAnswer);
			// handle score
			Integer score = handleGetScoreOfExam(exam);
			exam.setScore(score);
			// handle time completed
			Double timeComplete = (double) ((submitTime.getTime() - exam.getCreateTime().getTime()) / (1000 * 60));
			exam.setTimeComplete(timeComplete);
			// handle correct question
			Integer correctQuestion = handleGetCorrectQuestion(exam);
			exam.setCorrectQuestion(correctQuestion);
			// handle result of exam
			if (score >= exam.getTotalScore() * exam.getLesson().getScorePass() / 100) {
				exam.setResult(Constants.PASS);
			}
			exam = examRepository.saveAndFlush(exam);
		}
		ExamResultResponse examResultResponse = mapper.map(exam, ExamResultResponse.class);
		examResultResponse.setLessonName(exam.getLesson().getName());
		return examResultResponse;
	}

	private Integer handleGetCorrectQuestion(Exam exam) {
		int num = 0;
		List<Answer> listAnswer = exam.getListAnswer();
		for (Answer answer : listAnswer) {
			if (answer.isResult() == true) {
				num++;
			}
		}
		return Integer.valueOf(num);
	}

	private Integer handleGetScoreOfExam(Exam exam) {
		int score = 0;
		List<Answer> listAnswer = exam.getListAnswer();
		for (Answer answer : listAnswer) {
			if (answer.isResult() == true) {
				score += answer.getQuestion().getLevel().getScore();
			}
		}
		return Integer.valueOf(score);
	}

	private List<Answer> randomQuestionAndSetAnswerFalse(Lesson lesson, Exam exam) {
		List<Answer> listAnswer = new ArrayList<>();
		List<LessonLevel> matrixQuestion = lesson.getMatrixQuestion();
		for (LessonLevel lessonLevel : matrixQuestion) {
			Level level = lessonLevel.getLevel();
			List<Question> listQuestion = questionRepository.findAllByLessonIdAndLevelIdAndRemovedFalse(lesson.getId(),
					level.getId());
			Collections.shuffle(listQuestion); // shuffle list
			// get question from shuffle question list
			if (listQuestion.size() >= lessonLevel.getQuantity()) {
				for (int i = 0; i < lessonLevel.getQuantity(); i++) {
					Answer answer = new Answer();
					answer.setAnswer("");
					answer.setResult(false);
					answer.setExam(exam);
					answer.setQuestion(listQuestion.get(i));
					listAnswer.add(answer);
				}
			}
		}
		return listAnswer;
	}

	private Integer getTotalScore(Lesson lesson) {
		List<LessonLevel> matrixQuestion = lesson.getMatrixQuestion();
		int totalScore = 0;
		for (LessonLevel lessonLevel : matrixQuestion) {
			totalScore += lessonLevel.getQuantity() * lessonLevel.getLevel().getScore();
		}
		return Integer.valueOf(totalScore);
	}

	private Integer getTotalTimeExam(Lesson lesson) {
		List<LessonLevel> matrixQuestion = lesson.getMatrixQuestion();
		int timeExam = 0;
		for (LessonLevel lessonLevel : matrixQuestion) {
			timeExam += lessonLevel.getQuantity() * lessonLevel.getLevel().getTime();
		}
		return Integer.valueOf(timeExam);
	}

	private Integer getTotalQuestion(Lesson lesson) {
		List<LessonLevel> matrixQuestion = lesson.getMatrixQuestion();
		int total = 0;
		for (LessonLevel lessonLevel : matrixQuestion) {
			total += lessonLevel.getQuantity();
		}
		return Integer.valueOf(total);
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

	private AnswerGetResponse mapAnswerToAnswerGetResponse(Answer answer) {
		AnswerGetResponse answerGetResponse = mapper.map(answer, AnswerGetResponse.class);
		answerGetResponse.setExamId(answer.getExam().getId());
		answerGetResponse.setQuestion(mapQuestionToQuestionResponse(answer.getQuestion()));
		return answerGetResponse;
	}

	private ExamGenerateResponse mapExamToExamGenerateResponse(Exam exam) {
		ExamGenerateResponse examGenerateResponse = mapper.map(exam, ExamGenerateResponse.class);
		examGenerateResponse.setLesson(mapLessonToLessonGetResponse(exam.getLesson()));
		examGenerateResponse.setListQuestion(mapListAnswerToQuestionGenerateResponses(exam.getListAnswer()));
		return examGenerateResponse;
	}

	private List<QuestionGenerateResponse> mapListAnswerToQuestionGenerateResponses(List<Answer> listAnswer) {
		List<QuestionGenerateResponse> list = new ArrayList<>();
		for (Answer answer : listAnswer) {
			list.add(mapQuestionToQuestionGenerateResponse(answer.getQuestion()));
		}
		return list;
	}

	private QuestionGenerateResponse mapQuestionToQuestionGenerateResponse(Question question) {
		QuestionGenerateResponse questionGenerateResponse = mapper.map(question, QuestionGenerateResponse.class);
		questionGenerateResponse.setLesson(mapLessonToLessonGetResponse(question.getLesson()));
		List<OptionResponse> list = new ArrayList<>();
		if (question.getQuestionType().equals(Constants.SELECTION)) {
			question.getListOption().forEach(option -> {
				OptionResponse optionResponse = mapper.map(option, OptionResponse.class);
				optionResponse.setQuestionId(question.getId());
				list.add(optionResponse);
			});
		}
		questionGenerateResponse.setListOption(list);
		return questionGenerateResponse;
	}

	public ListExamGetResponse getAllExam() {
		List<Exam> listExam = examRepository.findByOrderByCreateTimeDesc();
		List<ExamGetResponse> list = new ArrayList<>();
		listExam.forEach(exam -> {	
			if (exam.getLesson().getRemoved() == false) {
				list.add(mapExamToExamGetResponse(exam, mapLessonToLessonGetResponse(exam.getLesson())));
			}
		});
		return new ListExamGetResponse(list, Integer.valueOf(list.size()), new LessonGetResponse());
	}


}
