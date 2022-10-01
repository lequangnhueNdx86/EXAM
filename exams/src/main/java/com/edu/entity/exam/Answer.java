package com.edu.entity.exam;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.edu.entity.BaseEntity;
import com.edu.entity.lesson.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String answer;
	private boolean result;
	@JsonBackReference
	@ManyToOne(targetEntity = Exam.class, cascade = CascadeType.ALL)
	@JoinColumn
	private Exam exam;
	@ManyToOne(targetEntity = Question.class, cascade = CascadeType.ALL)
	@JoinColumn
	private Question question;
	
}
