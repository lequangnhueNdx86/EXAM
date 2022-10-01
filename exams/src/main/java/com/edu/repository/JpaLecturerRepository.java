package com.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edu.entity.user.Lecturer;

@Repository
public interface JpaLecturerRepository extends JpaRepository<Lecturer, Long>{
	Lecturer findByUsername(String username);

}
