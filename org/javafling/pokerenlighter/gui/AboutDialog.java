package org.javafling.pokerenlighter.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.javafling.pokerenlighter.main.PokerEnlighter;

/**
 *
 * @author Murzea Radu
 */
public class AboutDialog extends JDialog
{
	public AboutDialog (JFrame parent)
	{
		super (parent, "About", true);
		
		setResizable (false);
		setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		
		JPanel content = new JPanel (new BorderLayout ());
		
		JPanel titlePanel = createTitlePanel ();
		content.add (titlePanel, BorderLayout.NORTH);
		
		JPanel middlePanel = new JPanel (new BorderLayout ());
		
		JPanel infoPanel = createInformationPanel ();
		JPanel copyrightPanel = createCopyrightPanel ();
		
		middlePanel.add (infoPanel, BorderLayout.NORTH);
		middlePanel.add (copyrightPanel, BorderLayout.CENTER);
		
		content.add (middlePanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = createButtonPanel ();
		
		content.add (buttonPanel, BorderLayout.SOUTH);

		setContentPane (content);
		pack ();
	}
	
	private JPanel createTitlePanel ()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		
		JLabel icon = new JLabel (new ImageIcon (getClass ().getResource ("images/ace_spades_icon.jpg")));
		panel.add (icon);
		
		JLabel title = new JLabel ("Poker Enlighter");
		title.setFont (new Font ("Verdana", Font.BOLD, 20));
		panel.add (title);

		return panel;
	}
	
	private JPanel createInformationPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (3, 2));
		
		panel.add (new JLabel ("Version:"));
		
		JTextField versionField = new JTextField ("2.0 Alpha build " + PokerEnlighter.BUILD_NUMBER);
		versionField.setEditable (false);
		panel.add (versionField);
		
		panel.add (new JLabel ("Minimum JVM version:"));
		
		JTextField minimumField = new JTextField ("Java " +
												PokerEnlighter.MAJOR_VERSION +
												" update " +
												PokerEnlighter.MINOR_VERSION);
		minimumField.setEditable (false);
		panel.add (minimumField);
		
		panel.add (new JLabel ("Compiled with: "));
		
		JTextField compilerField = new JTextField ("Java 7 update 11");
		compilerField.setEditable (false);
		panel.add (compilerField);
		
		return panel;
	}
	
	private JPanel createCopyrightPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		JPanel webPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		webPanel.add (new JLabel ("Visit Webpage:"));
		JLabelLink webpage = new JLabelLink ("http://pokerenlighter.javafling.org/",
											"http://pokerenlighter.javafling.org/");
		webPanel.add (webpage);
		panel.add (webPanel, BorderLayout.NORTH);
		
		JTextArea licenseinfo = new JTextArea ();
		licenseinfo.setEditable (false);
		licenseinfo.setLineWrap (true);
		licenseinfo.setWrapStyleWord (true);
		licenseinfo.setText ("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.\n\n" +
							"You should have received a copy of the license along with this program; if not, download it from the official website.");
		licenseinfo.setPreferredSize (new Dimension (320, 110));
		
		panel.add (licenseinfo, BorderLayout.CENTER);
		
		//character that represents the copyright sign.
		//used here to avoid issues from compilers/obfuscators/etc.
		//used in the next label (see below)
		char copyright_char = '\u00A9';
		JLabel aboutcopyright = new JLabel ("Copyright " + copyright_char + " 2011 - 2013 Murzea Radu.");
		panel.add (aboutcopyright, BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel createButtonPanel()
	{
		JPanel panel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		JButton okButton = new JButton ("OK");
		
		okButton.addActionListener (new ActionListener ()
		{
			@Override
			public void actionPerformed (ActionEvent a)
			{
				dispose ();
			}
		});
		
		panel.add (okButton);
		
		return panel;
	}
}
