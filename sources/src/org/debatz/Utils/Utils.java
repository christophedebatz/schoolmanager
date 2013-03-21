package org.debatz.Utils;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JOptionPane;




public class Utils {
	
	public static String appName = "School Manager";
	public static String serverConfigFile = "serverConfig.xml";
	public static String clientConfigFile = "clientConfig.xml";
	public static String dbConfigFile = "dbConfig.xml";
	
	
	
	public static boolean deleteDirectory (File path) { 
		boolean resultat = true; 
		if (path.isFile())
			return path.delete();
        
        if (path.exists()) { 
                File [] files = path.listFiles(); 
                for (int i = 0; i < files.length; i++)
                	resultat &= files[i].isDirectory() ? deleteDirectory(files[i]) : files[i].delete();
        }
        resultat &= path.delete(); 
        return resultat; 
	}
	
	
	
	public static String getIPAddress () {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (Exception e) {
			alert(e);
		}
	    return null;
	}
	
	
	
	public static Properties loadProperties (String file) {
		Properties props = new Properties();
		FileInputStream xml;
		
		try {
			xml = new FileInputStream(file);
			props.loadFromXML(xml);
			xml.close();
		} catch (Exception e) {
			alert(e);
		}
		return props;
	}
	
	
	
	public static Properties saveProperties (String file, Properties props) {
		File f = new File (file);
		if (f.exists())
			f.delete();
		
		try {
			FileOutputStream xml = new FileOutputStream(file);
			props.storeToXML(xml, "");
			xml.close();
		} catch (IOException e) {
			Utils.alert(e);
		}
		return props;
	}
	
	
	
	public static void alert (Exception e) {
		JOptionPane.showMessageDialog (null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
	
	
	
	public static void alert (String message) {
		JOptionPane.showMessageDialog (null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	public static Properties getDefaultServerProperties () {
		Properties defaultServerProperties = new Properties();
		defaultServerProperties.put("server_port", "6789");
		defaultServerProperties.put("server_password", "");
		return defaultServerProperties;
	}
	
	
	public static String getCurrentTime (String separator) {
		Calendar start = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH" + separator + "mm" + separator + "ss");
		return sdf.format(start.getTime());
	}
	
	
	
	public static boolean saveFileOnDisk (Object parent, String content, String title, String fileType) {
		FileDialog dial = new FileDialog((Frame) parent, "Sauvegarder...", FileDialog.SAVE);
		dial.setFile("SchoolManager_" + title + "_" + getCurrentTime("-") + fileType);
	    dial.setDirectory(".\\");
		dial.setLocationRelativeTo(null);
		dial.setVisible(true);
		if (dial.getFile() == null)
			return false;
		String path = dial.getDirectory() + System.getProperty("file.separator") + dial.getFile();
		FileWriter writer = null;
		try {
		     writer = new FileWriter (path, false);
		     writer.write(content.substring(4));
		     writer.close();
		     JOptionPane.showMessageDialog (null, "Le contenu a bien ŽtŽ sauvegardŽ !", "Sauvegarde effectuŽe", JOptionPane.INFORMATION_MESSAGE);
		     return true;
		} catch(IOException e) {
		    alert(e);
		}
		return false;
	}
	
	
	
	public static boolean confirmRequest (String title, String message) {
		int choice = JOptionPane.showConfirmDialog(null, 
									message, 
									title, 
									JOptionPane.YES_NO_CANCEL_OPTION, 
									JOptionPane.QUESTION_MESSAGE
						);
		return (choice == JOptionPane.YES_OPTION) ? true : false;
	}
	
	
	
	
	
	public static boolean confirmRequest () {
		int choice = JOptionPane.showConfirmDialog(null, 
									"Confirmer l'action ?", 
									"Demande de confirmation", 
									JOptionPane.YES_NO_CANCEL_OPTION, 
									JOptionPane.QUESTION_MESSAGE
						);
		return (choice == JOptionPane.YES_OPTION) ? true : false;
	}
	
	
	
	public static Object stringToObject (final String item)  {
		return new Object() { 
			public String toString() { return item; }
		};
	}
	
	
	public static boolean isWindows () {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
}
