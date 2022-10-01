package com.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.entity.lesson.Question;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, Integer>{
	@Query("SELECT COUNT(q) FROM Question q WHERE q.lesson.id = ?1 AND q.removed = false")
	Integer countByLessonIdAndRemovedFalse(Integer lessonId);
	
	List<Question> findAllByLessonIdAndRemovedFalse(Integer lessonId);
	
	Optional<Question> findByIdAndRemovedFalse(Integer questionId);
	
	List<Question> findAllByLessonIdAndLevelIdAndRemovedFalse(Integer lessonId, Integer LevelId);
	
	List<Question> findByRemovedFalse();
	
	Integer countByRemovedFalse();

}
