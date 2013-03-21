package org.debatz.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.debatz.schoolManagerServer.Course;
import org.debatz.schoolManagerServer.Grade;
import org.debatz.schoolManagerServer.Major;
import org.debatz.schoolManagerServer.Student;
import org.debatz.schoolManagerServer.Teacher;
import org.debatz.schoolManagerServer.Tutor;




public class Packet implements Serializable {


	private static final long serialVersionUID = 1L;

	private String message, queryTable, queryType, queryWhere;
	short queryNo;
	
	


	private List<Course> courses = new ArrayList<Course>();
	private List<Grade> grades = new ArrayList<Grade>();
	private List<Major> majors = new ArrayList<Major>();
	private List<Student> students = new ArrayList<Student>();
	private List<Teacher> teacher = new ArrayList<Teacher>();
	private List<Tutor> tutors = new ArrayList<Tutor>();
	
	
	
	
	public Packet () {
	}
	
	
	public Packet (String message) {
		this.message = message;
	}
	
	
	
	public Packet (String message, String queryType, String queryTable, String queryWhere) {
		this.message = message;
		this.queryType = queryType;
		this.queryTable = queryTable;
		this.queryWhere = queryWhere;
	}
	

	public Packet (String message, short queryNo) {
		this.message = message;
		this.queryNo = queryNo;
	}
	
	
	
	
	
	
	public short getQueryNo() {
		return queryNo;
	}


	
	
	

	public void setQueryNo(short queryNo) {
		this.queryNo = queryNo;
	}
	
	
	
	
	
	public String getMessage () {
		return message;
	}






	public List<Course> getCourses() {
		return courses;
	}






	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}






	public List<Grade> getGrades() {
		return grades;
	}






	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}






	public List<Major> getMajors() {
		return majors;
	}






	public void setMajors(List<Major> majors) {
		this.majors = majors;
	}

	
	
	
	public void addCourse (Course course) {
		this.courses.add(course);
	}





	public List<Student> getStudents() {
		return students;
	}






	public void setStudents(List<Student> students) {
		this.students = students;
	}






	public List<Teacher> getTeacher() {
		return teacher;
	}






	public void setTeacher(List<Teacher> teacher) {
		this.teacher = teacher;
	}






	public List<Tutor> getTutors() {
		return tutors;
	}






	public void setTutors(List<Tutor> tutors) {
		this.tutors = tutors;
	}





	public String getQueryTable() {
		return queryTable;
	}




	public void setQueryTable(String queryTable) {
		this.queryTable = queryTable;
	}




	public String getQueryType() {
		return queryType;
	}




	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}




	public String getQueryWhere() {
		return queryWhere;
	}




	public void setQueryWhere(String queryWhere) {
		this.queryWhere = queryWhere;
	}
	
	
	
	public void setMessage (String message) {
		this.message = message;
	}
	
	
}
