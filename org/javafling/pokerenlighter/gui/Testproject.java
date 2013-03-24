package org.javafling.pokerenlighter.gui;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import java.io.IOException;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Testproject
{
	public static final String errorTitle = "Program error";
	public static final String errorContent = "The program encountered an error at startup";
	
	//specifies minimum major version. Examples: 5 (JRE 5), 6 (JRE 6), 7 (JRE 7) etc.
	private static final int MAJOR_VERSION = 7;
	
	//specifies minimum minor version. Examples: 12 (JRE 6u12), 23 (JRE 6u23), 2 (JRE 7u2) etc.
	private static final int MINOR_VERSION = 1;
	
	//checks if the version of the currently running JVM is bigger than
	//the minimum version required to run this program.
	//returns true if it's ok, false otherwise
	private static boolean checkVersion ()
	{
		//get the JVM version
		String version = System.getProperty ("java.version");

		//extract the major version from it
		int sys_major_version = Integer.parseInt (String.valueOf (version.charAt (2)));
		
		//if the major version is too low (unlikely !!), it's not good
		if (sys_major_version < MAJOR_VERSION)
		{
			return false;
		}
		else if (sys_major_version > MAJOR_VERSION)
		{
			return true;
		}
		else
		{
			//find the underline ( "_" ) in the version string
			int underlinepos = version.lastIndexOf ("_");

			int mv;

			try
			{
				//everything after the underline is the minor version.
				//extract that
				mv = Integer.parseInt (version.substring (underlinepos + 1));
			}
			//if it's not ok, then the version is probably not good
			catch (NumberFormatException e)
			{
				return false;
			}

			//if the minor version passes, wonderful
			return (mv >= MINOR_VERSION);
		}
	}
	
	public static void main(String[] args)
	{
		//check if the minimum version is ok
		if (! checkVersion ())
		{
			final String title = "Poker Enlighter Minimum Version Error";
			final StringBuilder message = new StringBuilder ();
			
			message.append ("JVM version detected: ");
			message.append (System.getProperty ("java.version"));
			message.append (". Minimum version required: ");
			message.append (MAJOR_VERSION);
			message.append (" Update ");
			message.append (MINOR_VERSION);
			message.append (".");
		
			SwingUtilities.invokeLater (new Runnable ()
			{
				@Override
				public void run ()
				{
					GUIUtilities.showErrorDialog (null, message.toString (), title);
				}
			});

			return;
		}
		
		OptionsContainer options = OptionsContainer.getOptionsContainer ();
		final String language = options.getLanguage ();
		final String lnf = options.getLookAndFeel ();
		
		SwingUtilities.invokeLater (new Runnable ()
		{
			@Override
			public void run ()
			{
				try
				{
					switch (lnf)
					{
						case "Nimbus": UIManager.setLookAndFeel (new NimbusLookAndFeel ()); break;
						case "System": UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ()); break;
						case "Motif": UIManager.setLookAndFeel (new MotifLookAndFeel ()); break;
						case "NimROD": UIManager.setLookAndFeel (new NimRODLookAndFeel ()); break;
						case "EaSynth": UIManager.setLookAndFeel (new EaSynthLookAndFeel ()); break;
					}
				}
				catch (UnsupportedLookAndFeelException |
						ClassNotFoundException |
						InstantiationException |
						IllegalAccessException e)
				{
					GUIUtilities.showErrorDialog (null, errorContent + ": " + e.getMessage (), errorTitle);
				}
				
				JFrame.setDefaultLookAndFeelDecorated (true);
				JDialog.setDefaultLookAndFeelDecorated (true);
				
				Language officialLang = null;
		
				switch (language)
				{
					case "English": officialLang = Language.ENGLISH; break;
					case "Romanian": officialLang = Language.ROMANIAN; break;
					default:
					{
						GUIUtilities.showErrorDialog (null, errorContent, errorTitle);
						
						return;
					}
				}

				PEDictionary dictionary = null;
				try
				{
					dictionary = PEDictionary.forLanguage (officialLang);
				}
				catch (IOException ex)
				{
					GUIUtilities.showErrorDialog (null, errorContent, errorTitle);
				}
				
				GUI g = GUI.getGUI (dictionary);

				g.setLocation (GUI.SCREEN_CENTER);

				g.setVisible (true);
			}
		});
	}
}