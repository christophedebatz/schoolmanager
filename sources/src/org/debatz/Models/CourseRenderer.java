package org.debatz.Models;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;




public class CourseRenderer extends JLabel implements TableCellRenderer {


	
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 
   public CourseRenderer () {
       super();
       this.setHorizontalAlignment(SwingConstants.CENTER);
   }

   
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
           int row, int column) {

       if (isSelected) {
           this.setOpaque(true);
           this.setBackground(Color.lightGray);
       } else {
           this.setOpaque(false);
       }

       if (value != null) {
           /*
            * if(column == 0) { this.setText(arg0) } else {
            */
           this.setText(value.toString());
           // }
       } else
           this.setText("");

       return this;
   }


}