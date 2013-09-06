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
import org.xfer.ui.PeerList;

import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebAccordionStyle;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tree.WebTree;

public class FilesPanel extends WebPanel 
{
	protected Controller	controller;
	
	protected WebAccordion	accordion;
	
	protected WebLabel		statusLabel;
	protected WebPanel		statusPanel;
	
	protected FilesView	myFilesView;
	protected FilesView	remoteFilesView;
	
	protected WebSplitPane		splitPane;
	protected PeerList			peerList;
	protected WebScrollPane	peerScrollPane;
	
	public FilesPanel()
	{
		setLayout(new BorderLayout());
		
		peerList 	= new PeerList(new String[]{"No Peers."});
		peerScrollPane = new WebScrollPane( peerList );
		
		myFilesView = new FilesView();
		remoteFilesView = new FilesView();
		
		accordion	= new WebAccordion( WebAccordionStyle.accordionStyle );
		accordion.setMultiplySelectionAllowed(false);
		accordion.addPane("My Files", myFilesView);
		accordion.addPane("Remote Files", remoteFilesView);
		accordion.addPane("Active Transfers (0)", new WebLabel("These are transfers"));
		
		splitPane = new WebSplitPane( WebSplitPane.HORIZONTAL_SPLIT, accordion, peerScrollPane);
		splitPane.setResizeWeight(0.8);
		splitPane.setOneTouchExpandable(true);
		
		statusPanel	= new WebPanel();
		statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		statusLabel = new WebLabel("Status: Online");

		statusLabel.setPlainFont();
		statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		statusLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		statusPanel.add(statusLabel);
		add(statusPanel, BorderLayout.SOUTH);
		add(splitPane, BorderLayout.CENTER);
		
		controller 	= new Controller();
		PeerManager.addPeerListener(controller);
	}
	
	private class Controller implements PeerListener
	{
		public Controller()
		{
		}

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
		
		public void onPeersChanged()
		{
			List<Peer> _peers = PeerManager.GetPeers();
			Peer[] peers = _peers.toArray(new Peer[_peers.size()]);
			
			if( peers != null && peers.length > 0 )
			{
				peerList.setListData(peers);
				peerList.setEnabled(true);
			}else
			{
				peerList.setListData(new String[]{"No Peers"});
				peerList.setEnabled(false);
			}
		}
	}
}