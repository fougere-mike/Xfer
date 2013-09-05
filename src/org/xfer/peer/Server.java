package org.xfer.peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.xfer.Logger;
import org.xfer.protocol.Protocol;

public class Server extends Thread implements Runnable 
{
	protected ServerSocket		socket;
	protected int				port;
	
	protected List<Socket>			clients;
	protected List<StatusListener>	statusListeners;
	
	public Server() throws IOException
	{
		this(Protocol.DEFAULT_LISTEN_PORT);
	}
	
	public Server(int port) throws IOException
	{
		if( port >= Protocol.MIN_PORT && port <= Protocol.MAX_PORT )
			this.port = port;
		else
			this.port = Protocol.DEFAULT_LISTEN_PORT;
		
		socket 			= new ServerSocket(this.port);
		clients 		= new ArrayList<Socket>();
		statusListeners = new ArrayList<StatusListener>();
	}
	
	public void addStatusListener(StatusListener listener)
	{
		if( listener == null )
			return;
		
		statusListeners.add(listener);
	}
	
	public List<Socket> getClients()
	{
		return clients;
	}
	
	public void run()
	{
		Socket client = null;
		while( !socket.isClosed() )
		{
			try
			{
				client = socket.accept();
				Logger.Log("Server.run", "Client socket accepted");
			}catch(IOException e)
			{
				Logger.Log("run", e.toString());
			}

			if( client == null )
				continue;

			this.handleClient(client);
			client = null;
		}
	}
	
	protected void onClientConnected(Protocol protocol)
	{
		Logger.Log("Server.onClientConnected", "Client connected.");
		if( statusListeners == null || statusListeners.isEmpty() )
		{
			Logger.Log("Server.onClientConnected", "No listeners registered. Disconnecting from client.");
			protocol.disconnect();
			return;
		}
		
		for(StatusListener listener : statusListeners)
			listener.onClientConnected(protocol);
	}
	
	protected void handleClient(Socket client)
	{
		try
		{
			Logger.Log("Server.handleClient", "Starting client handler thread");
			new ClientHandler(client).start();
		}catch(IOException e)
		{
			Logger.Log("Server.handleClient", e.toString());
		}
	}
	
	public interface StatusListener
	{
		public void onClientConnected(Protocol protocol);
		public void onException(Exception e);
	}
	
	private class ClientHandler extends Thread implements Runnable
	{
		protected Socket	socket;
		protected Protocol	protocol;
		
		public ClientHandler(Socket client) throws IOException
		{
			socket 	  = client;
			protocol  = new Protocol(socket);
		}
		
		public void run()
		{
			try
			{
				Logger.Log("ClientHandler.run", "Connecting to client");
				protocol.connectToClient();
				
				onClientConnected(protocol);
			}catch(Exception e)
			{
				Logger.Log("ClientHandler.run", e.toString());
				try
				{
					protocol.getSocket().close();
				}catch(Exception _e){}
			}
		}
	}
}