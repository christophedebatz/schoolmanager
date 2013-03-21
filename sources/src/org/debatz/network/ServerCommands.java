package org.debatz.network;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.debatz.Utils.Utils;
import org.debatz.schoolManagerServer.Course;
import org.debatz.schoolManagerServer.Database;
import org.debatz.schoolManagerServer.Major;
import org.debatz.schoolManagerServer.ServerUI;
import org.debatz.schoolManagerServer.Teacher;

public class ServerCommands {

	
	
	private Packet responseRequest = null;
	private Database db;
	private int idClient = -1;
	
	
	
	public ServerCommands (Packet packetInput, int idClient) throws SQLException {
		if (packetInput == null)
			return;
		db = Database.getInstance();
		this.idClient = idClient;
		prepareResponseRequestPacket(packetInput);
	}
	
	
	
	
	
	
	private void prepareResponseRequestPacket(Packet packetInput) throws SQLException {	
		ResultSet rs = null;
		String sql = null;
		
		if (packetInput.getMessage().startsWith("request registered")) {
			responseRequest = new Packet("result query");
			switch (packetInput.getQueryNo()) {
			
			// select for courses search
			case 1:
				sql = "select teacher_id, teacher_name, teacher_surname from teachers natural left join teachers_courses";
				rs = db.select(sql);
				List<Teacher> teachers = new ArrayList<Teacher>();
				while (rs.next())
					teachers.add(new Teacher (rs.getInt("teacher_id"), rs.getString("teacher_surname"), rs.getString("teacher_name")));
				
				responseRequest.setTeacher(teachers);
				break;
				
			
			case 2:
				sql = "select course_name, course_id, course_option from courses " +
						"natural left join teachers_courses " +
						"natural left join teachers " +
						"natural left join majors_courses " +
						"natural left join majors " +
						packetInput.getQueryWhere();
				
				rs = db.select(sql);
				List<Course> courses = new ArrayList<Course>();
				while (rs.next())
					courses.add(new Course (rs.getInt("course_id"), rs.getString("course_name"), rs.getBoolean("course_option")));
				
				responseRequest.setCourses(courses);
				ServerUI.log("Le client #" + idClient + " a affiché la liste des cours.");
				break;
				
			}
		}
	
		else {
			sql = buildSQLQuery(packetInput);
			
			if (packetInput.getQueryType().equals("select")) {
				responseRequest = new Packet("result query");
				
				rs = db.select(sql);
				if (packetInput.getQueryTable().equals("courses")) {
					List<Course> courses = new ArrayList<Course>();
					while (rs.next())
						courses.add(new Course (rs.getInt("course_id"), rs.getString("course_name"), rs.getBoolean("course_option")));
					
					responseRequest.setCourses(courses);
					ServerUI.log("Le client #" + idClient + " a affiché la liste des cours.");
				} // endif
				
				else if (packetInput.getQueryTable().equals("majors")) {
					List<Major> majors = new ArrayList<Major>();
					
					while (rs.next()) {
						majors.add(new Major (rs.getInt("major_id"), rs.getString("major_name")));
					}
					
					responseRequest.setMajors(majors);
				} // endif
				
			}// endif
			
			else if (packetInput.getQueryType().equals("delete")) {
				Statement query = this.db.getConnection().createStatement();
				query.execute(sql);
				responseRequest = new Packet("deleted");
				ServerUI.log("Le client #" + idClient + " a supprimé un cours.");
			}
			
			else if (packetInput.getQueryType().equals("insert")) {
				responseRequest = new Packet("inserted");
				if (packetInput.getQueryTable().equals("courses")) {
					PreparedStatement ps = db.getConnection().prepareStatement("insert into courses values(default, ?, ?)");
					ps.setString(1, packetInput.getCourses().get(0).getName());
					ps.setBoolean(2, packetInput.getCourses().get(0).getOption());
					ps.execute();
					new Course(Database.getLastId("courses"), packetInput.getCourses().get(0).getName(), packetInput.getCourses().get(0).getOption());
					ps.close();
					ServerUI.log("Le client #" + idClient + " a inséré le cours " + packetInput.getCourses().get(0).getName());
				}
				
			}
		}// endif
		
		
	}



	
	
	
	
	public String buildSQLQuery(Packet packet) {
		String result = null;
		if (packet.getQueryType().equals("select")) {
			result = "select * from " + packet.getQueryTable();
			if (packet.getQueryWhere() != null)
				result += " where " + packet.getQueryWhere();
		} else if (packet.getQueryType().equals("delete")) {
			result = "delete from " + packet.getQueryTable();
			if (packet.getQueryWhere() != null)
				result += " where " + packet.getQueryWhere();
		}
		return result;
	}




	public Packet getResponseRequest () {
		return responseRequest;
	}
}
