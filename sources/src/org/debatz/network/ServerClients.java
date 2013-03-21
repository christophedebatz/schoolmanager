package org.debatz.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.Timer;
import org.debatz.schoolManagerServer.ServerUI;




public class ServerClients extends Thread {

	
	private int idClient = 0;
	private Socket socket = null;
	private boolean isConnected = false;
	private volatile boolean listening = true;
	private Server server = null;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Timer timer;
	private Packet responseRequest;
	

	
    public ServerClients (Socket socket, Server server, int idClient) {
    	this.setName("Thread du client " + idClient);
    	this.socket = socket;
    	this.server = server;
    	this.idClient = idClient;
    	this.listening = listening;
    	  	
    	ServerUI.log("Le client " + socket.getInetAddress() + " (client #" + idClient + ") se connecte...");
    	timer = createTimer();
    	timer.start();
    }
 
    
    
    
    
    
    
    
    public Timer createTimer() {
    	ActionListener action = new ActionListener () {
    		@Override
    		public void actionPerformed (ActionEvent e) {
    			if (!isConnected) {
    				ServerUI.log("Le timeout de connexion est terminé pour client #" + idClient + ". Déconnexion.");
    				dismiss("timeout");
    			}
    		}
        };
        return new Timer (15000, action);
    }
    
    
    
    
    
    
    
    public void run() {
 
    	try {
    		out = new ObjectOutputStream(socket.getOutputStream());
    		in = new ObjectInputStream(socket.getInputStream()); 
    		
    		Packet input;
    		String inputMessage;
			String serverPassword = server.getServerUI().getServerConfig().getProperty("server_password");
 
    		while (listening) {
    			input = (Packet) in.readObject();
    			inputMessage = input.getMessage();
    			
    			if (!listening)
    				break;
        	
    			if (!isConnected && serverPassword.length() > 0 && !inputMessage.equals(serverPassword) && !inputMessage.equals("abort")) {
    				send("password required");
    				continue;
    			} else {
    				if (inputMessage.equals("abort")) {
    					ServerUI.log(getClientIP() + " (client #" + idClient + ") s'est déconnecté du serveur");
    					server.removeClientAt(idClient, "accepted");
    					return;
    				} else if (inputMessage.startsWith("request")) {
    					ServerCommands cmd = new ServerCommands (input, this.idClient);
    					send(cmd.getResponseRequest());
    				} else { // connection ok
    					timer.stop();
    					isConnected = true;
    					send("connected");
    					ServerUI.log(getClientIP() + " (client #" + idClient + ") est maintenant connecté (+ " + timer.getDelay() + ")");
    				}
    			} // endif
    			
    		} // endwhile

    		server.removeClientAt(idClient, "closed");
 
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
    
    
    
    
    
    
    public void send (String message) throws IOException {
		out.writeObject(new Packet(message));
	}
    
    
    
    
    
    public void send (Packet packet) throws IOException {
    	out.writeObject(packet);
    }
    
    
    
    
    
    public void dismiss (String reasonCode) {
    	this.listening = false;// fonctionne testé
    	isConnected = false;
    	try {
    		if (out != null)
    			send(reasonCode == null ? "abort" : "abort " + reasonCode);
        	out.close();
        	in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    
    public boolean isConnected () {
    	return isConnected;
    }


	public int getIdClient() {
		return idClient;
	}


	public void setIdClient(short idClient) {
		this.idClient = idClient;
		
	}
	
	
	
	
	public String getClientIP () {
		if (socket != null)
			return socket.getInetAddress().toString();
		else
			return null;
	}
	
	
	
	
	@Override
	public String toString () {
		return "Client #" + this.getIdClient() + " [" + this.getClientIP() + "]";
	}








	public Packet getResponseRequest() {
		return responseRequest;
	}








	public void setResponseRequest(Packet responseRequest) {
		this.responseRequest = responseRequest;
	}
	
}
