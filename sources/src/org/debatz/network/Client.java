package org.debatz.network;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import org.debatz.Utils.Utils;
import org.debatz.schoolManagerClient.ClientUI;




public class Client extends Thread {

	
	public boolean noServerFound = false;
	
	
	private static Client _instance;
	private boolean isConnected = false;
	private Socket s;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private volatile boolean listening = true;
	private static Properties clientConfig;
	private Packet responseRequest;
	
	
	
	
	
	public Client (Properties clientConfig) {
		_instance = this;
		Client.clientConfig = clientConfig;
		
		try {
			int timeout = 0;
			s = new Socket (clientConfig.getProperty("server_address"), Integer.parseInt(clientConfig.getProperty("server_port")));
			
			if ((timeout = Integer.parseInt(clientConfig.getProperty("client_timeout"))) > 0)
				s.setSoTimeout(timeout * 1000); // timeout server 
			
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
    
		} catch (Exception e) {
			noServerFound = true;
			Utils.alert("Serveur non connecté !");
		}
	}
	
	
	
	
	
	
	public static Client getInstance () {
		if (_instance == null)
			_instance = new Client (clientConfig);
		return _instance;
	}
	
	
	
	
	
	

	
	
	public Packet getResponseRequest () {
		return responseRequest;
	}
	
	
	




	@Override
	public void run() {
		
        try {////
        	send("hello");
        	
        	Packet input;
    		String inputMessage = null;
        	
			while (listening) {
				
				input = (Packet) in.readObject();
				inputMessage = input.getMessage();
				if (inputMessage.startsWith("abort"))
					break;
				if (inputMessage.equals("password required")) {
					String serverPassword = ClientUI.authRequestDialog();
					send(serverPassword == null ? "abort" : serverPassword);
					if (serverPassword == null)
						break;
				} else if (inputMessage.equals("connected")) {
					isConnected = true;
					ClientUI.getInstance().toggleConnectButton(true);
				} else {
					synchronized(this) {
						this.responseRequest = input;
					}
				}
			}
			
			isConnected = false;
			ClientUI.getInstance().toggleConnectButton(false);
			String reason = getAbortedReason(inputMessage);
			Utils.alert(reason);
			
			out.close();
            in.close();
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			dismiss();
		}
	}
	
	
	
	
	
	
	
	
	
	public Packet sendAndLoad (Packet packet) {
		Packet result = null;
		send(packet);
		
		do {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized(this){
				result = responseRequest;
			}
		} while (result == null);
		
		return result;
	}
	
	





	public void send (String message) throws IOException {
		out.writeObject(new Packet(message));
	}
	
	
	
	
	
	
	 public void send (Packet packet) {
	    	try {
				out.writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 
	
	
	
	
	
	public boolean isConnected() {
		return isConnected;
	}


	
	
	
	
	
	public void dismiss () {
		this.listening = false;// fonctionne testé
		isConnected = false;
		try {
			if (out != null)
				send("abort");
        	out.close();
            in.close();
			s.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	
	
	private String getAbortedReason (String inputLine) {
		String reasonCode = inputLine.substring("abort".length(), inputLine.length()).trim();
		if (reasonCode.equals("closed"))
			return "Le serveur a été fermé, la connexion est perdue.";
		else if (reasonCode.equals("timeout"))
			return "La connexion doit se faire en 10 secondes max.\nAu delà, la connexion est perdue.";
		else if (reasonCode.equals("expulsed"))
			return "L'administrateur serveur a annulé votre connexion.";
		else if (reasonCode.equals("accepted"))
			return null;
		else
			return "La connexion a été perdue.";
	}
	
}
