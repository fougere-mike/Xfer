package org.xfer.peer;

import java.util.ArrayList;
import java.util.List;

import org.xfer.protocol.Message;
import org.xfer.protocol.Protocol;

public class Peer 
{
	protected Protocol	protocol;
	protected boolean connected;
	
	protected List<PeerMessageListener> messageListeners;
	
	public Peer(Protocol protocol)
	{
		this.protocol = protocol;
		this.messageListeners = new ArrayList<PeerMessageListener>();
	}
	
	public String toString()
	{
		return protocol.getSocket().getRemoteSocketAddress().toString();
	}
	
	public Protocol getProtocol()
	{
		return protocol;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public void addMessageListener(PeerMessageListener listener)
	{
		if( listener != null )
			messageListeners.add(listener);
	}
	
	public List<PeerMessageListener> getMessageListeners() {
		return messageListeners;
	}

	public void setMessageListeners(List<PeerMessageListener> messageListeners) {
		this.messageListeners = messageListeners;
	}
	
	public interface PeerMessageListener
	{
		public void onMessageReceived(Peer peer, Message message);
	}
}
