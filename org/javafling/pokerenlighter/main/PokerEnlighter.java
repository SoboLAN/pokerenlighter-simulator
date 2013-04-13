package org.javafling.pokerenlighter.main;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.javafling.pokerenlighter.gui.GUI;
import org.javafling.pokerenlighter.gui.GUIUtilities;
import org.javafling.pokerenlighter.gui.Language;
import org.javafling.pokerenlighter.gui.OptionsContainer;
import org.javafling.pokerenlighter.gui.PEDictionary;

public final class PokerEnlighter
{
	//specifies minimum major version. Examples: 5 (JRE 5), 6 (JRE 6), 7 (JRE 7) etc.
	public static final int MAJOR_VERSION = 7;
	
	//specifies minimum minor version. Examples: 12 (JRE 6u12), 23 (JRE 6u23), 2 (JRE 7u2) etc.
	public static final int MINOR_VERSION = 1;
	
	public static final String FULL_VERSION = "2.0 Alpha build 438";
	
	public static final int BUILD_NUMBER = 438;
	
	public static final String COMPILED_WITH = "Java 7 Update 11";
	
	//UNIX timestamp: 1365369999
	public static final String BUILD_DATE = "07 Apr 2013 21:26:39 AM UTC";

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
		
		SwingUtilities.invokeLater (new PokerEnlighterStarter (language, lnf));
	}
}

class PokerEnlighterStarter implements Runnable
{
	private static final String errorTitle = "Program error";
	private static final String errorContent = "The program encountered an error at startup: ";
	private String language, lnf;

	public PokerEnlighterStarter (String language, String lnf)
	{
		this.language = language;
		this.lnf = lnf;
	}
		
	@Override
	public void run ()
	{
		PEDictionary dictionary = null;
		
		try
		{
			setLNF ();

			dictionary = PEDictionary.forLanguage (getLanguage ());
		}
		catch (Exception ex)
		{
			GUIUtilities.showErrorDialog (null, errorContent + ex.getMessage (), errorTitle);
		}
				
		GUI g = GUI.getGUI (dictionary);

		g.setLocationToCenterOfScreen ();

		g.setVisible (true);
	}
		
	private void setLNF () throws Exception
	{
		switch (lnf)
		{
			case "Nimbus": UIManager.setLookAndFeel (new NimbusLookAndFeel ()); break;
			case "System": UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ()); break;
			case "Motif": UIManager.setLookAndFeel (new MotifLookAndFeel ()); break;
			case "NimROD": UIManager.setLookAndFeel (new NimRODLookAndFeel ()); break;
			case "EaSynth": UIManager.setLookAndFeel (new EaSynthLookAndFeel ()); break;
			default: throw new RuntimeException ();
		}
				
		JFrame.setDefaultLookAndFeelDecorated (true);
		JDialog.setDefaultLookAndFeelDecorated (true);
	}
		
	private Language getLanguage () throws RuntimeException
	{
		switch (language)
		{
			case "English": return Language.ENGLISH;
			case "Romanian": return Language.ROMANIAN;
			default: throw new RuntimeException ();
		}
	}
}