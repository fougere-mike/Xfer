package org.xfer.protocol;

public class Message 
{
	private MessageType	 	type;
	private String 			data;
	
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
}
