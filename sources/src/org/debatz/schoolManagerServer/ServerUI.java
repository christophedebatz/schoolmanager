package org.debatz.schoolManagerServer;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.debatz.Utils.*;
import org.debatz.network.Server;
import org.debatz.network.ServerClients;




public class ServerUI extends JFrame implements ActionListener, WindowListener {


	private static final long serialVersionUID = -1L;

	private JPanel jpContainer, jpParameters, jpActivity, jpPassword;
	private JLabel lblPort, lblFireClient;
	private JButton buttonSave, buttonDrop;
	private static JComboBox comboFireClient;

	private static JButton buttonResetActivity;
	private JButton buttonStoreLog;
	private JTextField jtfPort;
	private JPasswordField jpfPassword;
	private JCheckBox cbPassword;
	private JToggleButton buttonActivate;
	private static DefaultComboBoxModel modelClientList;
	private static DefaultListModel modelActivityList;
	private JList listActivity;
	private JScrollPane scrollActivity;
	
	private Properties serverConfig = null;	
	private Server server = null;
	
	
	
	
	public ServerUI () {
		super();
		displayFrame();
		logSayHello();
	}
	
	
	
	private void logSayHello () {
		log("Bienvenue sur " + Utils.appName + " Server", false);
		log("L'adresse IP du serveur est " + Utils.getIPAddress(), false);
	}
	
	
	
	private void displayFrame () {
		jpContainer = new JPanel();
		jpContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		jpParameters = new JPanel();
		jpActivity = new JPanel();
		jpActivity.setBorder(BorderFactory.createTitledBorder(" Log d'activité - " + Server.getNbClients() + " connecté(s)"));
		jpParameters.setBorder(BorderFactory.createTitledBorder(" Paramètres "));
		
		this.setVisible(true);
		this.setTitle(Utils.appName + " - serveur");
		this.setSize(700, 420);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(jpContainer);
		this.setLayout(new GridBagLayout());
	
		modelActivityList = new DefaultListModel();
		listActivity = new JList(modelActivityList);
		scrollActivity = new JScrollPane(listActivity);
		scrollActivity.getViewport().setBackground(this.getBackground());
		buttonActivate = new JToggleButton("ACTIVER SERVEUR");
		lblFireClient = new JLabel("Déconnecter des clients : ");
		modelClientList = new DefaultComboBoxModel();
		modelClientList.addElement(Utils.stringToObject("Aucun"));
		comboFireClient = new JComboBox(modelClientList);
		comboFireClient.setEnabled(false);
		comboFireClient.addActionListener(this);
		refreshServerUI(null);
		
		jpActivity.setLayout(new GridBagLayout());
		jpActivity.add(scrollActivity, new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(scrollActivity.getPreferredSize().getWidth(), scrollActivity.getPreferredSize().getHeight()));
		jpActivity.add(lblFireClient, new GBC(0, 1).setInsets(3).setAnchor(GBC.EAST));
		jpActivity.add(comboFireClient, new GBC(1, 1).setInsets(3).setAnchor(GBC.EAST));
		jpContainer.add(jpParameters, new GBC(0, 0, 1, 1).setInsets(3).setFill(GBC.VERTICAL).setAnchor(GBC.WEST));
		jpContainer.add(jpActivity, new GBC(1, 0, 5, 2).setInsets(3).setFill(GBC.BOTH).setWeight(500, 380).setAnchor(GBC.WEST));
		jpContainer.add(buttonActivate, new GBC(0, 1).setFill(GBC.BOTH).setInsets(5, 3, 5, 3).setAnchor(GBC.EAST));
	
		lblPort = new JLabel("Port d'écoute :");
		jtfPort = new JTextField();
		cbPassword = new JCheckBox("Exiger mot de passe");
		buttonSave = new JButton("Enregistrer");
		buttonDrop = new JButton("Vider la base de données");
		buttonResetActivity = new JButton("Vider le log d'activité");
		buttonStoreLog = new JButton("Sauvegarder le log...");
		
		buttonResetActivity.addActionListener(this);
		buttonResetActivity.setEnabled(false);
		buttonDrop.addActionListener(this);
		buttonSave.addActionListener(this);
		cbPassword.addActionListener(this);
		buttonActivate.addActionListener(this);
		buttonStoreLog.addActionListener(this);
		
		jpPassword = new JPanel();
		jpPassword.setLayout(new BorderLayout());
		jpPassword.setBorder(BorderFactory.createTitledBorder(" Accès restreint "));
		jpfPassword = new JPasswordField();
		jpfPassword.setDisabledTextColor(Color.LIGHT_GRAY);
		jpfPassword.setEnabled(false);
		jpPassword.add(cbPassword, BorderLayout.NORTH);
		jpPassword.add(jpfPassword, BorderLayout.SOUTH);
		
		jpParameters.setLayout(new GridBagLayout());
		jpParameters.add(lblPort, new GBC(0, 0).setFill(GBC.EAST).setAnchor(GBC.EAST).setWeight(20, lblPort.getSize().getHeight()));
		jpParameters.add(jtfPort, new GBC(1, 0).setFill(GBC.HORIZONTAL).setInsets(3).setWeight(90, jtfPort.getSize().getHeight()));
		jpParameters.add(jpPassword, new GBC(0, 1, 2, 1).setInsets(4).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL).setWeight(jpPassword.getMaximumSize().getWidth(), jpPassword.getMinimumSize().getHeight()));
		jpParameters.add(buttonSave, new GBC(0, 2, 2, 1).setInsets(2, 3, 0, 3).setFill(GBC.HORIZONTAL).setWeight(115, buttonSave.getSize().getHeight()));
		jpParameters.add(buttonStoreLog, new GBC(0, 3, 2, 1).setInsets(35, 3, 0, 3).setFill(GBC.HORIZONTAL).setWeight(115, buttonStoreLog.getSize().getHeight()));
		jpParameters.add(buttonResetActivity, new GBC(0, 4, 2, 1).setInsets(3, 3, 0, 3).setFill(GBC.HORIZONTAL).setWeight(115, buttonResetActivity.getSize().getHeight()));
		jpParameters.add(buttonDrop, new GBC(0, 5, 2, 1).setInsets(3).setFill(GBC.HORIZONTAL).setWeight(115, buttonDrop.getSize().getHeight()));
		
		configureDisplay();
	}
	
	
	
	private void saveSettings () {
		getServerConfig().setProperty("server_port", jtfPort.getText().trim());
		getServerConfig().setProperty("server_password", String.valueOf(jpfPassword.getPassword()));
		Utils.saveProperties(Utils.serverConfigFile, getServerConfig());
		
		JOptionPane.showMessageDialog(null, "Enregistrement effectué !", "Effectué", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	private void configureDisplay() {
		
		File f = releaseDropButton();
		
		f = new File(Utils.serverConfigFile);
		if (f.exists()) {
			setServerConfig(Utils.loadProperties(Utils.serverConfigFile));
		} else
			setServerConfig(Utils.saveProperties(Utils.serverConfigFile, Utils.getDefaultServerProperties()));
		
		if (getServerConfig().getProperty("server_password").toString().length() > 0) {
			jpfPassword.setText(getServerConfig().getProperty("server_password"));
			jpfPassword.setEnabled(true);
			cbPassword.setSelected(true);
		}
		
		if (getServerConfig().containsKey("server_port"))
			jtfPort.setText(getServerConfig().getProperty("server_port"));
	}
	
	
	
	
	private File releaseDropButton () {
		File f = new File("derbyDB/");
		if (!f.isDirectory())
			buttonDrop.setEnabled(false);
		else
			buttonDrop.setEnabled(true);
		return f;
	}
	
	
	
	public static void log (String message) {
		if (modelActivityList != null) {
			buttonResetActivity.setEnabled(true);
			modelActivityList.add(ServerUI.modelActivityList.size(), Utils.getCurrentTime(":") + " - " + message);
		}
	}
	
	
	

	public static void log (String message, boolean showTime) {
		String time = showTime ? Utils.getCurrentTime(":") + " - " : "";
		modelActivityList.add(ServerUI.modelActivityList.size(), time + message);
	}
	
	
	
	public void refreshServerUI (final Hashtable <Integer, ServerClients> clients) {
		if (clients != null) {
			modelClientList.removeAllElements();
			modelClientList.addElement(Utils.stringToObject("Aucun"));
			if (clients.isEmpty())
				comboFireClient.setEnabled(false);
			else {
				comboFireClient.setEnabled(true);
				for (Enumeration<ServerClients> e = clients.elements(); e.hasMoreElements();)
					modelClientList.addElement(Utils.stringToObject(e.nextElement().toString()));
			}
			comboFireClient.setSelectedIndex(0);
			jpActivity.setBorder(BorderFactory.createTitledBorder(" Log d'activité - " + Server.getNbClients() + " connecté(s)"));
		}
	}

	
	
	

	
	@Override
	public void actionPerformed (ActionEvent e) {
		Object source = e.getSource();
		if (source == buttonDrop) {
			if (!Utils.confirmRequest())
				return;
			buttonDrop.setEnabled(false);
			if (Database.remove()) {
				JOptionPane.showMessageDialog(null, "La base de données a été vidé avec succès !", "Effectué", JOptionPane.INFORMATION_MESSAGE);
				Database.removeInstance();
			} else {
				JOptionPane.showMessageDialog(null, "Impossible de vider la base de données...", "Erreur", JOptionPane.ERROR_MESSAGE);
				buttonDrop.setEnabled(true);
			}
		} 
		
		
		else if (source == buttonSave) {
			buttonSave.setEnabled(false);
			saveSettings();
			buttonSave.setEnabled(true);
		} 
		
		
		else if (source == buttonActivate) {
		    if (buttonActivate.getModel().isSelected()) { // launch server
		    	buttonSave.setEnabled(false);
		    	buttonDrop.setEnabled(false);
		    	buttonActivate.setText("DESACTIVER SERVEUR");
		    	launchServer();
		    	
		    } else { // release server
		    	stopServer();
		    	releaseDropButton();
		    	buttonSave.setEnabled(true);
		    	buttonActivate.setText("ACTIVER SERVEUR");
		    }
		} 
		
		
		else if (source == cbPassword) {
			if (cbPassword.isSelected()) {
	        	jpfPassword.setText(getServerConfig().getProperty("server_password"));
	        	jpfPassword.setEnabled(true);
	        } else {
	        	jpfPassword.setText("");
	        	jpfPassword.setEnabled(false);
	        }
		}
		
		else if (source == buttonResetActivity) {
			buttonResetActivity.setEnabled(false);
			modelActivityList.clear();
			logSayHello();
		}
		
		
		else if (source == buttonStoreLog) {
			String content = null;
			for (int i = 0; i < modelActivityList.size(); i++)
				content += modelActivityList.elementAt(i).toString() + "\n";
			
			Utils.saveFileOnDisk (this, content, "activite", ".log");
		}
		
		
		else if (source == (JComboBox)comboFireClient) {
			if (modelClientList.getSize() == 0)
				return;
			String selectedClient = modelClientList.getSelectedItem().toString();
			if (!selectedClient.equalsIgnoreCase("aucun")) {
				int idClient = Integer.parseInt(selectedClient.substring(8, selectedClient.indexOf("[") - 1)); 
				if (server != null) {
					server.removeClientAt(idClient, "expulsed");
					log("Le client #" + idClient + " a été déconnecté par l'administrateur.");
				}
			}
		}
			
	}
	
	
	
	
	
	private void launchServer () {
		Database db = Database.getInstance(); // initialize database connection
		
		PreparedStatement ps = null;
		
		try {
			ps = db.getConnection().prepareStatement("insert into majors values(default, ?)");
		
		ps.setString(1, "Informatique");
		
		ps.execute();
		
		ps = db.getConnection().prepareStatement("insert into majors values(default, ?)");
		ps.setString(1, "Web Marketing");
		
		ps.execute();
		
		ps = db.getConnection().prepareStatement("insert into majors values(default, ?)");
		ps.setString(1, "Mécanique");
		
		ps.execute();
		
		
		ps = db.getConnection().prepareStatement("insert into majors values(default, ?)");
		ps.setString(1, "Electricité");
		
		ps.execute();
		
		
		
		
		
		ps = db.getConnection().prepareStatement("insert into courses values(default, ?, ?)");
		ps.setString(1, "Anglais");
		ps.setBoolean(2, true);
		
		ps.execute();
		
		ps = db.getConnection().prepareStatement("insert into teachers_courses values(default, ?, ?)");
		ps.setInt(1, 0);
		ps.setInt(2, 0);
		
		ps.execute();
		
		ps = db.getConnection().prepareStatement("insert into teachers values(default, ?, ?)");
		ps.setString(1, "DE BATZ");
		ps.setString(2, "Christophe");
		
		ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server = new Server (this);
		server.start();
	}
	

	
	
	private void stopServer () {
		if (server!= null) {
			server.shutDown();
			log("Le serveur est maintenant désactivé");
		}
	}
	


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowClosing(WindowEvent arg0) {
		stopServer();
	}



	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	public Properties getServerConfig() {
		return serverConfig;
	}



	public void setServerConfig(Properties serverConfig) {
		this.serverConfig = serverConfig;
	}
}
