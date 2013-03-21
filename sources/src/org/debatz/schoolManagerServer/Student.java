package org.debatz.schoolManagerServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Student extends Person implements Serializable {
	
	
	public Student(String surname, String name) {
		super(surname, name);
	}

	private static final long serialVersionUID = 1L;
	private Major major;
	private ArrayList<Grade> grade;

	
	public ArrayList<Grade> getGrade() {
		return grade;
	}

	public void setGrade(ArrayList<Grade> grade) {
		this.grade = grade;
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

}
