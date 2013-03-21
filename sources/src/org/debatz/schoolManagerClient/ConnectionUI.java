package org.debatz.schoolManagerClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.debatz.Utils.GBC;
import org.debatz.Utils.Utils;




public class ConnectionUI extends JDialog implements ActionListener {

	
	

	private static final long serialVersionUID = 1L;
	private ClientUI clientUI;
	private JLabel picture, addressLabel, portLabel, timeoutLabel;
	private JTextField serverAddress, serverPort, clientTimeout;
	private JPanel jpContainer;
	private JButton validate;
	

	
	
	
	public ConnectionUI (ClientUI parent) {
		super((JFrame) parent, "Connexion au serveur", true);
		this.clientUI = parent;
		
		this.setSize(300, 330);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.initComponent();
		this.configureDisplay();
		this.setVisible(true);
	}
	
	
	
	
	
	
	private void initComponent () {
		jpContainer = new JPanel();
		jpContainer.setLayout(new BorderLayout());
		jpContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		picture = new JLabel(new ImageIcon(getClass().getResource("server.png")));
		JPanel panIcon = new JPanel();
		panIcon.setLayout(new BorderLayout());
		panIcon.add(picture);
		
		JPanel envParams = new JPanel();
		envParams.setLayout(new GridBagLayout());
		envParams.setBorder(BorderFactory.createTitledBorder(" Paramètres "));
		
		serverAddress = new JTextField();
		serverAddress.setPreferredSize(new Dimension(125, 25));
		addressLabel = new JLabel("Adresse du serveur :");

		serverPort = new JTextField("6789");
		serverPort.setPreferredSize(new Dimension(125, 25));
		portLabel = new JLabel("Numéro du port :");
		
		clientTimeout = new JTextField("25");
		clientTimeout.setPreferredSize(new Dimension(125, 25));
		timeoutLabel = new JLabel("Timeout client :");

		envParams.add(addressLabel, new GBC(0, 0).setAnchor(GBC.WEST).setInsets(2));
		envParams.add(serverAddress, new GBC(1, 0).setAnchor(GBC.WEST).setInsets(2));
		envParams.add(portLabel, new GBC(0, 1).setAnchor(GBC.WEST).setInsets(2));
		envParams.add(serverPort, new GBC(1, 1).setAnchor(GBC.WEST).setInsets(2));
		envParams.add(timeoutLabel, new GBC(0, 2).setAnchor(GBC.WEST).setInsets(2));
		envParams.add(clientTimeout, new GBC(1, 2).setAnchor(GBC.WEST).setInsets(2));
		
		validate = new JButton("Connexion");
		validate.addActionListener(this);
		
		this.setContentPane(jpContainer);
		jpContainer.add(panIcon, BorderLayout.NORTH);
		jpContainer.add(envParams, BorderLayout.CENTER);
		jpContainer.add(validate, BorderLayout.SOUTH);
		
		this.pack();
		validate.requestFocus();
	}
	
	
	
	
	
	
	
	private void configureDisplay () {
		Properties clientConfig = Utils.loadProperties(Utils.clientConfigFile);
		serverAddress.setText(clientConfig.getProperty("server_address"));
		serverPort.setText(clientConfig.getProperty("server_port"));
		clientTimeout.setText(clientConfig.getProperty("client_timeout"));
	}




	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == validate) 
		{
			if (serverAddress.getText().length() <= 0)
				Utils.alert("L'adresse du serveur est incomplète.");
			else if (Integer.parseInt(serverPort.getText()) < 0)
				Utils.alert("Le port du serveur est au mauvais format.");
		
			else 
			{
				this.dispose();
			
				Properties newClientConfig = new Properties();
				newClientConfig.put("server_address", serverAddress.getText());
				newClientConfig.put("server_port", serverPort.getText());
				newClientConfig.put("client_timeout", clientTimeout.getText());
				
				if (Utils.confirmRequest("Sauvegarde", "Se souvenir de ces paramètres de connexion ?")) {
					Utils.saveProperties(Utils.clientConfigFile, newClientConfig);
					clientUI.launchClient(null);
				}
				else
					clientUI.launchClient(newClientConfig);
			}
		
		}
		
	}
	
	
}
