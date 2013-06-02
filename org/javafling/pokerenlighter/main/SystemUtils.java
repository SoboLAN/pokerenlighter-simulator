package org.javafling.pokerenlighter.main;

import com.easynth.lookandfeel.EaSynthLookAndFeel;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
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
 * Provides utility methods for the program.
 * 
 * @author Radu Murzea
 * 
 * @version 1.1
 */
public class SystemUtils
{
	/**
	 * Will return the number of logical CPUs available to the machine.
	 * 
	 * @return the number of logical CPUs available.
	 * 
	 * @since 1.0
	 */
	public static int getNrOfLogicalCPUs ()
	{
		return Runtime.getRuntime ().availableProcessors ();
	}

	/**
	 * Checks if the version of the JVM fits with the required version.
	 * 
	 * @param majorVersion the minimum required JVM version: 5 (JRE 5), 6 (JRE 6) etc.
	 * @param minorVersion the minimum required JVM version: 4 (JRE 7u4), 21 (JRE 6u21) etc.
	 * 
	 * @return true if the JVM version is ok, false otherwise.
	 * 
	 * @since 1.0
	 */
	public static boolean checkVersion (int majorVersion, int minorVersion)
	{
		String version = System.getProperty ("java.version");

		//extract the major version
		int sys_major_version = Integer.parseInt (String.valueOf (version.charAt (2)));
		
		//the JVM won't even be able to load the class if the major version is older
		//so no check is necessary
		if (sys_major_version > majorVersion)
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
			return (mv >= minorVersion);
		}
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
	public static void setLNF (String lnf) throws Exception
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
