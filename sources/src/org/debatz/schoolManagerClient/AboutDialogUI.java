package org.debatz.schoolManagerClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.debatz.Utils.Utils;

public class AboutDialogUI extends JDialog implements ActionListener {



	
	private static final long serialVersionUID = 1L;
	private JLabel picture, description;
	private JPanel jpContainer;
	private JButton validate;
	
	
	
	
	public AboutDialogUI (JFrame parent) {
		super(parent, "A propos...", true);
		this.setSize(350, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.initComponent();
		this.setVisible(true);
	}
	
	
	
	
	
	
	private void initComponent () {
		jpContainer = new JPanel();
		jpContainer.setLayout(new BorderLayout());
		jpContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		picture = new JLabel(new ImageIcon(getClass().getResource("christophe.jpg")));
		JPanel panIcon = new JPanel();
		panIcon.setLayout(new BorderLayout());
		panIcon.add(picture);
		
		JPanel envParams = new JPanel();
		envParams.setLayout(new BorderLayout());
		envParams.setBorder(BorderFactory.createTitledBorder(Utils.appName));
		
		description = new JLabel("<html>" + Utils.appName + " est un progiciel de gestion d'école tout en un. La section serveur et la section client ont été rassemblé dans une seule application dans le but de simplifié la vie de l'utilisateur. Enfin, ce logiciel est utilisable sur l'ensemble des OS supportant Java. Son principal développeur est Christophe de Batz, étudiant-ingénieur en trosième année de license à l'EFREI Paris. Logiciel distribué sous license fermée.</html>");
		description.setPreferredSize(new Dimension (1, 1));
		description.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, getBackground()));
		envParams.add(description, BorderLayout.CENTER);
		
		validate = new JButton("Fermer");
		validate.addActionListener(this);
		
		this.setContentPane(jpContainer);
		jpContainer.add(panIcon, BorderLayout.NORTH);
		jpContainer.add(envParams, BorderLayout.CENTER);
		jpContainer.add(validate, BorderLayout.SOUTH);
		
		validate.requestFocus();
	}




	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.dispose();
	}
	
	
}
