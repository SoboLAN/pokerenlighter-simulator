package org.javafling.pokerenlighter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.javafling.pokerenlighter.main.InternetConnection;
import org.javafling.pokerenlighter.main.InternetConnectionFactory;
import org.javafling.pokerenlighter.main.PokerEnlighter;

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
		updateAction.addActionListener (new UpdateListener ());
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
			AboutDialog ad = new AboutDialog (parent);
			ad.setLocationRelativeTo (parent);
			ad.setVisible (true);
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
	
	//TO DO: move this functionality to a SwingWorker
	private class UpdateListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			String url = "http://pokerenlighter.javafling.org/update.check.php?build=" + PokerEnlighter.BUILD_NUMBER;
			
			InternetConnection conn = InternetConnectionFactory.createDirectConnection (url);
			
			String content = conn.getContent ();
			
			if (content == null || content.equals ("ERROR"))
			{
				GUIUtilities.showErrorDialog (parent, "There was an error while checking for update", "Update Check");
			}
			else if (content.startsWith ("YES"))
			{
				String[] elements = content.split ("\\|");
			
				GUIUtilities.showOKDialog (parent, "An update is available: " + elements[1], "Update Check");
			}
			else if (content.startsWith ("NO"))
			{
				GUIUtilities.showOKDialog (parent, "You have the latest version", "Update Check");
			}
		}
	}
}

