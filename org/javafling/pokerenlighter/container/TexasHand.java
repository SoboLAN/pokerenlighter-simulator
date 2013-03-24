package org.javafling.pokerenlighter.container;

import org.javafling.pokerenlighter.combination.Card;

/** Represents a Texas Hold'em hand.
*/
public class TexasHand extends PokerHand
{
	//storage for the 2 cards
	private char c1, c2;

	//store if the 2 cards are suited or not
	private boolean suited;

	/** Creates a TexasHand object using the 2 cards provided as parameters.
	*
	* @param a The first card of the hand.
	* @param b The second card of the hand.
	* @param s Specifies whether the hand is suited or not.
	*
	* @exception IllegalArgumentException If a or b do not represent a valid card. Another situation
	* is when a and b are equal and s is true.
	*/
	public TexasHand (char a, char b, boolean s)
	{
		//since this is a subclass, let's construct the superclass first
		super ();

		//let's test the parameters for validity.
		if ((a == b && s) || (Card.getRank (a) == 0 || Card.getRank (b) == 0))
		{
			throw new IllegalArgumentException ();
		}

		c1 = a;
		c2 = b;
		suited = s;
	}
	
	/** Returns the x-th card that is part of the hand.
	*
	* @param x The card to be returned is provided by this parameter. Since the hand
	* consists of 2 cards, only the values 0 or 1 are accepted.
	*
	* @return the requested card (the first or second).
	*
	* @exception IllegalArgumentException If x is outside the acceptable range.
	*/
	public final char getCard (int x)
	{
		switch (x)
		{
			case 0: return c1;
			case 1: return c2;
			default: throw new IllegalArgumentException ();
		}
	}

	/** Determines if this Texas Hold'em hand is suited or not.
	*
	* @return true if the hand is suited, false otherwise.
	*/
	public final boolean getSuit ()
	{
		return suited;
	}

	/** A little alternative to the toString method (toString will return AA0 while
	* this returns AA offsuit).
	*
	* @return a string representation of this hand.
	*/
	public final String prettyPrint ()
	{
		String suit = (suited) ? "suited" : "offsuit";
		return Character.toString (c1) + Character.toString (c2) + " " + suit;
	}

	/** Compares two TexasHand objects.
	*
	* @param thc the TexasHand to compare.
	*
	* @return true if the 2 TexasHands are equal (they contain the same cards and are both suited or offsuit).
	*/
	public boolean compare (TexasHand thc)
	{		
		if ((c1 != thc.getCard (0) || c2 != thc.getCard (1)) && (c1 != thc.getCard (1) || c2 != thc.getCard (0)))
		{
			return false;
		}

		if (suited ^ thc.getSuit ())
		{
			return false;
		}

		return true;
	}

	/** Compares two TexasHand objects.
	*
	* @param th the TexasHand to compare.
	*
	* @return true if the 2 TexasHands are equal (they contain the same cards and are both suited or offsuit),
	* false otherwise. The difference between this and the compare method is that, in this case, the 2 hands
	* must have equal number of occurrences and wins.
	*/
	@Override public boolean equals (Object th)
	{
		if (th == null)
		{
			return false;
		}

		if (! getClass ().equals (th.getClass ()))
		{
			return false;
		}

		TexasHand param = (TexasHand) th;

		if (super.getOccurrence () != param.getOccurrence () || super.getWins () != param.getWins ())
		{
			return false;
		}

	//	return compareTo (param);
		
		//TODO: recheck this
		
		return true;
	}

	/** Returns a hash code representation of this TexasHand.
	*
	* @return a hash code representation of this TexasHand. Value will be 17 and 456 inclusively.
	*/
	@Override public int hashCode ()
	{
		int rez = (super.getOccurrence () + super.getWins ()) % 500;

		rez += (Card.getRank (c1) + Card.getRank (c2));

		rez += ((suited) ? 29 : 13);

		return rez;
	}

	/** Returns a string representation of this hand.
	*
	* @return a string representation of this hand. Length will be 3: first two are the cards, the third is the suit
	* represented by '1' (suited) or '0' (offsuit).
	*/
	@Override public String toString ()
	{
	    char suit = (suited) ? '1' : '0';
		return Character.toString (c1) + Character.toString (c2) + Character.toString (suit);
	}
}