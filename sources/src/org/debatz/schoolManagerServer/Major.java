package org.debatz.schoolManagerServer;

import java.io.Serializable;
import java.util.ArrayList;
 
public class Major implements Serializable {
	

	
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Course> courses;
	private int id;
	
	
	
	
	public Major(int id, String name) {
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
