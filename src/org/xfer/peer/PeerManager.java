package org.xfer.peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xfer.Logger;
import org.xfer.peer.Peer.PeerMessageListener;
import org.xfer.protocol.Message;
import org.xfer.protocol.MessageType;
import org.xfer.protocol.Protocol;

public class PeerManager 
{
	private static final PeerManager instance = new PeerManager();
	protected Server		server;
	protected List<Peer>	peers;
	
	protected Client.StatusListener	clientListener;
	protected Server.StatusListener	serverListener;
	protected PeerMessageListener		peerMessageListener;
	protected List<PeerListener>		peerListeners;
	
	private PeerManager()
	{
		peers = new ArrayList<Peer>();
		peerListeners = new ArrayList<PeerListener>();
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
	
	protected PeerMessageListener getPeerMessageListener()
	{
		if( peerMessageListener == null )
		{
			peerMessageListener = new PeerMessageListener(){

				@Override
				public void onMessageReceived(Peer peer, Message message) {
					if( message.getType() == MessageType.DISCONNECT )
					{
						onPeerDisconnected(peer);
					}
				}
			};
		}
		
		return peerMessageListener;
	}
	
	protected synchronized void onPeerConnected(Peer peer)
	{
		if( peer == null )
		{
			Logger.Log("PeerManager.onPeerConnected", "Peer is null");
			return;
		}
		
		peer.setConnected(true);
		peer.addMessageListener(getPeerMessageListener());
		peers.add(peer);
		
		new PeerMessageThread(peer).start();
		Logger.Log("PeerManager.onPeerConnected", "Peer connected");
		Logger.Log("PeerManager.onPeerConnected", "Total Peers: " + peers.size());
		
		for(PeerListener listener : peerListeners)
			listener.onPeerConnected(peer);
	}
	
	protected void onPeerDisconnected(Peer peer)
	{
		peer.getProtocol().disconnect();
		peer.setConnected(false);
		
		synchronized(peers)
		{
			peers.remove(peer);
		}
		
		Logger.Log("PeerManager.onPeerDisconnected", "Peer disconnected");
		Logger.Log("PeerManager.onPeerDisconnected", "Total Peers: " + peers.size());
		
		for(PeerListener listener : peerListeners)
			listener.onPeerConnected(peer);
	}
	
	public static void addPeerListener(PeerListener listener)
	{
		if( listener == null || instance.peerListeners.contains(listener) )
			return;
		
		instance.peerListeners.add(listener);
	}
	
	public static List<Peer> GetPeers()
	{
		return instance.peers;
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
	
	public static void disconnectFromPeer(Peer peer)
	{
		instance._disconnectFromPeer(peer);
	}
	
	protected void _disconnectFromPeer(Peer peer)
	{
		onPeerDisconnected(peer);
	}
	
	public static void disconnectFromAllPeers()
	{
		instance._disconnectFromAllPeers();
	}
	
	protected void _disconnectFromAllPeers()
	{
		Peer[] peers = this.peers.toArray(new Peer[this.peers.size()]);
		for(Peer peer : peers)
			_disconnectFromPeer(peer);
	}
	
	public interface PeerListener
	{
		public void onPeerConnected(Peer peer);
		public void onPeerDisconnected(Peer peer);
	}
	
	private class PeerMessageThread extends Thread implements Runnable
	{
		protected Peer peer;
		public PeerMessageThread(Peer peer)
		{
			this.peer = peer;
		}
		
		public void run()
		{
			try
			{
				Message msg = null;
				while(peer.isConnected())
				{
					msg = peer.getProtocol().receiveMessage();
					if( msg == null )
						continue;
					
					for(PeerMessageListener listener : peer.getMessageListeners())
						listener.onMessageReceived(peer, msg);
				}
			}catch(IOException ioe)
			{
				PeerManager.this.onPeerDisconnected(peer);
			}
		}
	}
}
