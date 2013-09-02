package org.xfer;

public class Logger 
{
	public static synchronized void Log(String tag, String msg)
	{
		System.out.println(tag+"(): "+msg);
	}
}
