package org.javafling.pokerenlighter.main;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.javafling.pokerenlighter.simulation.SystemUtils;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import org.javafling.pokerenlighter.gui.GUI;
import org.javafling.pokerenlighter.gui.GUIUtilities;
import org.javafling.pokerenlighter.gui.Language;
import org.javafling.pokerenlighter.gui.OptionsContainer;
import org.javafling.pokerenlighter.gui.PEDictionary;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.skin.BusinessBlueSteelSkin;
import org.jvnet.substance.skin.BusinessSkin;
import org.jvnet.substance.skin.ChallengerDeepSkin;
import org.jvnet.substance.skin.CremeCoffeeSkin;
import org.jvnet.substance.skin.CremeSkin;
import org.jvnet.substance.skin.DustCoffeeSkin;
import org.jvnet.substance.skin.DustSkin;
import org.jvnet.substance.skin.EmeraldDuskSkin;
import org.jvnet.substance.skin.MagmaSkin;
import org.jvnet.substance.skin.MistAquaSkin;
import org.jvnet.substance.skin.MistSilverSkin;
import org.jvnet.substance.skin.ModerateSkin;
import org.jvnet.substance.skin.NebulaBrickWallSkin;
import org.jvnet.substance.skin.NebulaSkin;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.skin.OfficeSilver2007Skin;
import org.jvnet.substance.skin.RavenGraphiteSkin;
import org.jvnet.substance.skin.RavenSkin;
import org.jvnet.substance.skin.SaharaSkin;
import org.jvnet.substance.skin.TwilightSkin;

/**
 * Main entry point of Poker Enlighter. Contains all relevant information about the program.
 * 
 * @author Radu Murzea
 * 
 * @version 1.0
 */
public final class PokerEnlighter implements Runnable
{
	/**
	 * Specifies the minimum major version needed to run this program.
	 */
	public static final int MAJOR_VERSION = 7;
	
	/**
	 * Specifies the minimum minor version needed to run this program.
	 */
	public static final int MINOR_VERSION = 1;
	
	/**
	 * Represents the full version string of this current release.
	 */
	public static final String FULL_VERSION = "2.1 build 488";
	
	/**
	 * The build number of this release.
	 */
	public static final int BUILD_NUMBER = 488;
	
	/**
	 * Version string of the JDK version used to compile this release.
	 */
	public static final String COMPILED_WITH = "Java 7 Update 11";

	/**
	 * String containing the compile date of this release.
	 */
	public static final String BUILD_DATE = "11 Jun 2013 07:05 AM UTC";
	
	//if an error occurs in this class, these Strings will be used in the error dialog that appears
	private static final String errorTitle = "Program error";
	private static final String errorContent = "The program encountered an error at startup: ";

	//references the options and the dictionary
	private static OptionsContainer options;
	private static PEDictionary dictionary;
	
	/**
	 * The main method. It will check the minimum version required to run this release, it will retrieve
	 * all options, it will set the chosen Look & Feel and will display the GUI.
	 * 
	 * @param args command-line arguments. Any values provided here will be ignored.
	 */
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
		
			GUIUtilities.showErrorDialog (null, message.toString (), title);

			return;
		}
		
		options = OptionsContainer.getOptionsContainer ();
		
		//get the language
		try
		{
			Language language = null;
			switch (options.getLanguage ())
			{
				case "English": language = Language.ENGLISH; break;
				case "Romanian": language = Language.ROMANIAN; break;
				default: throw new RuntimeException ("invalid language");
			}
			
			dictionary = PEDictionary.forLanguage (language);
		}
		catch (Exception ex)
		{
			GUIUtilities.showErrorDialog (null, errorContent + ex.getMessage (), errorTitle);
	
			return;
		}
		
		//show the GUI on the EDT
		SwingUtilities.invokeLater (new PokerEnlighter ());
	}
	
	/**
	 * Will be called by the main method.
	 * 
	 * @since 1.0
	 */
	@Override
	public void run ()
	{
		//set the look & feel
		try
		{
			setLNF (options.getLookAndFeel ());
		}
		catch (Exception ex)
		{
			GUIUtilities.showErrorDialog (null, errorContent + ex.getMessage (), errorTitle);
		}

		//create and show the GUI
		GUI g = GUI.getGUI (dictionary);
		g.setLocationToCenterOfScreen ();
		g.setVisible (true);
	}
	
	/**
	 * Sets the Look & Feel specified as parameter.
	 * 
	 * @param lnf the Look & Feel
	 * 
	 * @throws Exception if the Look & Feel is not among the accepted values.
	 * 
	 * @since 1.1
	 */
	public void setLNF (String lnf) throws Exception
	{
		switch (lnf)
		{
			case "Nimbus": UIManager.setLookAndFeel (new NimbusLookAndFeel ()); break;
			case "System": UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ()); break;
			case "Motif": UIManager.setLookAndFeel (new MotifLookAndFeel ()); break;
			case "NimROD": UIManager.setLookAndFeel (new NimRODLookAndFeel ()); break;
			case "EaSynth": UIManager.setLookAndFeel (new EaSynthLookAndFeel ()); break;
			case "SeaGlass": UIManager.setLookAndFeel (new SeaGlassLookAndFeel ()); break;
			case "Substance-Business": SubstanceLookAndFeel.setSkin (new BusinessSkin ()); break;
			case "Substance-BusinessBlack": SubstanceLookAndFeel.setSkin (new BusinessBlackSteelSkin ()); break;
			case "Substance-BusinessBlue": SubstanceLookAndFeel.setSkin (new BusinessBlueSteelSkin ()); break;
			case "Substance-Challenger": SubstanceLookAndFeel.setSkin (new ChallengerDeepSkin ()); break;
			case "Substance-Creme": SubstanceLookAndFeel.setSkin (new CremeSkin ()); break;
			case "Substance-CremeCoffee": SubstanceLookAndFeel.setSkin (new CremeCoffeeSkin ()); break;
			case "Substance-Dust": SubstanceLookAndFeel.setSkin (new DustSkin ()); break;
			case "Substance-DustCoffee": SubstanceLookAndFeel.setSkin (new DustCoffeeSkin ()); break;
			case "Substance-Emerald": SubstanceLookAndFeel.setSkin (new EmeraldDuskSkin ()); break;
			case "Substance-Magma": SubstanceLookAndFeel.setSkin (new MagmaSkin ()); break;
			case "Substance-MistAqua": SubstanceLookAndFeel.setSkin (new MistAquaSkin ()); break;
			case "Substance-MistSilver": SubstanceLookAndFeel.setSkin (new MistSilverSkin ()); break;
			case "Substance-Moderate": SubstanceLookAndFeel.setSkin (new ModerateSkin ()); break;
			case "Substance-Nebula": SubstanceLookAndFeel.setSkin (new NebulaSkin ()); break;
			case "Substance-NebulaBrick": SubstanceLookAndFeel.setSkin (new NebulaBrickWallSkin ()); break;
			case "Substance-OfficeBlue": SubstanceLookAndFeel.setSkin (new OfficeBlue2007Skin ()); break;
			case "Substance-OfficeSilver": SubstanceLookAndFeel.setSkin (new OfficeSilver2007Skin ()); break;
			case "Substance-Raven": SubstanceLookAndFeel.setSkin (new RavenSkin ()); break;
			case "Substance-RavenGraphite": SubstanceLookAndFeel.setSkin (new RavenGraphiteSkin ()); break;
			case "Substance-Sahara": SubstanceLookAndFeel.setSkin (new SaharaSkin ()); break;
			case "Substance-Twilight": SubstanceLookAndFeel.setSkin (new TwilightSkin ()); break;
			default: throw new IllegalArgumentException ("invalid look-and-feel");
		}
				
		JFrame.setDefaultLookAndFeelDecorated (true);
		JDialog.setDefaultLookAndFeelDecorated (true);
	}
}