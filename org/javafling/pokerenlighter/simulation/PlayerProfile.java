package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;

/**
 *
 * @author Murzea Radu
 */
public final class PlayerProfile
{
	private HandType handType;
	
	private Range range;
	private Card[] cards;
	
	public PlayerProfile (HandType handType)
	{
		this.handType = handType;
	}
	
	public void setHandType (HandType handType)
	{
		this.handType = handType;
	}
	
	public void setRange (Range range)
	{
		this.range = range;
	}
	
	public void setCards (Card[] cards)
	{
		this.cards = cards;
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
