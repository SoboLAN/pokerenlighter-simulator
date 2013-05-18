package org.javafling.pokerenlighter.combination;

/**
 * One of the most important classes in the program.
 * It describes a (poker) card, by its 2 elements: the actual card and its color.
 * 
 * @version 1.1
 * 
 * @author Radu Murzea
 */
public class Card
{
	// Can be 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
	private int card;

	// Can be 'c' (Club), 'd' (Diamond), 'h' (Heart), 's' (Spade)
	private char color;

	/**
     * Creates a card of the specified value and color.
     *
     * @param  x   Card value. Can be only '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' or 'A'.
     * @param  y   Card color. Can be only 'c', 'd', 'h' or 's'.
     *
     * @throws  IllegalArgumentException  If x or y have unacceptable values.
     */
	public Card (char x, char y)
	{
		this.card = Card.getRank (x);
		
		// check validity of card		
		if (this.card == 0)
		{
			throw new IllegalArgumentException ("invalid card rank");
		}

		// check validity of color
		if (y != 'c' && y != 'd' && y != 'h' && y != 's')
		{
			throw new IllegalArgumentException ("invalid card color");
		}
		else
		{
			this.color = y;
		}
	}
	
	/**
     * Creates a card of the specified rank and color.
     *
     * @param  x   Card rank. Can be only 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 or 14.
     * @param  y   Card color. Can be only 'c', 'd', 'h' or 's'.
     *
     * @throws  IllegalArgumentException  If x or y have unacceptable values.
     */
	public Card (int x, char y)
	{
		// The rank must be a valid card rank (between 2 and 14 inclusively)
		if (x < 2 || x > 14)
		{
			throw new IllegalArgumentException ("invalid card rank");
		}
		else
		{
			this.card = x;
		}

		// check color validity
		if (y != 'c' && y != 'd' && y != 'h' && y != 's')
		{
			throw new IllegalArgumentException ("invalid card color");
		}
		else
		{
			this.color = y;
		}
	}

	/**
	 * Checks whether 2 cards are suited (they have the same color).
	 *
	 * @param a	Another card.
	 *
	 * @return	True if the current card is suited with the card a, false otherwise.
	 */
	public final boolean isSuited (Card a)
	{
		return (a.getColor () == this.color);
	}

	/**
	 * Returns the card rank (same one used to construct the card).
	 *
	 * @return	The card rank of the current object.
	 */
	public final int getRank ()
	{
		return this.card;
	}
	
	/**
	 * Returns the card color (same one used to construct the card).
	 *
	 * @return	The card color of the current object.
	 */
	public final char getColor ()
	{
		return this.color;
	}
	
	/**
	 * Returns the card as a char ('3', 7', 'A', 'K', etc.).
	 *
	 * @return	The card value.
	 */
	public final char getCharCard ()
	{
		return getCharCard (this.card);
	}

	/**
	 * Returns the card as an integer (3, 7, 14, 13, etc.).
	 *
	 * @param c	Character representation of a card.
	 *
	 * @return	The card value. If c doesn't represent a valid card, 0 is returned.
	 */
	public static int getRank (char c)
	{
		switch (c)
		{
			case '2': return 2;
			case '3': return 3;
			case '4': return 4;
			case '5': return 5;
			case '6': return 6;
			case '7': return 7;
			case '8': return 8;
			case '9': return 9;
			case 'T': return 10;
			case 'J': return 11;
			case 'Q': return 12;
			case 'K': return 13;
			case 'A': return 14;
			default: return 0;
		}
	}

	/**
	 * Returns the card as a char ('3', 7', 'A', 'K', etc.).
	 *
	 * @param x The card rank. Accepted values are between 2 and 14 inclusively.
	 *
	 * @return The char representation of the card. If x is not in the accepted range, '0' is returned.
	 */
	public static char getCharCard (int x)
	{
		switch (x)
		{
			case 2: return '2';
			case 3: return '3';
			case 4: return '4';
			case 5: return '5';
			case 6: return '6';
			case 7: return '7';
			case 8: return '8';
			case 9: return '9';
			case 10: return 'T';
			case 11: return 'J';
			case 12: return 'Q';
			case 13: return 'K';
			case 14: return 'A';
			default: return '0';
		}
	}

	/**
	 * Compares 2 cards. Two cards are equal if they have the same rank and the same color.
	 *
	 * @param c The card to compare.
	 * 
	 * @return true if this object equals c, false otherwise.
	 */
	@Override
	public boolean equals (Object c)
	{
		if (c == null)
		{
			return false;
		}
		else if (c == this)
		{
			return true;
		}

		//since the class (Card) is not final, c could be a subtype of Card.
		//if that is the case, then this method could return true, which is not acceptable.
		//so a test must be made to see what type each one really is.
		//the instanceof cannot be used here, since it would say that "c" is a instance of Card, even
		//if (maybe) it is not
		if (! getClass ().equals (c.getClass ()))
		{
			return false;
		}

		//now it's safe to cast the parameter to a Card
		Card param = (Card) c;

		return (param.getRank () == this.card && param.getColor () == this.color);
	}

	/**
	 * Returns the hash code value for this <code>Card</code>.
	 *
	 * @return a hash code value for this <code>Card</code>. The value will be between 2 and 56 inclusively.
	 */
	@Override
	public int hashCode ()
	{
		//add rank
		int rez = this.card;

		//add color
		switch (this.color)
		{
			case 'c': return rez;
			case 'd': return rez * 2;
			case 'h': return rez * 3;
			case 's': return rez * 4;
		}
		
		return rez;
	}

	/**
	 * Returns the string representation of this card.
	 *
	 * @return The string representation of this card.
	 */
	@Override
	public String toString ()
	{
		return Character.toString (getCharCard ()) + Character.toString (this.color);
	}
}