package com.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.entity.lesson.Level;

@Repository
public interface JpaLevelRepository extends JpaRepository<Level, Integer>{
	Optional<Level> findByIdAndRemovedFalse(Integer levelId);
	
	List<Level> findByRemovedFalse();
	
}
