package org.debatz.schoolManagerServer;

import java.io.Serializable;

public class Grade implements Serializable {


	
	private static final long serialVersionUID = 1L;
	private int id = Database.UNKNOW_ID;
	private int grade;
	private Course course;

	
	
	
	public Grade (int grade, Course course) {
		this.grade = grade;
		this.course = course;
	}
	
	
	
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse (Course course) {
		this.course = course;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public int getId () {
		return id;
	}
	
}
