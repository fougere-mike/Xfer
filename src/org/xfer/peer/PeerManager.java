package org.xfer.peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xfer.Logger;
import org.xfer.protocol.Protocol;

public class PeerManager 
{
	private static final PeerManager instance = new PeerManager();
	protected Server			server;
	protected List<Peer>		peers;
	
	protected Client.StatusListener	clientListener;
	protected Server.StatusListener	serverListener;
	private PeerManager()
	{
		peers = new ArrayList<Peer>();
	}
	
	protected Client.StatusListener getClientStatusListener()
	{
		if( clientListener == null )
		{
			clientListener = new Client.StatusListener(){
				@Override
				public void onConnect(Protocol protocol) {
					final Peer p = new Peer(protocol);
					onPeerConnected(p);
				}

				@Override
				public void onDisconnect(Protocol protocol) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onException(Exception e) {
					Logger.Log("Client", e.toString());
				}
			};
		}
		
		return clientListener;
	}
	
	protected Server.StatusListener getServerStatusListener()
	{
		if( serverListener == null )
		{
			serverListener = new Server.StatusListener(){

				@Override
				public void onClientConnected(Protocol protocol) {
					final Peer p = new Peer(protocol);
					Logger.Log("ServerListener.onClientConnected", "Peer connected");
					onPeerConnected(p);
				}

				@Override
				public void onException(Exception e) {
					Logger.Log("Server", e.toString());
				}
				
			};
		}
		
		return serverListener;
	}
	
	protected synchronized void onPeerConnected(Peer peer)
	{
		if( peer == null )
		{
			Logger.Log("PeerManager.onPeerConnected", "Peer is null");
			return;
		}
		
		peers.add(peer);
		Logger.Log("PeerManager.onPeerConnected", "Peer connected");
		Logger.Log("PeerManager.onPeerConnected", "Total Peers: " + peers.size());
	}
	
	public static void begin() throws IOException
	{
		instance._begin();
	}
	
	public void _begin() throws IOException
	{
		server = new Server();
		server.addStatusListener(getServerStatusListener());
		server.start();
	}
	
	public static void connectToPeer(String address, int port) throws IOException
	{
		instance._connectToPeer(address, port);
	}
	
	public static void connectToPeer(String address) throws IOException
	{
		instance._connectToPeer(address, Protocol.DEFAULT_LISTEN_PORT);
	}
	
	protected void _connectToPeer(String address, int port) throws IOException
	{
		Client client = new Client();
		client.addStatusListener(getClientStatusListener());
		client.connect(address, port);
	}
}
