package org.javafling.pokerenlighter.main;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
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
	public static final int MINOR_VERSION = 4;
	
	/**
	 * Represents the full version string of this current release.
	 */
	public static final String FULL_VERSION = "2.3 build 538";
	
	/**
	 * The build number of this release.
	 */
	public static final int BUILD_NUMBER = 538;
	
	/**
	 * Version string of the JDK version used to compile this release.
	 */
	public static final String COMPILED_WITH = "Java 7 Update 51";

	/**
	 * String containing the compile date of this release.
	 */
	public static final String BUILD_DATE = "26 Jul 2014 04:55 PM UTC";
	
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
		LookAndFeel lnfObject = null;
		
		switch (lnf)
		{
			case "Nimbus": lnfObject = new NimbusLookAndFeel (); break;
			case "System": UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ()); break;
			case "Motif": lnfObject = new MotifLookAndFeel (); break;
			case "NimROD": lnfObject = new NimRODLookAndFeel (); break;
			case "EaSynth": lnfObject = new EaSynthLookAndFeel (); break;
			case "SeaGlass": lnfObject = new SeaGlassLookAndFeel (); break;
			case "Substance-Business": lnfObject = new SubstanceBusinessLookAndFeel (); break;
			case "Substance-BusinessBlack": lnfObject = new SubstanceBusinessBlackSteelLookAndFeel (); break;
			case "Substance-BusinessBlue": lnfObject = new SubstanceBusinessBlueSteelLookAndFeel (); break;
			case "Substance-Challenger": lnfObject = new SubstanceChallengerDeepLookAndFeel (); break;
			case "Substance-Creme": lnfObject = new SubstanceCremeLookAndFeel (); break;
			case "Substance-CremeCoffee": lnfObject = new SubstanceCremeCoffeeLookAndFeel (); break;
			case "Substance-Dust": lnfObject = new SubstanceDustLookAndFeel (); break;
			case "Substance-DustCoffee": lnfObject = new SubstanceDustCoffeeLookAndFeel (); break;
			case "Substance-Emerald": lnfObject = new SubstanceEmeraldDuskLookAndFeel (); break;
			case "Substance-Magellan": lnfObject = new SubstanceMagellanLookAndFeel (); break;
			case "Substance-MistAqua": lnfObject = new SubstanceMistAquaLookAndFeel (); break;
			case "Substance-MistSilver": lnfObject = new SubstanceMistSilverLookAndFeel (); break;
			case "Substance-Moderate": lnfObject = new SubstanceModerateLookAndFeel (); break;
			case "Substance-Nebula": lnfObject = new SubstanceNebulaLookAndFeel (); break;
			case "Substance-NebulaBrick": lnfObject = new SubstanceNebulaBrickWallLookAndFeel (); break;
			case "Substance-OfficeBlue": lnfObject = new SubstanceOfficeBlue2007LookAndFeel (); break;
			case "Substance-OfficeSilver": lnfObject = new SubstanceOfficeSilver2007LookAndFeel (); break;
			case "Substance-Raven": lnfObject = new SubstanceRavenLookAndFeel (); break;
			case "Substance-Sahara": lnfObject = new SubstanceSaharaLookAndFeel (); break;
			case "Substance-Twilight": lnfObject = new SubstanceTwilightLookAndFeel (); break;
			default: throw new IllegalArgumentException ("invalid look-and-feel");
		}
		
		if (lnfObject != null)
		{
			UIManager.setLookAndFeel (lnfObject);
		}
		
		JFrame.setDefaultLookAndFeelDecorated (true);
		JDialog.setDefaultLookAndFeelDecorated (true);
	}
}