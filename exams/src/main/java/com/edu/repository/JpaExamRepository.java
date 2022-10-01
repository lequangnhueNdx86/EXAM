package com.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.entity.exam.Exam;

@Repository
public interface JpaExamRepository extends JpaRepository<Exam, Integer>{
	@Query("SELECT COUNT(e) FROM Exam e WHERE e.lesson.id = ?1")
	Integer countByLessonId(Integer lessonId);
	
	List<Exam> findAllByLessonId(Integer lessonId);
	
	List<Exam> findByOrderByCreateTimeDesc();
	
	@Query(nativeQuery = true, value = "SELECT exam.* FROM exam ORDER BY score DESC LIMIT ?1")
	List<Exam> findByOrderByScoreDescAndLimitedTo(int limit);
	
	@Query("SELECT e FROM Exam e GROUP BY e.studentCode")
	List<Exam> listDistinctStudentCompletedExam();

}
