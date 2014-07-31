package org.javafling.pokerenlighter.simulation;

/**
 * Represents all the possible hand types a player can have.
 * 
 * @author Radu Murzea
 * 
 * @version 1.0
 */
public enum HandType
{
    RANDOM, RANGE, EXACTCARDS;
    
    @Override
    public String toString()
    {
        if (this == RANDOM) {
            return "Random";
        } else if (this == RANGE) {
            return "Range";
        } else if (this == EXACTCARDS) {
            return "Exact Cards";
        } else {
            return "";
        }
    }
}
