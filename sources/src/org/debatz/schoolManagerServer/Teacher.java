package org.debatz.schoolManagerServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher extends Person implements Serializable {
	
	

	private static final long serialVersionUID = 1L;
	private ArrayList<Course> courses;
	private int id;
	
	
	public Teacher (int id, String surname, String name) {
		super(surname, name);
		this.id = id;
	}
	

	
	
	
	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		this.courses.add(course);
	}
}
