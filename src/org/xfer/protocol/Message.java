package org.xfer.protocol;

import java.nio.ByteBuffer;

public class Message 
{
	private MessageType	 	type;
	private String 			data;
	
	public static final Message DISCONNECT = new Message(MessageType.DISCONNECT);
	
	public Message(MessageType type, String data)
	{
		this.type = type;
		this.data = data;
	}
	
	public Message(MessageType type)
	{
		this(type, "");
	}
	
	public Message(String data)
	{
		this(MessageType.NULL, data);
	}
	
	public Message()
	{
		this("");
	}

	public String getData() 
	{
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
	}

	public MessageType getType() 
	{
		return type;
	}

	public void setType(MessageType type) 
	{
		this.type = type;
	}
	
	public static Message fromByteArray(byte[] array)
	{
		if( array == null || array.length < 8 )
			return null;
		
		ByteBuffer buff = ByteBuffer.wrap(array);
		int _type = buff.getInt();
		int _dLen = buff.getInt();
		
		if( _type < 0 || _type > MessageType.values().length )
			return null;
		
		if( _dLen < 0 || _dLen > (array.length - 8) )
			return null;
		
		MessageType type = MessageType.values()[_type];
		
		byte[] _data = new byte[_dLen];
		buff.get(_data);
		
		String data = new String(_data);
		
		return new Message(type, data);
	}
	
	public byte[] toByteArray()
	{
		byte[]	_data 	 = data.getBytes();
		int		_dataLen = _data.length;
		int arrayLen 	 =	4 +			// Type (int) 
							4 + 		// Data Length (int)
							_dataLen;	// Data (ascii string)
		
		ByteBuffer buff = ByteBuffer.allocate(arrayLen);
		buff.putInt(type.ordinal());
		buff.putInt(_dataLen);
		buff.put(_data);
		
		return buff.array();
	}
}
