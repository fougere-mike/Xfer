package org.xfer.peer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.xfer.protocol.Protocol;

public class Client 
{
	protected Socket		socket;
	protected Protocol		protocol;
	
	protected List<StatusListener>	statusListeners;
	
	public Client()
	{
		statusListeners = new ArrayList<StatusListener>();
	}
	
	public void addStatusListener(StatusListener listener)
	{
		if(listener == null)
			return;
		
		statusListeners.add(listener);
	}
	
	public void connect(String serverAddress, int port) throws IOException
	{
		socket 		= new Socket(serverAddress, port);
		protocol	= new Protocol(socket);
		
		try
		{
			protocol.connectToServer();
		}catch(IOException e)
		{
			for(StatusListener listener : statusListeners)
				listener.onException(e);
			throw e;
		}
		
		for(StatusListener listener : statusListeners)
			listener.onConnect(protocol);
	}
	
	public Protocol getProtocol()
	{
		return protocol;
	}
	
	public static void main(String[] args)
	{
		try
		{
			Client c = new Client();
		}catch(Exception e)
		{
			System.err.println("Failed to connect to server. "+e.toString());
		}
	}
	
	public interface StatusListener
	{
		public void onConnect(Protocol protocol);
		public void onDisconnect(Protocol protocol);
		public void onException(Exception e);
	}
}
