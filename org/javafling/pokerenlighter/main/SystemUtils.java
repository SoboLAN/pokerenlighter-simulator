package org.javafling.pokerenlighter.main;

/**
 *
 * @author Murzea Radu
 */
public class SystemUtils
{
	public static int getNrOfLogicalCPUs ()
	{
		return Runtime.getRuntime ().availableProcessors ();
	}
	
	//checks if the version of the currently running JVM is bigger than
	//the minimum version required to run this program.
	//returns true if it's ok, false otherwise
	public static boolean checkVersion (int majorVersion, int minorVersion)
	{
		//get the JVM version
		String version = System.getProperty ("java.version");

		//extract the major version from it
		int sys_major_version = Integer.parseInt (String.valueOf (version.charAt (2)));
		
		//if the major version is too low, it's not good
		//this is close to impossible since the JVM wouldn't even be able to load such a class.
		//though, it's best to be thourough
		if (sys_major_version < majorVersion)
		{
			return false;
		}
		else if (sys_major_version > majorVersion)
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
}
