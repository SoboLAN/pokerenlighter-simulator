package org.javafling.pokerenlighter.combination;

/**
 * The player's cards have to be combined with the cards on the table (flop, turn, river) to determine
 * what the player's hand is.
 * This class describes all the possible outcomes of the combining process.
 * One very important advice: start calling the methods from the top (Royal Flush, then Straight Flush etc.)
 * and stop when you hit something that is valid (i.e. String different from "0"). That is because,
 * for example, when you have three of a kind, the method isOnePair will return a valid String,
 * despite the fact that you might want something else.
 * <br />
 * NOTE: The results are returned as Strings. The card representation in these Strings is of the form 'A', 'K',
 * 'J', 'T', '5' etc.
 * <br /><br />
 * This class is not thread-safe.
 * 
 * @author Radu Murzea
 * 
 * @version 1.2
 */
public final class Combination
{
    // The cards representing the combination
	private Card[] cards;

	//cache containers for the isStraight and isFlush methods
	private String flush, straight;

    /**
	 * Creates the combination.
	 * <br />
	 * <strong>WARNING:</strong> The constructor accepts duplicate cards. To avoid unexpected
	 * behaviour, don't provide such input.
	 *
	 * @param a First card.
	 *
	 * @param b Second card.
	 *
	 * @param c Third card.
	 *
	 * @param d Fourth card.
	 *
	 * @param e Fifth card.
	 * 
	 * @throws NullPointerException if one of the cards is null.
	 */
	public Combination (Card a, Card b, Card c, Card d, Card e)
	{
		if (a == null || b == null || c == null || d == null || e == null)
		{
			throw new NullPointerException ("card cannot be null");
		}
		
		cards = new Card[5];

		//this is to avoid duplicate code
		setCards (a, b, c, d, e);
    }
	
	//if the card at index "x" has a lower rank than that at index "y", it swaps them
	private void swapCards (int x, int y)
	{
		if (cards[x].getRank () < cards[y].getRank ())
		{
			Card k = cards[x];
			cards[x] = cards[y];
			cards[y] = k;
		}
	}
	
	/**
	 * Sets new cards for this Combination.
	 * <br />
	 * <strong>WARNING</strong>: Just like the constructor, this method accepts duplicate cards.
	 * 
	 * @param a First card.
	 *
	 * @param b Second card.
	 *
	 * @param c Third card.
	 *
	 * @param d Fourth card.
	 *
	 * @param e Fifth card.
	 * 
	 * @throws NullPointerException if one of the cards is null.
	 */
	public void setCards (Card a, Card b, Card c, Card d, Card e)
	{
		if (a == null || b == null || c == null || d == null || e == null)
		{
			throw new NullPointerException ("card cannot be null");
		}
		
		//store parameters in the cards field
		cards[0] = a;
		cards[1] = b;
		cards[2] = c;
		cards[3] = d;
		cards[4] = e;
		
		//sort the cards in descending order by rank
		//sorting network is used since it is extremely fast for such small arrays
		//it is actually the fastest sorting method known to man
		//9 comparisons. insertion sort would've required 10
		swapCards (0, 1);
		swapCards (3, 4);
		swapCards (2, 4);
		swapCards (2, 3);
		swapCards (0, 3);
		swapCards (0, 2);
		swapCards (1, 4);
		swapCards (1, 3);
		swapCards (1, 2);
		
		//new input means old cache is invalid, so purge it
		flush = straight = null;
	}

	/**
	 * Returns the card with ID x.
	 *
	 * @param x The ID of the the card. Accepted values are between 0 and 4 inclusively.
	 *
	 * @return The card with the ID x. If the parameter x is out of bounds, null is returned.
	 */
	public Card getCard (int x)
	{
		return (x < 0 || x > 4) ? null : cards[x];
	}

	/**
	 * Returns the 5 cards of this combination.
	 *
	 * @return The 5 cards of the combination (in descending order of their rank).
	 */
	public String getHighCard ()
	{
		StringBuilder stt = new StringBuilder ();
		int i;

		//just add the cards to the result
		for (i = 0; i < 5; ++i)
		{
			stt.append (cards[i].getCharCard ());
		}

		return stt.toString ();
	}
    
	/**
	 * Determines if this <code>Combination</code> contains a pair or not.
	 *
	 * @return String of the form 'abcd' (a = highest pair, b = kicker1, c = kicker2, d = kicker3),
	 * '0' if the <code>Combination</code> doesn't have any pairs.
	 */
	public String getOnePair ()
	{
		String stt = "0";

		//since the cards are in descending order, we start from the
		//beginning (in order to return the highest pair, if there is none).
		//every neighbouring cards are checked for equal ranks.
		//if a match is found, result is composed and returned.

		if (cards[0].getRank () == cards[1].getRank ())
		{
			stt = Character.toString (cards[0].getCharCard ());
			stt = stt + cards[2].getCharCard ();
			stt = stt + cards[3].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[1].getRank () == cards[2].getRank ())
		{
			stt = Character.toString (cards[1].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[3].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[2].getRank () == cards[3].getRank ())
		{
			stt = Character.toString (cards[2].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[3].getRank () == cards[4].getRank ())
		{
			stt = Character.toString (cards[3].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();
			stt = stt + cards[2].getCharCard ();

			return stt;
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains 2 pair or not.
	 *
	 * @return String of the form 'xyz' (x = highest pair, y = second highest pair, z = kicker),
	 * '0' if the <code>Combination</code> doesn't have 2 pair.
	 */
	public String getTwoPair ()
	{
		String stt = "0";

		//same implementation idea as in isOnePair method.
		//this time, when a pair is found, a second pair must be found too.

		if (cards[0].getRank () == cards[1].getRank ())
		{
			if (cards[2].getRank () == cards[3].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[2].getCharCard ();
				stt = stt + cards[4].getCharCard ();

				return stt;
			}
			if (cards[3].getRank () == cards[4].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[3].getCharCard ();
				stt = stt + cards[2].getCharCard ();

				return stt;
			}
		}
		if (cards[1].getRank () == cards[2].getRank ())
		{
			if (cards[3].getRank () == cards[4].getRank ())
			{
				stt = Character.toString (cards[1].getCharCard ());
				stt = stt + cards[3].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains 3 of a kind.
	 *
	 * @return String of the form 'xyz' (x = triple card, y = kicker1, z = kicker2),
	 * '0' if this <code>Combination</code> has no three of a kind.
	 */
	public String getThreeOfAKind ()
	{
		String stt = "0";

		//search for groups of 3 neighbouring cards that have the same rank

		if (cards[0].getRank () == cards[1].getRank () && cards[1].getRank () == cards[2].getRank ())
		{
			stt = Character.toString (cards[0].getCharCard ());
			stt = stt + cards[3].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[1].getRank () == cards[2].getRank () && cards[2].getRank () == cards[3].getRank ())
		{
			stt = Character.toString (cards[1].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[2].getRank () == cards[3].getRank () && cards[3].getRank () == cards[4].getRank ())
		{
			stt = Character.toString (cards[2].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();

			return stt;
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains a straight.
	 *
	 * @return String with highest card of the straight or '0' if this <code>Combination</code> has no straight.
	 */
	public String getStraight ()
	{
		//if the straight has already been called, don't compute again.
		if (straight != null)
		{
			return straight;
		}

		String stt = "0";

		//check for straight
		if (cards[0].getRank () == cards[1].getRank () + 1 &&
			cards[1].getRank () == cards[2].getRank () + 1 &&
			cards[2].getRank () == cards[3].getRank () + 1 &&
			cards[3].getRank () == cards[4].getRank () + 1)
		{
			stt = Character.toString (cards[0].getCharCard ());

			//fill the straight field
			straight = stt;

			return stt;
		}

		//the wheel is also a possibility
		if (cards[0].getRank () == 14 &&
			cards[1].getRank () == 5 &&
			cards[2].getRank () == 4 &&
			cards[3].getRank () == 3 &&
			cards[4].getRank () == 2)
		{
			stt = "5";

			//fill the straight field
			straight = stt;
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains a flush.
	 *
	 * @return String that contains the 5 cards that form the flush (in descendent order),
	 * '0' if this <code>Combination</code> has no flush.
	 */
	public String getFlush ()
	{
		//if it was already computed, return result
		if (flush != null)
		{
			return flush;
		}

		String stt = "0";

		//check for flush
		if (cards[0].getColor () == cards[1].getColor () &&
			cards[1].getColor () == cards[2].getColor () &&
			cards[2].getColor () == cards[3].getColor () &&
			cards[3].getColor () == cards[4].getColor ())
		{
			stt = Character.toString (cards[0].getCharCard ());
			stt = stt + cards[1].getCharCard ();
			stt = stt + cards[2].getCharCard ();
			stt = stt + cards[3].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			//fill the flush field
			flush = stt;
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains a full house.
	 *
	 * @return String of the form 'xy' (x = the triple card and y = the pair),
	 * '0' if this <code>Combination</code> has no full house.
	 */
	public String getFullHouse ()
	{
		String stt = "0";

		//search for a set (three of a kind) and a pair. if found, we probably have a winner :D

		if (cards[0].getRank () == cards[1].getRank () && cards[1].getRank () == cards[2].getRank ())
		{
			if (cards[3].getRank () == cards[4].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[3].getCharCard ();

				return stt;
			}
		}

		if (cards[2].getRank () == cards[3].getRank () && cards[3].getRank () == cards[4].getRank ())
		{
			if (cards[0].getRank () == cards[1].getRank ())
			{
				stt = Character.toString (cards[2].getCharCard ());
				stt = stt + cards[0].getCharCard ();
			}
		}

		return stt;
	}

    /**
	 * Determines if this <code>Combination</code> contains a quad (4 of a kind).
	 *
	 * @return String of the form 'xy' (x = card that forms the quad, y = kicker),
	 * '0' if this <code>Combination</code> doesn't have a quad.
	 */
    public String getQuad ()
    {
        String stt = "0";

		//search for 4 equal cards...

		if (cards[0].getRank () == cards[1].getRank () && cards[1].getRank () == cards[2].getRank () && cards[2].getRank () == cards[3].getRank ())
		{
			stt = Character.toString (cards[0].getCharCard ());
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[1].getRank () == cards[2].getRank () && cards[2].getRank () == cards[3].getRank () && cards[3].getRank () == cards[4].getRank ())
		{
			stt = Character.toString (cards[1].getCharCard ());
			stt = stt + cards[0].getCharCard ();
		}

		return stt;
	}

	/**
	 * Determines if this <code>Combination</code> contains a straight flush.
	 *
	 * @return The highest card of the straight flush in the form of a string,
	 * '0' if this <code>Combination</code> has no straight flush.
	 */
	public String getStraightFlush ()
	{
		String stt = "0";

		//if the straight and flush are not computed yet, do it know (subsequent calls will probably follow)
        this.straight = (this.straight == null) ? getStraight () : this.straight;
        this.flush = (this.flush == null) ? getFlush () : this.flush;

		//if the cards form a straight and a flush, then we have a straight flush
		if (! straight.equals ("0") && ! flush.equals ("0"))
		{
			stt = straight.equals("5") ? "5" : Character.toString (cards[0].getCharCard ());
		}

		return stt;
	}
	
	/**
	 * Determines if this <code>Combination</code> contains a royal flush.
	 *
	 * @return true if this <code>Combination</code> has a royal flush, false otherwise.
	 */
	public boolean getRoyalFlush ()
	{
		//the royal flush is a straight flush with highest card Ace. as simple as that
		return (getStraightFlush ().equals ("A"));
	}

	/**
	 * Checks this object for equality.
	 *
	 * @param c The <code>Combination</code> to be compared with.
	 *
	 * @return true if "c" equals this object, false otherwise.
	 */
	@Override public boolean equals (Object c)
	{
		if (c == null)
		{
			return false;
		}
		
		if (c == this)
		{
			return true;
		}

		//since Combination is a final class, the instanceof operator is safe here
		if (! getClass ().equals (c.getClass ()))
		{
			return false;
		}

		Combination param = (Combination) c;

		for (int i = 0; i < 5; ++i)
		{
			if (! param.getCard (i).equals (cards[i]))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the hash code value for this <code>Combination</code>.
	 *
	 * @return a hash code value for this <code>combination</code>. The value will be between 10 and 280 inclusively.
	 */
	@Override public int hashCode ()
	{
		int rez = 0;
		
		for (int i = 0; i < 5; ++i)
		{
			//add the ranks of the cards
			rez += cards[i].hashCode ();
		}

		return rez;
	}

	/**
	 * Return a String representation of this <code>Combination</code>.
	 *
	 * @return The string representation of this <code>Combination</code>. The cards will be in
	 * descending order of their rank.
	 */
	@Override public String toString ()
	{
		StringBuilder rez = new StringBuilder ();
		
		for (int i = 0; i < 5; i++)
		{
			rez.append (cards[i].toString ());
		}

		return rez.toString ();
	}
}