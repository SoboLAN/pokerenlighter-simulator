package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;

/**
 * Contains all the information about a player necessary for the simulator to do his job.
 * 
 * @author Radu Murzea
 */
public final class PlayerProfile
{
    private HandType handType;
    
    private Range range;
    private Card[] cards;
    
    /**
     * Creates a new player profile. Depending on the type of hand, some null values are allowed for
     * the parameters. More specifically, if you say that the player has a Range-type hand, then you
     * can provide a <code>null</code> value for the cards. You can apply the same principle to the other
     * hand types. Here are some examples of what you CAN do:
     * <pre>
     * new PlayerProfile (HandType.EXACTCARDS, null, myCards);
     * new PlayerProfile (HandType.RANGE, myRange, null);
     * new PlayerProfile (HandType.RANDOM, null, null);
     * </pre>
     * but you can't do this:
     * <pre>
     * new PlayerProfile (null, myRange, myCards);
     * new PlayerProfile (HandType.EXACTCARDS, myRange, null);
     * new PlayerProfile (HandType.RANGE, null, myCards);
     * </pre>
     * 
     * @param handType The hand type.
     * 
     * @param range the range for this profile. If the hand type is NOT HandType.RANGE, then this value
     * may be null or not. Either way, it will be discarded.
     * 
     * @param cards the cards for this profile. If the hand type is NOT HandType.EXACTCARDS, then this value
     * may be null or not. Either way, it will be discarded.
     * 
     * @throws NullPointerException
     * <ul>
     * <li>if handType is null.</li>
     * <li>if handType is HandType.EXACTCARDS and any of the cards is null.</li>
     * <li>if handType is HandType.RANGE and range is null.</li>
     * </ul>
     * 
     * @throws IllegalArgumentException if the length of the cards array is not 2 or 4 or 5 or if cards
     * contains duplicate cards. These conditions will be checked only if handType is HandType.EXACTCARDS.
     */
    public PlayerProfile(HandType handType, Range range, Card[] cards)
    {
        if (handType == null) {
            throw new NullPointerException();
        }

        this.handType = handType;
        
        if (handType == HandType.EXACTCARDS) {
            if (cards == null) {
                throw new NullPointerException();
            } else {
                if (containsNullCards(cards)) {
                    throw new NullPointerException();
                }
                
                if ((cards.length != 2 && cards.length != 4 && cards.length != 5) || containsDuplicates(cards)) {
                    throw new IllegalArgumentException();
                }
            }
            
            this.cards = cards;
        } else if (handType == HandType.RANGE) {
            if (range == null) {
                throw new NullPointerException();
            }
            
            this.range = range;
        }
    }
    
    /**
     * Returns the hand type of this profile.
     * @return the hand type of this profile.
     */
    public HandType getHandType()
    {
        return handType;
    }
    
    /**
     * Returns the range of this profile.
     * @return the range of this profile or NULL if none is set.
     */
    public Range getRange()
    {
        return range;
    }
    
    /**
     * Returns the cards of this profile
     * @return the cards of this profile or NULL if none were set.
     */
    public Card[] getCards()
    {
        return cards;
    }
    
    private boolean containsNullCards(Card[] cards)
    {
        for (Card card : cards) {
            if (card == null) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean containsDuplicates(Card[] cards)
    {
        for (int i = 0; i < cards.length - 1; i++) {
            for (int j = i + 1; j < cards.length; j++) {
                if (cards[i].equals(cards[j])) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
