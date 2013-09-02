package org.xfer.ui.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;

public class FilesPanel extends JPanel 
{
	protected Controller	controller;
	protected WebList		peerList;
	
	public FilesPanel()
	{
		setLayout(new BorderLayout());
		
		controller 	= new Controller();
		peerList	= new WebList();
		peerList.add("My Files", new WebLabel());
		peerList.add("Peer 1", new WebLabel());
		
		add(peerList, BorderLayout.CENTER);
	}
	
	private class Controller
	{
		public Controller()
		{
		}
	}
}
