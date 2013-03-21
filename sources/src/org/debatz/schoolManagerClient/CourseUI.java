package org.debatz.schoolManagerClient;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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

public class CourseUI extends JDialog implements ActionListener {

	
	
	private static final long serialVersionUID = 1L;
	private JButton addCourse, okSearch, validate;
	private JTable tabCourses;
	private JPanel jpContainer;
	private Client client;
	private JComboBox combo1, combo2;
	private JCheckBox option;
	private CourseTableModel model;
	private JTextField txt1;
	private DefaultComboBoxModel mCombo1, mCombo2;
	private JDialog dial;
	private ClientUI parent;
	
	
	public CourseUI (ClientUI parent) {
		super(parent, "GŽrer les cours", false);
		this.parent = parent;
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
		addCourse = new JButton("Ajouter un cours...");
		addCourse.addActionListener(this);
		jpContainer.setBorder(new EmptyBorder(7, 7, 7, 7));
		this.setContentPane(jpContainer);
		
		JPanel panelSearch = new JPanel();
		panelSearch.setBorder(BorderFactory.createTitledBorder(" Chercher un cours "));
		panelSearch.setLayout(new GridBagLayout());
		JLabel label1 = new JLabel("Enseignant :");
		JLabel label2 = new JLabel("Majeure :");
		JLabel label3 = new JLabel("Nom du cours :");
		mCombo1 = new DefaultComboBoxModel();
		mCombo2 = new DefaultComboBoxModel();
		combo1 = new JComboBox(mCombo1);
		combo2 = new JComboBox(mCombo2);
		
		loadMajors();
		loadTeachers();
		
		txt1 = new JTextField();
		okSearch = new JButton("Chercher");
		okSearch.addActionListener(this);
		
		panelSearch.add(label1, new GBC(0, 0).setInsets(1).setFill(GBC.BOTH).setAnchor(GBC.NORTHWEST));
		panelSearch.add(combo1, new GBC(1, 0).setInsets(1).setAnchor(GBC.WEST).setWeight(50, combo1.getMinimumSize().getHeight()));
		panelSearch.add(label2, new GBC(2, 0).setInsets(1).setFill(GBC.BOTH).setAnchor(GBC.WEST));
		panelSearch.add(combo2, new GBC(3, 0).setInsets(1).setAnchor(GBC.WEST));
		panelSearch.add(label3, new GBC(0, 1).setInsets(1).setFill(GBC.BOTH).setAnchor(GBC.WEST));
		panelSearch.add(txt1, new GBC(1, 1).setInsets(1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST));
		panelSearch.add(okSearch, new GBC(3, 1, 2, 1).setInsets(1).setFill(GBC.HORIZONTAL));
		jpContainer.add(panelSearch, new GBC(0, 0).setFill(GBC.BOTH).setWeight(530, 30).setAnchor(GBC.CENTER));
		
		List<Course> courses = client.sendAndLoad(new Packet("request", "select", "courses", null)).getCourses();
		model = new CourseTableModel(courses);
		this.tabCourses = new JTable(model);
		 
		for (int i = 0, n = tabCourses.getColumnModel().getColumnCount(); i < n; i++)
			tabCourses.getColumnModel().getColumn(i).setCellRenderer(new CourseRenderer());
				
		this.tabCourses.setRowHeight(20);
		this.tabCourses.setAutoCreateRowSorter(true);
		this.tabCourses.getColumn("A").setHeaderValue("ID");
		this.tabCourses.getColumn("B").setHeaderValue("Nom");
		this.tabCourses.getColumn("C").setHeaderValue("Optionnel");
		this.tabCourses.getColumn("D").setCellRenderer(new ButtonRenderer("Supprimer"));
		this.tabCourses.getColumn("D").setCellEditor(new ButtonEditor(new JCheckBox(), tabCourses));
		this.tabCourses.getColumn("D").setHeaderValue("Supprimer");
		
		jpContainer.add(new JScrollPane(tabCourses), new GBC(0, 1, 3, 1).setFill(GBC.BOTH).setInsets(6, 3, 3, 3).setWeight(540, tabCourses.getPreferredScrollableViewportSize().getHeight()));
		jpContainer.add(addCourse, new GBC(0, 2).setAnchor(GBC.SOUTHEAST).setInsets(3));
	}
	
	
	
	
	
	private void loadCoursesBySearching () {
		String where = "";
		String teacher = combo1.getSelectedItem().toString();
		String major = combo2.getSelectedItem().toString();
		
		String sep = " and ";
		if (!teacher.equals("Aucun..."))
			where += "teacher_name = '" + teacher.trim() + "'";
		if (!major.equals("Aucun..."))
			where += where.length() > 0 ? sep + "major_name = '" + major.trim() + "'" : "major_name = '" + major.trim() + "'";
		if (txt1.getText().length() > 0)
			where += where.length() > 0 ? sep + "course_name LIKE '%" + txt1.getText() + "%'" : "course_name LIKE '%" + txt1.getText() + "%'";
			
		Packet packet = new Packet("request registered", (short) 2);
		packet.setQueryWhere(where.length() > 0 ? " where " + where : where);
		this.tabCourses.setModel(new CourseTableModel(client.sendAndLoad(packet).getCourses()));
	}
	
	
	
	

	
	
	
	
	private void loadTeachers () {
		Packet result = client.sendAndLoad(new Packet("request registered", (short) 1));
		combo1.addItem(Utils.stringToObject("Aucun..."));
		
		if (result.getTeacher().size() == 0)
			combo1.setEnabled(false);
		else
			for (int i = 0; i < result.getTeacher().size(); i++)
				combo1.addItem(Utils.stringToObject(result.getTeacher().get(i).getSurname().trim() + " " + result.getTeacher().get(i).getName().toUpperCase().trim()));
		
	}
	
	
	
	
	private void loadMajors () {
		Packet result = client.sendAndLoad(new Packet("request", "select", "majors", null));
		
		combo2.addItem(Utils.stringToObject("Aucun..."));
		
		if (result.getMajors().size() == 0)
			combo2.setEnabled(false);
		else
			for (int i = 0; i < result.getMajors().size(); i++)
				combo2.addItem(Utils.stringToObject(result.getMajors().get(i).getName().trim()));
	}
	
	
	
	
	
	
	
	/*private void openUpdateCourse () {
		dial = new JDialog();
		dial.setSize(350, 230);
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
		panelNew.add(major, new GBC(0, 3).setAnchor(GBC.WEST));
		panelNew.add(combo2, new GBC(1, 3).setFill(GBC.BOTH));
		panelNew.add(option, new GBC(0, 4, 2, 1).setAnchor(GBC.CENTER).setFill(GBC.CENTER));
		panelNew.add(new JSeparator(JSeparator.HORIZONTAL), new GBC(0, 5, 2, 1).setFill(GBC.BOTH));
		panelNew.add(validate, new GBC(1, 6).setFill(GBC.CENTER).setAnchor(GBC.EAST));
		panelWin.add(panelNew, BorderLayout.SOUTH);
		
		validate.addActionListener(this);
	}*/
	
	
	
	
	
	
	
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
		JLabel imgNew = new JLabel (new ImageIcon(getClass().getResource("/adding.png")));
		JLabel name = new JLabel("Nom du cours :");
		JLabel major = new JLabel("Major :");
		option = new JCheckBox("Optionnel");
		validate = new JButton("Ajouter");
		panelWin.add(imgNew, BorderLayout.NORTH);
		panelNew.add(name, new GBC(0, 1).setAnchor(GBC.WEST));
		panelNew.add(txt1, new GBC(1, 1).setFill(GBC.BOTH));
		panelNew.add(major, new GBC(0, 2).setAnchor(GBC.WEST));
		panelNew.add(combo2, new GBC(1, 2).setFill(GBC.BOTH));
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
		client.send(packet);
		Utils.alert("Le cours " + txt1.getText() + " a ŽtŽ ajoutŽ !");
	
		dial.dispose();
		this.dispose();
	}
	
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addCourse) {
			openAddCourse();
		} else if (e.getSource() == okSearch) {
			loadCoursesBySearching();
		} else if (e.getSource() == validate) {
			addNewCourse();
		}
	}
}
