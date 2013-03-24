package org.javafling.pokerenlighter.container;

import org.javafling.pokerenlighter.combination.Card;

/** This represents an Omaha poker hand. It consists of 4 distinct cards.
*/
public class OmahaHand extends PokerHand
{
	//storage for the cards that make up the hand
	private Card[] cards;

	/** Creates a OmahaHand object using the 4 cards provided as parameters.
	*
	* @param a The first card of the hand.
	* @param b The second card of the hand.
	* @param c The third card of the hand.
	* @param d The fourth card of the hand.
	*/
	public OmahaHand (Card a, Card b, Card c, Card d)
	{
		super ();

		//copy parameter cards to field
		cards = new Card[4];
		cards[0] = a;
		cards[1] = b;
		cards[2] = c;
		cards[3] = d;
		
		int i, j;

		//sort the hands descendently by rank and ascendently by color (if ranks are equal)
		for (i = 0; i < 3; ++i)
		{
			for (j = i + 1; j < 4; ++j)
			{
				if (cards[i].getRank () < cards[j].getRank ())
				{
					Card aux = cards[i]; cards[i] = cards[j]; cards[j] = aux;
				}
				else if (cards[i].getRank () == cards[j].getRank ())
				{
					if (cards[i].getColor () > cards[j].getColor ())
					{
						Card aux = cards[i]; cards[i] = cards[j]; cards[j] = aux;
					}
				}
			}
		}
	}
	
	/** Returns the x-th card that is part of the hand.
	*
	* @param x The card to be returned is provided by this parameter.
	* Since the hand consists of 4 cards, only the values [0; 3] are accepted. If x is outside of this range, null is returned.
	*
	* @return The requested card or null if x is outside the accepted range.
	*/
	public Card getCard (int x)
	{
		return ((x < 0 || x > 3) ? null : cards[x]);
	}

	/** Compares this object with another. It's almost like an equals method.
	*
	* @param thc The OmahaHand to be compared.
	*
	* @return true if this OmahaHand has exactly the same cards as the one provided through the parameter,
	* false otherwise.
	*/
	public boolean compare (OmahaHand thc)
	{
		for (int i = 0; i < 4; ++i)
		{
			if (! cards[i].equals (thc.getCard (i)))
			{
				return false;
			}
		}
		
		return true;
	}

	/** Compares this OmahaHand with another.
	*
	* @param oh The object to be compared with.
	*
	* @return true if this object equals "oh", false otherwise. Two OmahaHands are equal
	* iff they contain the same cards and have the same number of occurrences and wins.
	*/
	@Override public boolean equals (Object oh)
	{
		//this omaha hand can't be equal to null
		if (oh == null)
		{
			return false;
		}

		//check to see if the parameter is of the right class
		if (! getClass ().equals (oh.getClass ()))
		{
			return false;
		}

		//now it's safe to cast
		OmahaHand param = (OmahaHand) oh;

		//check nr of occurrences and wins
		if (super.getOccurrence () != param.getOccurrence () || super.getWins () != param.getWins ())
		{
			return false;
		}

		//and finally check if the cards match
		return compare (param);
	}

	/** Returns a hash code for this object.
	*
	* @return a hash code for this object. Value will be between 16 and 567 inclusively.
	*/
	@Override public int hashCode ()
	{
		int rez = cards[0].hashCode () + cards[1].hashCode () + cards[2].hashCode () + cards[3].hashCode ();

		rez += (super.getOccurrence () + super.getWins ()) % 500;

		return rez;
	}

	/** Returns a string representation of this OmahaHand.
	*
	* @return a string representation of this OmahHand. Length will be exactly 8 characters (2 for each card).
	*/
	@Override public String toString ()
	{
		return cards[0].toString () + cards[1].toString () + cards[2].toString () + cards[3].toString ();
	}
}