package org.xfer.ui.panels;

import java.awt.BorderLayout;

import javax.swing.tree.DefaultMutableTreeNode;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tree.WebTree;
import com.alee.laf.tree.WebTreeModel;

public class FilesView extends WebPanel
{
	protected WebTree			fileTree;
	protected WebScrollPane	scrollPane;
	public FilesView()
	{
		fileTree = new WebTree();
		fileTree.setEditable(false);
		fileTree.setSelectionMode(WebTree.DISCONTIGUOUS_TREE_SELECTION);
		fileTree.setPlainFont();
		
		WebTreeModel model = (WebTreeModel)fileTree.getModel();
		model.setRoot(null);
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject("Node 1");
		
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode();
		node2.setUserObject("Node2");
		node.add(node2);
		model.setRoot(node);
		
		scrollPane = new WebScrollPane( fileTree );
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
}
