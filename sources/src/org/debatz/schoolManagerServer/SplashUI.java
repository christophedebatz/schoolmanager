package org.debatz.schoolManagerServer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.debatz.Utils.GBC;
import org.debatz.Utils.Utils;
import org.debatz.schoolManagerClient.ClientUI;



public class SplashUI extends JDialog implements ActionListener {

	
	
	private static final long serialVersionUID = 1L;
	
	private JPanel jpContainer, jpGlobal;
	private ButtonGroup bgModes;
	private JRadioButton rbServerMode;
	private JRadioButton rbClientMode;
	private JButton buttonHelp;
	
	
	
	
	public SplashUI (JFrame pFrame) {
		super(pFrame, true);
		displayDialog();
	}
	
	
	
	
	
	@Override
	public void actionPerformed (ActionEvent e) {
		Object source = e.getSource();
		if (source == rbServerMode) {
			this.dispose();
			new ServerUI();
		} else if (source == rbClientMode) {
			this.dispose();
			new ClientUI();
		} else if (source == buttonHelp)
			JOptionPane.showMessageDialog (this, "Deux modes sont disponibles :\n\n- Le mode serveur vous permet d'héberger la base de données\n sur votre ordinateur et d'y autoriser la connexion à d'autres utilisateurs.\n\n- Le mode client vous permet de vous connecter à un serveur afin\nde gérer votre établisemment scolaire.", Utils.appName + " - Aide", JOptionPane.CLOSED_OPTION);
		else
			this.dispose();
			
		System.gc();
	}
	
	
	
	
	
	
	private void displayDialog () {
		this.setSize(330, 175);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle(Utils.appName);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jpGlobal = new JPanel();
		jpGlobal.setLayout(new GridBagLayout());
		jpContainer = new JPanel();
		jpContainer.setBorder(BorderFactory.createTitledBorder(" Mode d'execution "));
		jpContainer.setLayout(new GridBagLayout());
		JPanel jpRadioButtons = new JPanel();
		jpRadioButtons.setLayout(new GridBagLayout());
		bgModes = new ButtonGroup();
		rbServerMode = new JRadioButton("Mode serveur");
		rbClientMode = new JRadioButton("Mode client");
		rbClientMode.addActionListener(this);
		rbServerMode.addActionListener(this);
		bgModes.add(rbServerMode);
		bgModes.add(rbClientMode);
		jpRadioButtons.add(rbServerMode, new GBC(0, 0).setInsets(15, 15, 25, 15));
		jpRadioButtons.add(rbClientMode, new GBC(1, 0).setInsets(15, 15, 25, 15));
		jpContainer.add(jpRadioButtons, new GridBagConstraints (0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.CENTER,
                new Insets (0, 0, 0, 0), 0, 0));
		jpGlobal.add(jpContainer, new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setInsets(5));
		buttonHelp = new JButton("Aide");
		buttonHelp.addActionListener(this);
		jpGlobal.add(buttonHelp, new GBC(1, 1).setAnchor(GBC.EAST));
		this.setContentPane(jpGlobal);
		this.setVisible(true);
	}

}
