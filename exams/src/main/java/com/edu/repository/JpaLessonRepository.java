package com.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.entity.lesson.Lesson;

@Repository
public interface JpaLessonRepository extends JpaRepository<Lesson, Integer>{
//	@Query("SELECT l FROM Lesson l WHERE removed = false")
	List<Lesson> findByRemovedFalse();
	
	Optional<Lesson> findByIdAndRemovedFalse(Integer lessonId);

}
