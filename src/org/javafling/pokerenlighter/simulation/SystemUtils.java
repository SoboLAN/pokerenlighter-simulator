package org.javafling.pokerenlighter.simulation;

/**
 * Provides utility methods for the program.
 * 
 * @author Radu Murzea
 */
public class SystemUtils
{
    /**
     * Will return the number of logical CPUs available to the machine.
     * 
     * @return the number of logical CPUs available.
     */
    public static int getNrOfLogicalCPUs()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
