package org.debatz.schoolManagerClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.debatz.Utils.Utils;
import org.debatz.network.Client;



public class ClientUI extends JFrame implements WindowListener {

	
	
	
	
	private static final long serialVersionUID = 1L;
	private static ClientUI _instance;
	private JPanel jpContainer;
	private Client client = null;
	private JToolBar toolbar;
	private JButton tbQuit, tbConnect, tbDisconnect, tbCourses, tbTeachers, tbStudents, tbHelp;
	private JEditorPane webView;
	private JScrollPane scroller;
	
	
	
	
	
	public ClientUI () {
		super();
		diplayFrame();
		this.addWindowListener(this);
		_instance = this;
	}
	
	
	
	
	
	// new singleton pattern to easily modify window
	public static ClientUI getInstance () {
		if (_instance == null)
			_instance = new ClientUI();
		return _instance;
	}
	
	
	
	
	
	private void diplayFrame () {
		jpContainer = new JPanel();
		
		this.setVisible(true);
		formatTitle(false);
		this.setSize(600, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(jpContainer);
		this.setLayout(new BorderLayout());
		
		makeToolBar();
		initComponent();
	}
	
	
	
	
	
	public void formatTitle (boolean connectionState) {
		String state = "(déconnecté)";
		if (connectionState) 
			state = "(connecté)";
		setTitle(Utils.appName + " - gestion d'école " + state);
	}
	
	
	
	
	
	
	public void initComponent () {
		webView = new JEditorPane();
		scroller = new JScrollPane(webView);
		webView.setContentType("ISO-8859-1");
		webView.setBackground(getBackground());
		try {
			File f = new File("index.html");
			webView.setPage(f.toURI().toURL());
		} catch (IOException e) {
			Utils.alert("Impossible de charger la page d'accueil \"index.html\" !");
			e.printStackTrace();
		}
		jpContainer.setBorder(new EmptyBorder(5,5,5,5));
		jpContainer.add(scroller, BorderLayout.CENTER);
	}
	
	
	
	
	
	
	
	private void makeToolBar () {
		toolbar = new JToolBar("Menu client");
		toolbar.setVisible(true);
		
		tbQuit = new JButton(new ImageIcon(getClass().getResource("/quit.png")));
		tbQuit.setPreferredSize(new Dimension(50, 50));
		tbQuit.addActionListener(listener);
		
		tbConnect = new JButton(new ImageIcon(getClass().getResource("/connect.png")));
		tbDisconnect = new JButton("cc");
		tbConnect.setPreferredSize(new Dimension(50, 50));
		tbDisconnect = new JButton(new ImageIcon(getClass().getResource("/disconnect.png")));
		tbDisconnect.setPreferredSize(new Dimension(50, 50));
		tbConnect.addActionListener(listener);
		tbDisconnect.addActionListener(listener);
		
		tbCourses = new JButton(new ImageIcon(getClass().getResource("/courses.png")));
		tbCourses.setPreferredSize(new Dimension(50, 50));
		tbCourses.addActionListener(listener);
		
		tbTeachers = new JButton(new ImageIcon(getClass().getResource("/teacher.png")));
		tbTeachers.setPreferredSize(new Dimension(50, 50));
		tbTeachers.addActionListener(listener);
		
		tbStudents = new JButton(new ImageIcon(getClass().getResource("/schoolers.png")));
		tbStudents.setPreferredSize(new Dimension(50, 50));
		tbStudents.addActionListener(listener);
		
		tbHelp = new JButton(new ImageIcon(getClass().getResource("/help.png")));
		tbHelp.setPreferredSize(new Dimension(50, 50));
		tbHelp.addActionListener(listener);
		
		tbConnect.setToolTipText("Se connecter au serveur");
		tbDisconnect.setToolTipText("Se déconnecter du serveur");
		tbCourses.setToolTipText("Accéder aux cours");
		tbTeachers.setToolTipText("Accéder aux professeurs");
		tbStudents.setToolTipText("Accéder aux étudiants et aux notes");
		tbQuit.setToolTipText("Quitter " + Utils.appName);
		tbHelp.setToolTipText("A propos de " + Utils.appName);

		toolbar.add(tbQuit);
		toolbar.add(tbConnect);
		toolbar.addSeparator();
		toolbar.add(tbCourses);
		toolbar.add(tbTeachers);
		toolbar.add(tbStudents);
		toolbar.addSeparator();
		toolbar.add(tbHelp);
		
		this.tbCourses.setEnabled(false);
		this.tbStudents.setEnabled(false);
		this.tbTeachers.setEnabled(false);
		
		jpContainer.add(toolbar, BorderLayout.NORTH);
		
	}
	
	
	
	
	
	
	ActionListener listener = new ActionListener () {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == tbQuit) {
				disconnect(false);
				close();
			}
			else if (e.getSource() == tbConnect)
				connect();
			else if (e.getSource() == tbDisconnect)
				disconnect(true);
			else if (e.getSource() == tbHelp)
				new AboutDialogUI(null);
			else if (e.getSource() == tbCourses) {
				if (client.isConnected())
					openCourseUI ();
			}
			else if (e.getSource() == tbStudents)
				if (client.isConnected())
					openStudentUI();
		}
	};
	
	
	
	
	
	
	
	
	
	public void openStudentUI () {
		new StudentUI (this);
	}

	
	
	
	
	
	
	public void openCourseUI () {
		new CourseUI (this);
	}
	
	
	
	
	
	public void close() {
		this.dispose();
	}
	
	
	
	
	
	public void connect () {
		new ConnectionUI (this);
	}
	
	
	
	
	
	
	
	public void disconnect (boolean alert) {
		if (client != null) {
			if (alert && !Utils.confirmRequest())
				return;
			client.dismiss();
			client = null;
			toggleConnectButton(false);
		}
	}
	
	
	

	
	
	public void launchClient (Properties clientConfig) {
		File f = new File(Utils.clientConfigFile);
		if (f.exists() && clientConfig == null)
			clientConfig = Utils.loadProperties(Utils.clientConfigFile);
		
		client = new Client (clientConfig);
		client.start();
	}
	
	
	
	
	
	
	
	public static String authRequestDialog () {
		return (String) JOptionPane.showInputDialog (null, "Le serveur requiert un mot de passe :", "Authentification...", JOptionPane.QUESTION_MESSAGE);
	}
	
	
	
	
	
	public ClientUI toggleConnectButton (boolean buttonEnabled) {
		for (int i = 0; i < toolbar.getComponents().length && toolbar.getComponentAtIndex(i) != null; i++) {
			if (toolbar.getComponentAtIndex(i) == tbConnect && buttonEnabled) {
				toolbar.remove(tbConnect);
				toolbar.add(tbDisconnect, i);
				this.tbCourses.setEnabled(true);
				this.tbStudents.setEnabled(true);
				this.tbTeachers.setEnabled(true);
				formatTitle(true);
			} else if (toolbar.getComponentAtIndex(i) == tbDisconnect && !buttonEnabled) {
				toolbar.remove(tbDisconnect);
				toolbar.add(tbConnect, i);
				this.tbCourses.setEnabled(false);
				this.tbStudents.setEnabled(false);
				this.tbTeachers.setEnabled(false);
				formatTitle(false);
			}
		}
		return this;
	}
	
	
	
	
	
	
	
	@Override
	public void windowClosing (WindowEvent e) {
		disconnect(false);
	}
	
	
	
	


	@Override
	public void windowActivated(WindowEvent e) { }



	@Override
	public void windowClosed(WindowEvent e) { }



	@Override
	public void windowDeactivated(WindowEvent e) { }



	@Override
	public void windowDeiconified(WindowEvent e) { }



	@Override
	public void windowIconified(WindowEvent e) { }



	@Override
	public void windowOpened(WindowEvent e) { }
	
}
