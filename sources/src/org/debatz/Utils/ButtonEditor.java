package org.debatz.Utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import org.debatz.Models.CourseTableModel;
import org.debatz.network.Client;
import org.debatz.network.Packet;

public class ButtonEditor extends DefaultCellEditor implements ActionListener {


	private static final long serialVersionUID = 1L;
	private JButton button;
	private int courseId;
	private Client client = null;
	private CourseTableModel model = null;
	
	
	public ButtonEditor (JCheckBox checkBox, JTable jtable) {
		super(checkBox);
		button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(this);
	    this.model = (CourseTableModel) jtable.getModel();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { 
		button.setText("Supprimer");
		if (column == 3)
			courseId = Integer.parseInt((String) value);
	    return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		client = Client.getInstance();
		client.sendAndLoad(new Packet("request", "delete", "courses", "course_id = " + courseId));
		model.removeRow(courseId);
		Utils.alert("Suppression effectuée !");
    }

}
