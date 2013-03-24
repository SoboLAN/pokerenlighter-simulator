package org.javafling.pokerenlighter.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Murzea Radu
 */
public class AboutDialog
{
	private JFrame parent;
	
	private JDialog aboutdialog;
	
	AboutDialog (JFrame parent)
	{
		this.parent = parent;
	}
	
	//creates the about box (a JPanel with an image, some text and an OK button)
	private JPanel createAboutBox ()
	{
		JPanel panel = new JPanel ();
		panel.setLayout (new BorderLayout ());
		
		JPanel topPanel = new JPanel (new BorderLayout ());
		
		JPanel namePanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		JLabel aboutname = new JLabel ("Poker Enlighter");
		namePanel.add (aboutname);
		topPanel.add (namePanel, BorderLayout.NORTH);
		
		JPanel imageInfoPanel = new JPanel (new GridLayout (1, 2, 10, 5));
		
		JLabel aboutImage = new JLabel (new ImageIcon (getClass (). getResource ("images/info.png")));
		imageInfoPanel.add (aboutImage);
		
		JPanel infoPanel = new JPanel (new GridLayout (3, 1));

		JLabel aboutversion = new JLabel ("Version: 2.0 Alpha build 438");
		infoPanel.add (aboutversion);

		JLabel aboutauthor = new JLabel ("Author: Murzea Radu");
		infoPanel.add (aboutauthor);

		JLabel aboutdate = new JLabel ("Build Date: 23 January 2013");
		infoPanel.add (aboutdate);
		
		imageInfoPanel.add (infoPanel);
		
		topPanel.add (imageInfoPanel, BorderLayout.CENTER);
		
		panel.add (topPanel, BorderLayout.NORTH);
		
		JPanel licensePanel = new JPanel (new BorderLayout ());

		JTextArea licenseinfo = new JTextArea ();
		licenseinfo.setEditable (false);
		licenseinfo.setLineWrap (true);
		licenseinfo.setWrapStyleWord (true);
		licenseinfo.setText ("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.\n\n" +
							"You should have received a copy of the license along with this program; if not, download it from the official website.");
		licenseinfo.setPreferredSize (new Dimension (350, 110));
		
		licensePanel.add (licenseinfo, BorderLayout.CENTER);

		//character that represents the copyright sign.
		//used here to avoid issues from compilers/obfuscators/etc.
		//used in the next label (see below)
		char copyright_char = '\u00A9';
		JLabel aboutcopyright = new JLabel ("Copyright " + copyright_char + " 2011 - 2013 Murzea Radu. All rights reserved.");
		licensePanel.add (aboutcopyright, BorderLayout.SOUTH);
		
		panel.add (licensePanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));

		JButton aboutclose = new JButton ("OK");
		buttonPanel.add (aboutclose);
		
		panel.add (buttonPanel, BorderLayout.SOUTH);

		//when the close button is pressed, the dialog is disposed
		aboutclose.addActionListener (new ActionListener ()
		{
			@Override public void actionPerformed (ActionEvent a)
			{
				aboutdialog.dispose ();
			}
		});

		//but the name of the program must be bigger
		aboutname.setFont (new Font ("Verdana", Font.BOLD, 24));
		
		return panel;
	}
	
	void display ()
	{
		aboutdialog = new JDialog (parent, "About", true);
		aboutdialog.setResizable (false);
		aboutdialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
		
		aboutdialog.setContentPane (createAboutBox ());
		aboutdialog.pack ();
		
		aboutdialog.setLocationRelativeTo (parent);
		aboutdialog.setVisible (true);
	}
}
