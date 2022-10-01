package com.edu.entity.lesson;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.edu.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LessonLevel extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(targetEntity = Lesson.class)
	@JoinColumn
	private Lesson lesson;
	@ManyToOne(targetEntity = Level.class)
	@JoinColumn
	private Level level;
	private Integer quantity;
	
	public LessonLevel(Lesson lesson, Level level, Integer quantity) {
		super();
		this.lesson = lesson;
		this.level = level;
		this.quantity = quantity;
	}
}
