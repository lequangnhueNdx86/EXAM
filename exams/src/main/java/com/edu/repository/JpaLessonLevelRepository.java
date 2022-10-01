package com.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.entity.lesson.LessonLevel;

@Repository
public interface JpaLessonLevelRepository extends JpaRepository<LessonLevel, Integer> {
	List<LessonLevel> findAllByLessonId(Integer lessonId);

}
