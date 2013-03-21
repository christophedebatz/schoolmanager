package org.debatz.schoolManagerServer;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.debatz.Utils.Utils;


public class SchoolManager {
	    

	public static void main (String[] args) {
		
		
		setLookAndFeel();
		SwingUtilities.invokeLater ( new Runnable() {
			public void run() {
				new SplashUI (null);
			}
		});
	}
	
	
	
	private static void setLookAndFeel () {
		try {
			if (Utils.isWindows())
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
