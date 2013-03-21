package org.debatz.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.Hashtable;

import org.debatz.schoolManagerServer.ServerUI;





public class Server extends Thread {

	
	
	private ServerSocket serverSocket = null;
	private volatile boolean listening = true;
	private Hashtable<Integer, ServerClients> clients;
	private static short nbClients = 0;
	private ServerUI serverUI;
	
	
	
	
	
	public Server (ServerUI serverUI) {
		this.serverUI = serverUI;
		int serverPort = Integer.parseInt(serverUI.getServerConfig().getProperty("server_port"));
		
        try {
            serverSocket = new ServerSocket(serverPort); // non limité en pool clients
            ServerUI.log("Le serveur est en écoute sur le port " + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        clients = new Hashtable<Integer, ServerClients>();
	}
	
	
	
	
	
	
	
	
	public void run () {
		while (listening) {
			int idClient = nbClients;
			try {
				clients.put(idClient, new ServerClients(serverSocket.accept(), this, idClient));
				clients.get(idClient).start();
				nbClients++;
				serverUI.refreshServerUI(this.clients);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
 
	}
	
	
	
	
	
	
	public void removeClientAt (int idClient, String reasonCode) {
		if (clients.get(idClient) != null) {
			clients.get(idClient).dismiss(reasonCode);
			clients.remove(idClient);
			nbClients--;
			serverUI.refreshServerUI(this.clients);
		}
	}
	
	
	
	

	
	
	
	public static int getNbClients() {
		return nbClients;
	}
	
	
	
	
	
	
	
	public void shutDown () {
		this.listening = false;
		for (int i = 0; i < clients.size(); i++)
			removeClientAt(i, "closed");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

	@Override
	protected void finalize () {
		try {
			shutDown();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Could not close server socket");
		}
	}
	
	
	
	
	@Override
	public String toString () {
		String result = null;
		for (Enumeration<ServerClients> e = clients.elements(); e.hasMoreElements();)
			result += e.nextElement().toString();
		return result;
	}







	public ServerUI getServerUI() {
		return serverUI;
	}
	
}
