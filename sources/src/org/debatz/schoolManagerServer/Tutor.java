package org.debatz.schoolManagerServer;

import java.io.Serializable;
import java.util.ArrayList;

public class Tutor extends Teacher implements Serializable {



	
	public Tutor(int id, String surname, String name) {
		super(id, surname, name);
	}

	private static final long serialVersionUID = 1L;
	private ArrayList<Student> students;

	
	
	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}
	
	public void addStudent(Student student) {
		this.students.add(student);
	}
	
}
