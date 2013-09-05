package org.xfer.peer;

import org.xfer.protocol.Protocol;

public class Peer 
{
	protected Protocol	protocol;
	public Peer(Protocol protocol)
	{
		this.protocol = protocol;
	}
	
	public String toString()
	{
		return protocol.getSocket().getRemoteSocketAddress().toString();
	}
}
