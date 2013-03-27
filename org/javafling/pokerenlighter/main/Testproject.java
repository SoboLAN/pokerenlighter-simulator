package org.javafling.pokerenlighter.main;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import java.io.IOException;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.javafling.pokerenlighter.gui.GUI;
import org.javafling.pokerenlighter.gui.GUIUtilities;
import org.javafling.pokerenlighter.gui.Language;
import org.javafling.pokerenlighter.gui.OptionsContainer;
import org.javafling.pokerenlighter.gui.PEDictionary;
import org.javafling.pokerenlighter.simulation.PokerType;

public final class Testproject
{
	private static final String errorTitle = "Program error";
	private static final String errorContent = "The program encountered an error at startup";
	
	//specifies minimum major version. Examples: 5 (JRE 5), 6 (JRE 6), 7 (JRE 7) etc.
	private static final int MAJOR_VERSION = 7;
	
	//specifies minimum minor version. Examples: 12 (JRE 6u12), 23 (JRE 6u23), 2 (JRE 7u2) etc.
	private static final int MINOR_VERSION = 1;

	public static void main(String[] args)
	{
		//check if the minimum version is ok
		if (! SystemUtils.checkVersion (MAJOR_VERSION, MINOR_VERSION))
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