package org.xfer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.xfer.Logger;
import org.xfer.peer.Peer;
import org.xfer.peer.PeerManager;
import org.xfer.protocol.Protocol;
import org.xfer.ui.panels.FilesPanel;

import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.hotkey.HotkeyData;

public class MainWindow 
{
	protected JFrame	frame;
	protected JPanel	contentPane;
	protected JPanel	filePanel;
	protected JPanel	chatPanel;
	
	protected WebMenuBar	menuBar;
	protected WebMenu		mnuXfer;
	protected WebMenu		mnuSettings;
	protected WebMenu		mnuHelp;
	
	protected WebMenuItem	itmExit;
	
	protected WebTabbedPane	tabPane;
	
	public MainWindow()
	{
		frame = new JFrame();
		contentPane = new JPanel();
		filePanel = new JPanel();
		chatPanel = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		menuBar = new WebMenuBar();
		mnuXfer = new WebMenu("Xfer");
		mnuXfer.setPlainFont(true);
		
		mnuSettings = new WebMenu("Settings");
		mnuSettings.setPlainFont(true);
		
		mnuHelp = new WebMenu("Help");
		mnuHelp.setPlainFont(true);
		
		itmExit = new WebMenuItem("Exit");
		itmExit.setPlainFont(true);
		itmExit.setHotkey(Hotkey.CTRL_Q);
		itmExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
				System.exit(0);
			}
		});
		
		WebMenuItem itmConnect = new WebMenuItem("Connect");
		itmConnect.setPlainFont();
		itmConnect.setHotkey(Hotkey.CTRL_R);
		itmConnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					PeerManager.connectToPeer("localhost");
				}catch(IOException ioe)
				{
					Logger.Log("ActionListener", e.toString());
				}
			}
		});
		
		WebMenuItem itmDisconnect = new WebMenuItem("Disconnect");
		itmDisconnect.setPlainFont();
		itmDisconnect.setHotkey(Hotkey.CTRL_D);
		itmDisconnect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{	
				PeerManager.disconnectFromAllPeers();
			}
		});
		
		
		mnuXfer.add(itmConnect);
		mnuXfer.add(itmDisconnect);
		mnuXfer.add(itmExit);
		
		menuBar.add(mnuXfer);
		menuBar.add(mnuSettings);
		menuBar.add(mnuHelp);
		menuBar.setUndecorated(true);
		frame.setJMenuBar(menuBar);
		
		tabPane = new WebTabbedPane();
		tabPane.setTabPlacement(WebTabbedPane.LEFT);
		tabPane.setPlainFont();
		tabPane.setFontName("Verdana");
		tabPane.setFocusable(false);
		
		tabPane.addTab("Files", new FilesPanel());
		tabPane.addTab("Chat", chatPanel);
		tabPane.addTab("Peers", new WebLabel());
		WebLabel settings = new WebLabel();
		settings.setDrawShade(true);
		tabPane.addTab("Settings", settings);
		tabPane.setTabInsets(new Insets(10, 25, 10, 25));
		contentPane.add(tabPane, BorderLayout.CENTER);
		contentPane.setBackground(Color.WHITE);
		
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setVisible(true);
		
		try
		{
			PeerManager.begin();
		}catch(IOException e)
		{
			Logger.Log("PeerManager", e.toString());
			Logger.Log("PeerManager", "Unable to listen for clients. Make sure there isn't another service " +
					"running on port "+Protocol.DEFAULT_LISTEN_PORT);
		}
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				new MainWindow();
			}
		});
	}
}
