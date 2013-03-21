package org.debatz.Models;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import org.debatz.schoolManagerServer.Course;

public class CourseTableModel extends AbstractTableModel {
	 
	
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> colonnes         = new ArrayList<String>(4);
    private List<Course>     list             = new ArrayList<Course>();
   

    
    
    public CourseTableModel (List<Course> data) {
    	this.colonnes.add(0,"N¡");
    	this.colonnes.add(1,"Nom");
    	this.colonnes.add(2,"Optionnel");
    	this.colonnes.add(3,"Supprimer");
        this.list = data;
    }
 
    
    
    
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) {
            return int.class;
        } else if (column == 1) {
            return String.class;
        } else if (column == 2) {
            return boolean.class;
        } else if (column == 3) {
        	return JButton.class;
        }
        return String.class;
    }
    
    
    
 
    public int getColumnCount() {
        return this.colonnes.size();
    }
 
    
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex == colonnes.size() - 1)
    		return true;
        return false;
    }
 
    
    
    
    public int getRowCount() {
        if (this.list != null)
            return this.list.size();
        return 0;
    }
    
    
    
 
    public String getValueAt(int rowIndex, int columnIndex) {
        if (this.list != null && this.list.size() > 0) {
            if (columnIndex == 0) {
                return String.valueOf(this.list.get(rowIndex).getId());
            } else if (columnIndex == 1) {
                return String.valueOf(this.list.get(rowIndex).getName());
            } else if (columnIndex == 2) {
                return String.valueOf(this.list.get(rowIndex).getOption() ? "Oui" : "Non");
            } else if (columnIndex == 3) {
                return String.valueOf(this.list.get(rowIndex).getId());
            }
        }
        return null;
    }
    
    
    
 
    public Course getCourseAtRow(int row) {
        if (this.list != null && this.list.size() > 0) {
            return this.list.get(row);
        }
        return null;
    }
 
  
    
    public List<Course> getCoursesAtRow(int[] rows) {
 
        if (this.list != null && this.list.size() > 0) {
            List<Course> listeI = new ArrayList<Course>();
            for (int idRow = 0, n = rows.length; idRow < n; idRow++) {
                listeI.add(getCourseAtRow(rows[idRow]));
            }
 
            return listeI;
        }
        return null;
    }
    
    
    
    public void removeRow (int courseId){
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == courseId)
				list.remove(i);
		}
		this.fireTableDataChanged();
	}
	

    
	public void addRow(Course course){
		list.add(course);
		this.fireTableDataChanged();
	}


    

}