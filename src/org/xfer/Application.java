package org.xfer;

import java.io.IOException;

import org.xfer.peer.PeerManager;

public class Application {
	public static void main(String[] args)
	{
		try {
			//PeerManager.begin();
			PeerManager.connectToPeer("localhost");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
