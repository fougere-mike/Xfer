package org.xfer.ui;

import com.alee.laf.list.WebList;

public class PeerList extends WebList
{
	public PeerList(Object[] data)
	{
		super(data);
		
		setEnabled(false);
		setPlainFont();
	}
}
