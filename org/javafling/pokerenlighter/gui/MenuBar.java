package org.javafling.pokerenlighter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Murzea Radu
 */
public class MenuBar
{
	private JFrame parent;
	
	private JMenuBar menuBar;
	
	private JMenu fileMenu, helpMenu;
	
	private JMenuItem exitAction, aboutAction, prefsAction, updateAction;
	
	public MenuBar (JFrame parent)
	{
		this.parent = parent;
		
		menuBar = new JMenuBar ();

		fileMenu = new JMenu ("File");
		helpMenu = new JMenu ("Help");
		
		fileMenu.setMnemonic (KeyEvent.VK_F);
		helpMenu.setMnemonic (KeyEvent.VK_H);

		menuBar.add (fileMenu);
		menuBar.add (helpMenu);

		exitAction = new JMenuItem ("Exit");
		prefsAction = new JMenuItem ("Preferences");
		aboutAction = new JMenuItem ("About");
		updateAction = new JMenuItem ("Check for Update");

		fileMenu.add (prefsAction);
		fileMenu.add (updateAction);
		fileMenu.add (exitAction);
		helpMenu.add (aboutAction);

		aboutAction.addActionListener (new AboutListener ());
		prefsAction.addActionListener (new PreferencesListener ());
		exitAction.addActionListener (new ExitListener ());
	}
	
	public JMenuBar getMenuBar ()
	{
		return menuBar;
	}

	private class AboutListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			new AboutDialog (parent).display ();
		}
	}
	
	private class PreferencesListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			new PreferencesDialog (parent).display ();
		}
	}
	
	private class ExitListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			parent.dispose ();
		}
	}
}

