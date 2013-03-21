package org.debatz.schoolManagerClient;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.debatz.Utils.ButtonEditor;
import org.debatz.Utils.ButtonRenderer;
import org.debatz.Utils.GBC;
import org.debatz.Utils.Utils;
import org.debatz.network.Client;
import org.debatz.network.Packet;
import org.debatz.schoolManagerServer.Course;
import org.debatz.schoolManagerServer.Database;

import org.debatz.Models.*;


public class StudentUI extends JDialog implements ActionListener {

	
	private static final long serialVersionUID = 1L;
	private JButton addStudent, manageGrades, validate;
	private JTable tabCourses;
	private JPanel jpContainer;
	private Client client;
	private JCheckBox option;
	private CourseTableModel model;
	private JTextField txt1;
	private JDialog dial;
	
	
	public StudentUI (ClientUI parent) {
		super((JFrame) parent, "Gérer les étudiants", false);
		this.setSize(550, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		client = Client.getInstance();
		this.initComponent();
		this.setVisible(true);
	}

	

	public void initComponent () {
		jpContainer = new JPanel(new GridBagLayout());
		addStudent = new JButton("Ajouter un étudiant...");
		manageGrades = new JButton("Gérer les notes");
		addStudent.addActionListener(this);
		jpContainer.setBorder(new EmptyBorder(7, 7, 7, 7));
		this.setContentPane(jpContainer);
		
		model = new CourseTableModel(loadStudentsToModel(null));
		this.tabCourses = new JTable(model);
		 
		for (int i = 0, n = tabCourses.getColumnModel().getColumnCount(); i < n; i++)
			tabCourses.getColumnModel().getColumn(i).setCellRenderer(new CourseRenderer());
				
		this.tabCourses.setRowHeight(30);
		this.tabCourses.setAutoCreateRowSorter(true);
		this.tabCourses.getColumn("A").setHeaderValue("ID");
		this.tabCourses.getColumn("B").setHeaderValue("Nom");
		this.tabCourses.getColumn("C").setHeaderValue("Prénom");
		this.tabCourses.getColumn("D").setCellRenderer(new ButtonRenderer("Suppr"));
		this.tabCourses.getColumn("D").setCellEditor(new ButtonEditor(new JCheckBox(), tabCourses));
		this.tabCourses.getColumn("D").setHeaderValue("Suppr");
		
		jpContainer.add(new JScrollPane(tabCourses), new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setInsets(6, 3, 3, 3).setWeight(540, tabCourses.getPreferredScrollableViewportSize().getHeight()));
		jpContainer.add(manageGrades, new GBC(0, 1).setAnchor(GBC.WEST).setInsets(3));
		jpContainer.add(addStudent, new GBC(1, 1).setAnchor(GBC.EAST).setInsets(3));
	}
	
	
	
	
	
	
	
	
	
	
	private List<Course> loadStudentsToModel (String where) {
		Packet result = client.sendAndLoad(new Packet("request", "select", "students", null));
		
		return result.getCourses();
	}

	
	
	
	
	
	
	private void openAddCourse () {
		dial = new JDialog();
		dial.setSize(350, 275);
		dial.setResizable(false);
		dial.setLocationRelativeTo(null);
		dial.setVisible(true);
		
		JPanel panelNew = new JPanel();
		JPanel panelWin = new JPanel();
		panelWin.setLayout(new BorderLayout());
		dial.setContentPane(panelWin);
		panelNew.setLayout(new GridBagLayout());
		panelWin.setBorder(new EmptyBorder(7, 7, 7, 7));
		panelNew.setBorder(BorderFactory.createTitledBorder(" Ajouter un cours "));
		JLabel imgNew = new JLabel (new ImageIcon("pictures/adding.png"));
		JLabel name = new JLabel("Nom du cours :");
		JLabel major = new JLabel("Major :");
		option = new JCheckBox("Optionnel");
		validate = new JButton("Ajouter");
		panelWin.add(imgNew, BorderLayout.NORTH);
		panelNew.add(name, new GBC(0, 1).setAnchor(GBC.WEST));
		panelNew.add(txt1, new GBC(1, 1).setFill(GBC.BOTH));
		panelNew.add(major, new GBC(0, 2).setAnchor(GBC.WEST));
		panelNew.add(option, new GBC(0, 3, 2, 1).setAnchor(GBC.CENTER).setFill(GBC.CENTER));
		panelNew.add(new JSeparator(JSeparator.HORIZONTAL), new GBC(0, 4, 2, 1).setFill(GBC.BOTH));
		panelNew.add(validate, new GBC(1, 5).setFill(GBC.CENTER).setAnchor(GBC.EAST));
		panelWin.add(panelNew, BorderLayout.SOUTH);
		
		validate.addActionListener(this);
	}
	
	
	
	
	
	
	private void addNewCourse () {
		Course addingCourse = new Course (Database.UNKNOW_ID, txt1.getText().trim(), option.isSelected());

		Packet packet = new Packet ("request", "insert", "courses", null);
		packet.addCourse(addingCourse);
		Utils.alert("La matière " + txt1.getText() + " a été ajoutée !");
		Packet response = client.sendAndLoad(packet);
	
		dial.dispose();
		model.addRow(response.getCourses().get(0));
		this.tabCourses.revalidate();
	}
	
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addStudent) {
			openAddCourse();
		} else if (e.getSource() == validate) {
			addNewCourse();
		}
	}
}
