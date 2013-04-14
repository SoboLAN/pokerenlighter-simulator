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

		content.add (createTitlePanel (), BorderLayout.NORTH);
		
		JTabbedPane tabPane = new JTabbedPane ();
		tabPane.setTabLayoutPolicy (JTabbedPane.WRAP_TAB_LAYOUT);
		
		tabPane.insertTab ("Information", null, createInformationPanel (), "General Information", 0);
		tabPane.insertTab ("Thanks to", null, createThanksPanel (), "List of thank you notes", 1);
		tabPane.insertTab ("Copyright", null, createCopyrightPanel (), "Copyright Information", 2);
		
		content.add (tabPane, BorderLayout.CENTER);

		content.add (createButtonPanel (), BorderLayout.SOUTH);

		setContentPane (content);
		pack ();
	}
	
	private JPanel createTitlePanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		JPanel titlePanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		JLabel icon = new JLabel (new ImageIcon (getClass ().getResource ("images/ace_spades_icon.jpg")));
		titlePanel.add (icon);
		
		JLabel title = new JLabel ("Poker Enlighter");
		title.setFont (new Font ("Verdana", Font.BOLD, 20));
		titlePanel.add (title);
		
		JPanel webPanel = new JPanel (new FlowLayout (FlowLayout.LEFT));
		webPanel.add (new JLabel ("Visit Website:"));
		JLabelLink webpage = new JLabelLink ("http://pokerenlighter.javafling.org/",
											"http://pokerenlighter.javafling.org/");
		webPanel.add (webpage);
		
		panel.add (titlePanel, BorderLayout.CENTER);
		panel.add (webPanel, BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel createInformationPanel ()
	{
		JPanel panel = new JPanel (new GridLayout (4, 2));
		
		panel.add (new JLabel ("Version:"));
		
		JTextField versionField = new JTextField (PokerEnlighter.FULL_VERSION);
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
		
		JTextField compilerField = new JTextField (PokerEnlighter.COMPILED_WITH);
		compilerField.setEditable (false);
		panel.add (compilerField);
		
		panel.add (new JLabel ("Build Date: "));
		
		JTextField buildDateField = new JTextField (PokerEnlighter.BUILD_DATE);
		buildDateField.setEditable (false);
		panel.add (buildDateField);
		
		return panel;
	}
	
	private JPanel createThanksPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		JTextArea thanksinfo = new JTextArea ();
		thanksinfo.setEditable (false);
		thanksinfo.setLineWrap (true);
		thanksinfo.setWrapStyleWord (true);
		thanksinfo.setText ("- the authors of Swing Hacks: Tips and Tools for Killer GUIs; they published the Swing code for status bars in their book.\n\n" + 
							"- the authors of Numerical Recipes - The Art of Scientific Computing; they published a more advanced random number generator in the book.\n\n" +
							"- EaSynth Inc. for providing the EaSynth Look & Feel\n\n" +
							"- Nilo J. Gonzalez for providing the NimROD Look & Feel\n\n" +
							"- David Gilbert for developing and publishing JFreeChart.");
		
		JScrollPane scrollPane = new JScrollPane (thanksinfo);
		scrollPane.setVerticalScrollBarPolicy (JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize (new Dimension (340, 110));
		
		panel.add (new JLabel ("Thanks go to (in no particular order):"), BorderLayout.NORTH);
		
		panel.add (scrollPane, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createCopyrightPanel ()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		JTextArea licenseinfo = new JTextArea ();
		licenseinfo.setEditable (false);
		licenseinfo.setLineWrap (true);
		licenseinfo.setWrapStyleWord (true);
		licenseinfo.setText ("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.\n\n" +
							"You should have received a copy of the license along with this program; if not, download it from the official website.");
		licenseinfo.setPreferredSize (new Dimension (340, 110));
		
		panel.add (licenseinfo, BorderLayout.CENTER);
	
		return panel;
	}
	
	private JPanel createButtonPanel()
	{
		JPanel panel = new JPanel (new BorderLayout ());
		
		//character that represents the copyright sign.
		//used here to avoid issues from compilers/obfuscators/etc.
		//used in the next label (see below)
		char copyright_char = '\u00A9';
		JLabel aboutcopyright = new JLabel ("Copyright " + copyright_char + " 2011 - 2013 Murzea Radu.");
		
		panel.add (aboutcopyright, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		
		JButton closeButton = new JButton ("Close");
		
		buttonPanel.add (closeButton);
		
		closeButton.addActionListener (new ActionListener ()
		{
			@Override
			public void actionPerformed (ActionEvent a)
			{
				dispose ();
			}
		});
		
		panel.add (buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
}
