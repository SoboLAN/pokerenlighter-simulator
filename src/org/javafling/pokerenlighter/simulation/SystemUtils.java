package org.javafling.pokerenlighter.simulation;

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
    public static int getNrOfLogicalCPUs()
    {
        return Runtime.getRuntime().availableProcessors();
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
    public static boolean checkVersion(int majorVersion, int minorVersion)
    {
        String version = System.getProperty("java.version");

        //extract the major version
        int sys_major_version = Integer.parseInt(String.valueOf(version.charAt(2)));
        
        //the JVM won't even be able to load the class if the major version is older
        //so no check is necessary
        if (sys_major_version > majorVersion) {
            return true;
        } else {
            //find the underline ( "_" ) in the version string
            int underlinepos = version.lastIndexOf("_");

            int mv;

            try {
                //everything after the underline is the minor version.
                //extract that
                mv = Integer.parseInt(version.substring(underlinepos + 1));
                
            //if it's not ok, then the version is probably not good
            } catch (NumberFormatException e) {
                return false;
            }

            //if the minor version passes, wonderful
            return (mv >= minorVersion);
        }
    }
}
