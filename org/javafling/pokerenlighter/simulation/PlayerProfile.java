package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;

/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public final class PlayerProfile
{
	private HandType handType;
	
	private Range range;
	private Card[] cards;
	
	/**
	 * Creates a new player profile. Depending on the type of hand, some null values are allowed for
	 * the parameters. More specifically, you can do this:
	 * <pre>
	 * new PlayerProfile (HandType.EXACTCARDS, null, myCards);
	 * new PlayerProfile (HandType.RANGE, myRange, null);
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
	 * @throws NullPointerException if handType is null. Also, if handType is HandType.EXACTCARDS and any
	 * of the cards is null. Also, if range is null in the case handType is HandType.RANGE.
	 * 
	 * @throws IllegalArgumentException if handType is HandType.EXACTCARDS and the length of the cards array
	 * is not 2 or 4. Also, if cards contains duplicate cards.
	 */
	public PlayerProfile (HandType handType, Range range, Card[] cards)
	{
		if (handType == null)
		{
			throw new NullPointerException ();
		}

		this.handType = handType;
		
		if (handType == HandType.EXACTCARDS)
		{
			if (cards == null)
			{
				throw new NullPointerException ();
			}
			else
			{
				if (! testForNull (cards))
				{
					throw new NullPointerException ();
				}
				
				if ((cards.length != 2 && cards.length != 4) || ! testDuplicates (cards))
				{
					throw new IllegalArgumentException ();
				}
			}
			
			this.cards = cards;
		}
		else if (handType == HandType.RANGE)
		{
			if (range == null)
			{
				throw new NullPointerException ();
			}
			
			this.range = range;
		}
	}
	
	private boolean testForNull (Card[] cards)
	{
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i] == null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean testDuplicates (Card[] cards)
	{
		for (int i = 0; i < cards.length - 1; i++)
		{
			for (int j = i + 1; j < cards.length; j++)
			{
				if (cards[i].equals (cards[j]))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public HandType getHandType ()
	{
		return handType;
	}
	
	public Range getRange ()
	{
		return range;
	}
	
	public Card[] getCards ()
	{
		return cards;
	}
}
