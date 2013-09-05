package org.xfer.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;

import org.xfer.peer.Peer;
import org.xfer.peer.PeerManager;
import org.xfer.peer.PeerManager.PeerListener;

import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebAccordionStyle;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.splitpane.WebSplitPane;

public class FilesPanel extends WebPanel 
{
	protected Controller	controller;
	protected WebList		peerList;
	protected WebPanel		listPanel;
	
	protected WebAccordion	accordion;
	protected WebSplitPane	splitPane;
	
	protected WebLabel		statusLabel;
	protected WebPanel		statusPanel;
	
	public FilesPanel()
	{
		setLayout(new BorderLayout());
		
		controller 	= new Controller();
		
		peerList	= new WebList(new String[]{"No Peers"});
		peerList.setEnabled(false);
		peerList.setPlainFont();
		
		listPanel	= new WebPanel();
		listPanel.setDrawRight(false);
		listPanel.setUndecorated(false);
		listPanel.add(peerList);
		
		splitPane	= new WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT, new WebLabel("These are my peers' files."), listPanel);
		splitPane.setOneTouchExpandable ( true );
		splitPane.setResizeWeight(0.8);
		
		accordion	= new WebAccordion( WebAccordionStyle.accordionStyle );
		accordion.setMultiplySelectionAllowed(false);
		accordion.addPane("My Files", new WebLabel("These are my files"));
		accordion.addPane("Remote Files", splitPane);
		accordion.addPane("Active Transfers (0)", new WebLabel("These are transfers"));
		
		statusPanel	= new WebPanel();
		statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		statusLabel = new WebLabel("Status: Online");
		statusLabel.setPlainFont();		
		statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		statusLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		statusPanel.add(statusLabel);
		add(statusPanel, BorderLayout.SOUTH);
		add(accordion, BorderLayout.CENTER);
	}
	
	private class Controller
	{
		public Controller()
		{
			PeerManager.addPeerListener(new PeerListener(){
				@Override
				public void onPeerConnected(Peer p)
				{
					EventQueue.invokeLater(new Runnable(){
						@Override
						public void run()
						{
							onPeersChanged();
						}
					});
				}

				@Override
				public void onPeerDisconnected(Peer peer) {
					EventQueue.invokeLater(new Runnable(){
						@Override
						public void run()
						{
							onPeersChanged();
						}
					});
				}
			});
		}
		
		public void onPeersChanged()
		{
			List<Peer> _peers = PeerManager.GetPeers();
			Peer[] peers = _peers.toArray(new Peer[_peers.size()]);
			peerList.setListData(peers);
		}
	}
}