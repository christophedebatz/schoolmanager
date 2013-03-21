package org.debatz.schoolManagerServer;

import java.io.Serializable;



public class Course implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private int id = Database.UNKNOW_ID;
	private String name;
	private boolean option = false;
	public static final boolean Optional = true;
	public static final boolean Obligatory = false;
	
	
	
	
	
	public Course () throws Exception {
		//db = new Database();
	}
	
	public Course (int id, String name, boolean option) {
		this.id = id;
		this.name = name;
		this.option = option;
		//db = new Database();
	}
	
	/*public void save () {
		if (id == Database.UNKNOW_ID)
			create();
		else
			update();
	}
	
	
	private void create () {
		PreparedStatement ps;
		try {
			ps = db.getConnection().prepareStatement("insert into courses values(default, ?, ?)");
			ps.setString(1, name);
			ps.setBoolean(2, option);
			
			ps.execute();
			ps.close();

			if ((id = Database.getLastId("courses")) == Database.UNKNOW_ID)
				throw new Exception ("Unable to get current id");
			
			System.out.println("new course inserted with id n¡ " + id);
			
		} catch (Exception e) {
			Utils.alert(e);
		}
	}
	
	
	public void delete () throws Exception {	
		PreparedStatement ps = db.getConnection()
				.prepareStatement("delete from courses where course_id = ? and course_name = ?");

		ps.setInt(1, id);
		ps.setString(2, name);
		
		ps.execute();
		ps.close();
		
		id = Database.UNKNOW_ID;
	}
	
	
	private void update () {
		PreparedStatement ps;
		try {
			ps = db.getConnection()
						.prepareStatement("update courses set course_name = ?, " +
										  		"course_option = ? " +
										  			"where course_id = ?");
			ps.setString(1, name);
			ps.setBoolean(2, option);
			ps.setInt(3, id);
			ps.execute();
			
			System.out.println("course has been edited (id n¡ " + id + ")");
		} catch (SQLException e) {
			Utils.alert(e);
		}
	}

	*/
	
	
	public boolean isOption() {
		return option;
	}
	
	public boolean isInDatabase () {
		return id != Database.UNKNOW_ID;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
	
	public void setOption(boolean option) {
		this.option = option;
	}
	
	public boolean getOption () {
		return option;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString () {
		return id + " - " + name + " - " + option;
	}
}
