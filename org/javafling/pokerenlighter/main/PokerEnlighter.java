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
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceChallengerDeepLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceEmeraldDuskLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel;

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
	public static final int MINOR_VERSION = 2;
	
	/**
	 * Represents the full version string of this current release.
	 */
	public static final String FULL_VERSION = "2.1.2 build 490";
	
	/**
	 * The build number of this release.
	 */
	public static final int BUILD_NUMBER = 490;
	
	/**
	 * Version string of the JDK version used to compile this release.
	 */
	public static final String COMPILED_WITH = "Java 7 Update 11";

	/**
	 * String containing the compile date of this release.
	 */
	public static final String BUILD_DATE = "11 Jun 2013 09:26 AM UTC";
	
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
			case "Substance-Business": UIManager.setLookAndFeel (new SubstanceBusinessLookAndFeel ()); break;
			case "Substance-BusinessBlack": UIManager.setLookAndFeel (new SubstanceBusinessBlackSteelLookAndFeel ()); break;
			case "Substance-BusinessBlue": UIManager.setLookAndFeel (new SubstanceBusinessBlueSteelLookAndFeel ()); break;
			case "Substance-Challenger": UIManager.setLookAndFeel (new SubstanceChallengerDeepLookAndFeel ()); break;
			case "Substance-Creme": UIManager.setLookAndFeel (new SubstanceCremeLookAndFeel ()); break;
			case "Substance-CremeCoffee": UIManager.setLookAndFeel (new SubstanceCremeCoffeeLookAndFeel ()); break;
			case "Substance-Dust": UIManager.setLookAndFeel (new SubstanceDustLookAndFeel ()); break;
			case "Substance-DustCoffee": UIManager.setLookAndFeel (new SubstanceDustCoffeeLookAndFeel ()); break;
			case "Substance-Emerald": UIManager.setLookAndFeel (new SubstanceEmeraldDuskLookAndFeel ()); break;
			case "Substance-Magellan": UIManager.setLookAndFeel (new SubstanceMagellanLookAndFeel ()); break;
			case "Substance-MistAqua": UIManager.setLookAndFeel (new SubstanceMistAquaLookAndFeel ()); break;
			case "Substance-MistSilver": UIManager.setLookAndFeel (new SubstanceMistSilverLookAndFeel ()); break;
			case "Substance-Moderate": UIManager.setLookAndFeel (new SubstanceModerateLookAndFeel ()); break;
			case "Substance-Nebula": UIManager.setLookAndFeel (new SubstanceNebulaLookAndFeel ()); break;
			case "Substance-NebulaBrick": UIManager.setLookAndFeel (new SubstanceNebulaBrickWallLookAndFeel ()); break;
			case "Substance-OfficeBlue": UIManager.setLookAndFeel (new SubstanceOfficeBlue2007LookAndFeel ()); break;
			case "Substance-OfficeSilver": UIManager.setLookAndFeel (new SubstanceOfficeSilver2007LookAndFeel ()); break;
			case "Substance-Raven": UIManager.setLookAndFeel (new SubstanceRavenLookAndFeel ()); break;
			case "Substance-Sahara": UIManager.setLookAndFeel (new SubstanceSaharaLookAndFeel ()); break;
			case "Substance-Twilight": UIManager.setLookAndFeel (new SubstanceTwilightLookAndFeel ()); break;
			default: throw new IllegalArgumentException ("invalid look-and-feel");
		}
				
		JFrame.setDefaultLookAndFeelDecorated (true);
		JDialog.setDefaultLookAndFeelDecorated (true);
	}
}