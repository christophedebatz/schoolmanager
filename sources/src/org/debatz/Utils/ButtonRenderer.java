package org.debatz.Utils;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.debatz.schoolManagerServer.Database;


public class ButtonRenderer extends JButton implements TableCellRenderer {

	
	private static final long serialVersionUID = 1L;
	private int courseId = Database.UNKNOW_ID;
	
	public ButtonRenderer (String title) {
	}
	
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocus, int row, int col) {
		setText("Supprimer");
		return this;
	}
	
	public int getCourseId () {
		return courseId;
	}
	
}
