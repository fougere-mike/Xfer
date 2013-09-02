package org.xfer.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Protocol 
{
	public static final int	MIN_PORT			= 1024;
	public static final int	MAX_PORT			= 65535;
	public static final int	DEFAULT_LISTEN_PORT = 65310;
	
	public static final String HANDSHAKE_KEY_SERVER 	= "AcK";
	public static final String HANDSHAKE_KEY_CLIENT 	= "sYn";
	public static final String HANDSHAKE_KEY_CONFIRM	= "SyNaCk";
	
	private Socket					socket;
	protected DataInputStream		socketReader;
	protected DataOutputStream		socketWriter;
	
	public Protocol(Socket socket) throws IOException
	{
		this.socket 	= socket;
		socketReader	= new DataInputStream( this.socket.getInputStream() );
		socketWriter	= new DataOutputStream( this.socket.getOutputStream() );
	}
	
	public void connectToServer() throws IOException
	{
		String serverKey = "";
		
		socketWriter.writeUTF( HANDSHAKE_KEY_CLIENT );
		socketWriter.flush();
		
		serverKey = socketReader.readUTF();
		if( !serverKey.equals( HANDSHAKE_KEY_SERVER ) )
			throw new IOException("Server did not transmit correct key");
		
		socketWriter.writeUTF( HANDSHAKE_KEY_CONFIRM );
		socketWriter.flush();
		
		serverKey = socketReader.readUTF();
		if( !serverKey.equals( HANDSHAKE_KEY_CONFIRM ) )
			throw new IOException("Server did not confirm connection");
	}
	
	public void connectToClient() throws IOException
	{
		String clientKey = socketReader.readUTF();
		if( !clientKey.equals( HANDSHAKE_KEY_CLIENT ) )
			throw new IOException("Client did not transmit correct key");
	
		socketWriter.writeUTF( HANDSHAKE_KEY_SERVER );
		socketWriter.flush();
		
		clientKey = socketReader.readUTF();
		if( !clientKey.equals( HANDSHAKE_KEY_CONFIRM ) )
			throw new IOException("Client did not confirm connection");
		
		socketWriter.writeUTF( HANDSHAKE_KEY_CONFIRM );
		socketWriter.flush();
	}
	
	public Socket getSocket()
	{
		return socket;
	}
}
