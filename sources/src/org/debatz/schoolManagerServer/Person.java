package org.debatz.schoolManagerServer;

import java.io.Serializable;

public class Person implements Serializable {

	
	
	private static final long serialVersionUID = 1L;
	protected int id;
	protected String name; // nom
	protected String surname; // prenom
	
	
	
	public Person (String surname, String name) {
		this.surname = surname;
		this.name = name;
	}
	
	
	
	
	public String getName() {
		return this.name;
	}
	
	
	public String getSurname() {
		return this.surname;
	}
	
	
	public void setName (String name) {
		this.name = name;
	}
	
	
	public void setSurname (String surname) {
		this.surname = surname;
	}
	
}
