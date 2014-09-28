package org.javafling.pokerenlighter.simulation;

/**
 * Represents all the different types of poker suppported by Poker Enlighter.
 * 
 * @author Radu Murzea
 */
public enum PokerType
{
    TEXAS_HOLDEM, OMAHA, OMAHA_HILO;
    
    @Override
    public String toString()
    {
        if (this == TEXAS_HOLDEM) {
            return "Texas Hold'em";
        } else if (this == OMAHA) {
            return "Omaha";
        } else if (this == OMAHA_HILO) {
            return "Omaha Hi/Lo";
        } else {
            return "";
        }
    }
}
