package com.edu.entity.exam;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.edu.entity.BaseEntity;
import com.edu.entity.lesson.Lesson;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "student_id")
	private String studentCode;
	private String studentName;
	private Integer totalQuestion;
	private Integer correctQuestion;
	private Integer timeExam; //min
	private Double timeComplete; //min
	private Integer totalScore;
	private Integer score;
	private String result; // PASS, FAIL
	@JsonManagedReference
	@ManyToOne(targetEntity = Lesson.class, cascade = CascadeType.ALL)
	@JoinColumn
	private Lesson lesson;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exam")
	private List<Answer> listAnswer;
	
	@Override
	public String toString() {
		return "";
	}
}
