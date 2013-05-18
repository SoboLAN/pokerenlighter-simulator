package org.javafling.pokerenlighter.combination;

/**
 * Represents the card-combination in Texas Hold'em (2 cards from player + 5 community cards).
 * <br /><br />
 * This class is not thread-safe.
 * 
 * @autho Radu Murzea
 * 
 * @version 1.2
 */
public final class TexasCombination
{
    // The cards representing the combination
	private Card[] cards;
	
	//result of "getCombination" is cached here
	private String result;

    /**
	 * Constructs a TexasCombination object.
	 * <br />
	 * <strong>WARNING:</strong> The constructor accepts duplicate cards. To avoid unexpected
	 * behaviour, don't provide such input.
	 *
	 * @param c the cards. This array must contain 7 Card objects.
	 *
	 * @throws IllegalArgumentException if the size of the argument is different than 7.
	 * 
	 * @throws NullPointerException if the parameter is null or if it contains null values.
	 */
	public TexasCombination (Card[] c)
	{
		if (c == null)
		{
			throw new NullPointerException ();
		}
		
		if (c.length != 7)
		{
			throw new IllegalArgumentException ("size of array must be 7");
		}
		
		for (int i = 0; i < 7; i++)
		{
			if (c[i] == null)
			{
				throw new NullPointerException ();
			}
		}
		
		//call made like this to avoid duplicate code
		setCards (c);
    }
	
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
	 * Sets new Cards for this TexasCombination object.
	 * <br />
	 * <strong>WARNING</strong>: Just like the constructor, this method accepts duplicate cards.
	 *
	 * @param c the cards. This array must contain 7 Card objects.
	 *
	 * @throws IllegalArgumentException if the size of the argument is different than 7.
	 * 
	 * @throws NullPointerException if the parameter is null or if it contains null values.
	 */
	public void setCards (Card[] c)
	{
		if (c == null)
		{
			throw new NullPointerException ();
		}
		
		if (c.length != 7)
		{
			throw new IllegalArgumentException ("size of array must be 7");
		}
		
		for (int i = 0; i < 7; i++)
		{
			if (c[i] == null)
			{
				throw new NullPointerException ();
			}
		}
		
		//copy argument to field
		cards = c;

		//sort the cards in descending order by rank
		//sorting network is used since it is extremely fast for such small arrays
		//16 comparisons. insertion sort would've required 21
		swapCards (1, 2);
		swapCards (0, 2);
		swapCards (0, 1);
		swapCards (3, 4);
		swapCards (5, 6);
		swapCards (3, 5);
		swapCards (4, 6);
		swapCards (4, 5);
		swapCards (0, 4);
		swapCards (0, 3);
		swapCards (1, 5);
		swapCards (2, 6);
		swapCards (2, 5);
		swapCards (1, 3);
		swapCards (2, 4);
		swapCards (2, 3);
		
		//new input means old cache is invalid, so purge it
		result = null;
	}
	
	/**
	 * Returns the card with ID index.
	 *
	 * @param index The ID of the the card. Accepted values are between 0 and 6 inclusively.
	 *
	 * @return The card with the ID index. If the parameter index is out of bounds, null is returned.
	 */
	public Card getCard (int index)
	{
		return (index < 0 || index >= 7) ? null : cards[index];
	}

	/**
	 * Returns the highest 5-card combination of the 7 cards.
	 *
	 * @return The highest combination. The first character of the string is the rank of the combination,
	 * followed by the cards that compose it.
	 * <br />
	 * Basically, these are the possibilities:
	 * 
	 * <ul>
	 * <li>High Card: "0" + the ranks of the 5 cards, in descending order</li>
	 * <li>One Pair: "1" + rank of the pair + big kicker + small kicker</li>
	 * <li>Two Pair: "2" + rank of the biggest pair + rank of the second pair + kicker</li>
	 * <li>Three of a Kind: "3" + rank of the set + big kicker + small kicker</li>
	 * <li>Straight: "4" + rank of the highest card that makes up the straight</li>
	 * <li>Flush: "5" + the ranks of the highest 5 cards that make up the flush, in descending order</li>
	 * <li>Full House: "6" + rank of the set + rank of the pair</li>
	 * <li>Quad: "7" + rank of the quad + kicker</li>
	 * <li>Straight Flush: "8" + rank of the highest card that makes up the straight flush</li>
	 * <li>Royal Flush: "9"</li>
	 * </ul>
	 * 
	 * The order of the elements within the result is ALWAYS kept.
	 */
	public String getCombination ()
	{
		//check cache
		if (result != null)
		{
			return result;
		}
		
		String tmprank;

		//check for royal flush
		if (isRoyalFlush ())
		{
			result = "9";
			return result;
		}

		//check for straight flush
		tmprank = getStraightFlush ();
		
		if (! tmprank.equals ("0"))
		{
			result = "8" + tmprank;
			return result;
		}

		//check for quad
		tmprank = getQuad ();
		
		if (! tmprank.equals ("0"))
		{
			result = "7" + tmprank;
			return result;
		}

		//check for full house
		tmprank = getFullHouse ();
		
		if (! tmprank.equals ("0"))
		{
			result = "6" + tmprank;
			return result;
		}

		//check for flush
		tmprank = getFlush ();
		
		if (! tmprank.equals ("0"))
		{
			result = "5" + tmprank;
			return result;
		}

		//check for straight
		tmprank = getStraight ();
		
		if (! tmprank.equals ("0"))
		{
			result = "4" + tmprank;
			return result;
		}

		//check for sets
		tmprank = getThreeOfAKind ();
		
		if (! tmprank.equals ("0"))
		{
			result = "3" + tmprank;
			return result;
		}

		//check for 2 pair
		tmprank = getTwoPair ();
		
		if (! tmprank.equals ("0"))
		{
			result = "2" + tmprank;
			return result;
		}

		//check for 1 pair
		tmprank = getOnePair ();
		
		if (! tmprank.equals ("0"))
		{
			result = "1" + tmprank;
			return result;
		}

		//if none of the above is found, high card is the only one left
		result = "0" + getHighCard ();
		
		return result;
	}

	/**
	 * Determines the high card combination of this combination.
	 *
	 * @return String of the form 'abcde' (a = highest card, b = 2nd highest,
	 * c = 3rd highest, d = 4th highest, e = 5th highest).
	 */
	public String getHighCard ()
	{
		String stt = new String ();
		int i;

		//since the cards are already in descending order (sorted by the constructor),
		//just adding them to the result is ok
		for (i = 0; i < 5; ++i)
		{
			stt = stt + cards[i].getCharCard ();
		}

		return stt;
	}
    
	/**
	 * Determines if this combination contains a pair or not.
	 *
	 * @return String of the form 'abcd' (a = highest pair, b = kicker1,
	 * c = kicker2, d = kicker3), '0' if the player doesn't have any pairs.
	 */
	public String getOnePair ()
	{
		String stt = "0";

		//search for 1 pair.
		//if found, return the pair and the highest kickers

		if (cards[0].getRank () == cards[1].getRank ())
		{
			stt = Character.toString (cards[0].getCharCard ());
			stt = stt + cards[2].getCharCard ();
			stt = stt + cards[3].getCharCard ();
			stt = stt + cards[4].getCharCard ();

			return stt;
		}
		if (cards[1].getCharCard () == cards[2].getCharCard ())
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
		if (cards[4].getRank () == cards[5].getRank ())
		{
			stt = Character.toString (cards[4].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();
			stt = stt + cards[2].getCharCard ();

			return stt;
		}
		if (cards[5].getRank () == cards[6].getRank ())
		{
			stt = Character.toString (cards[5].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();
			stt = stt + cards[2].getCharCard ();

			return stt;
		}

		return stt;
	}

	/**
	 * Determines if this combination contains 2 pair or not.
	 *
	 * @return String of the form 'xyz' (x = highest pair, y = second highest pair, z = kicker),
	 * '0' if the combination doesn't have 2 pairs.
	 */
	public String getTwoPair ()
	{
		String stt = "0";

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
			if (cards[4].getRank () == cards[5].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[4].getCharCard ();
				stt = stt + cards[2].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[5].getCharCard ();
				stt = stt + cards[2].getCharCard ();

				return stt;
			}

			return stt;
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
			if (cards[4].getRank () == cards[5].getRank ())
			{
				stt = Character.toString (cards[1].getCharCard ());
				stt = stt + cards[4].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[1].getCharCard ());
				stt = stt + cards[5].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}

			return stt;
		}
		if (cards[2].getRank () == cards[3].getRank ())
		{
			if (cards[4].getRank () == cards[5].getRank ())
			{
				stt = Character.toString (cards[2].getCharCard ());
				stt = stt + cards[4].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[2].getCharCard ());
				stt = stt + cards[5].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
		}
		if (cards[3].getRank () == cards[4].getRank ())
		{
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[3].getCharCard ());
				stt = stt + cards[5].getCharCard ();
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
		}

		return stt;
	}
    
	/**
	 * Determines if this combination contains 3 of a kind.
	 *
	 * @return String of the form 'xyz' (x = triple card, y = kicker1, z = kicker2),
	 * '0' if the combination has no three of a kind.
	 */
	public String getThreeOfAKind ()
	{
		String stt = "0";

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
		if (cards[3].getRank () == cards[4].getRank () && cards[4].getRank () == cards[5].getRank ())
		{
			stt = Character.toString (cards[3].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();

			return stt;
		}
		if (cards[4].getRank () == cards[5].getRank () && cards[5].getRank () == cards[6].getRank ())
		{
			stt = Character.toString (cards[4].getCharCard ());
			stt = stt + cards[0].getCharCard ();
			stt = stt + cards[1].getCharCard ();

			return stt;
		}

		return stt;
	}

	/**
	 * Determines if this combination contains a straight.
	 *
	 * @return String with highest card of the straight or '0' if the combination has no straight.
	 */
	public String getStraight ()
	{
		String stt = "0";
		int[] ids = new int[7];
		int i, j, k;

		//I will use the ranks, it's easier
		for (i = 0; i < 7; ++i)
		{
			ids[i] = cards[i].getRank ();
		}

		// eliminate duplicates (the big if statements at the end of the method will not work correctly if there are duplicate cards)
		for (i = 0; i < 6; ++i)
		{
			ids[i] = (ids[i] == ids[i + 1]) ? 0 : ids[i];
		}

		// resort cards (duplicates (which are now = 0) will be moved in the back)
		for (i = 0; i < 6; ++i)
		{
			for (j = i + 1; j < 7; ++j)
			{
				if (ids[i] < ids[j])
				{
					k = ids[i];
					ids[i] = ids[j];
					ids[j] = k;
				}
			}
		}

		//now let's search for straights

		if (ids[0] == ids[1] + 1 && ids[1] == ids[2] + 1 && ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1)
		{
			stt = Character.toString (Card.getCharCard (ids[0]));
			return stt;
		}

		if (ids[1] == ids[2] + 1 && ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1 && ids[4] == ids[5] + 1)
		{
			stt = Character.toString (Card.getCharCard (ids[1]));
			return stt;
		}

		if (ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1 && ids[4] == ids[5] + 1 && ids[5] == ids[6]+ 1)
		{
			stt = Character.toString (Card.getCharCard (ids[2]));
			return stt;
		}

		// There is one aditional situation. That is the wheel
		if (ids[0] == 14)
		{
			if (ids[1] == 5 && ids[2] == 4 && ids[3] == 3 && ids[4] == 2)
			{
				stt = "5";
				return stt;
			}
			if (ids[2] == 5 && ids[3] == 4 && ids[4] == 3 && ids[5] == 2)
			{
				stt = "5";
				return stt;
			}
			if (ids[3] == 5 && ids[4] == 4 && ids[5] == 3 && ids[6] == 2)
			{
				stt = "5";
				return stt;
			}
		}

		return stt;
	}

	/**
	 * Determines if this combination contains a flush.
	 *
	 * @return String that contains the 5 cards that form the flush
	 * (in descendent order), '0' if the combination has no flush.
	 */
	public String getFlush ()
	{
		Card[] ids = new Card[7];
		Card z;
		int i, j;
		String stt = "0";

		//I will need the color too for this one, so let's use the whole cards.
		for (i = 0; i < 7; ++i)
		{
			ids[i] = cards[i];
		}

		// Group all cards by their color (which color is not relevant since no color
		//is more important than the other in Texas Hold'em)
		for (i = 0; i < 6; ++i)
		{
			for (j = i + 1; j < 7; ++j)
			{
				if (ids[i].getColor () > ids[j].getColor ())
				{
					z = ids[i];
					ids[i] = ids[j];
					ids[j] = z;
				}
			}
		}

		// Rudimentary / basic check of color condition. If it fails, it is surely no flush.
		if (ids[2].getColor () != ids[3].getColor () || ids[3].getColor () != ids[4].getColor ())
		{
			return stt;
		}

		int[] tmp = new int[7];
		int k, tmp_dim;
		char domin_color = ids[3].getColor ();

		// Determine number of cards of dominant color
		tmp_dim = 0;
		for (i = 0; i < 7; ++i)
		{
			if (ids[i].getColor () == domin_color)
			{
				tmp[tmp_dim++] = ids[i].getRank ();
			}
		}

		// If there are less than 5 cards of the same color, there is no flush
		if (tmp_dim < 5)
		{
			return stt;
		}

		// Sort cards that have the same color in ascending order */
		for (i = 0; i < tmp_dim - 1; ++i)
		{
			for (j = i + 1; j < tmp_dim; ++j)
			{
				if (tmp[i] < tmp[j])
				{
					k = tmp[i];
					tmp[i] = tmp[j];
					tmp[j] = k;
				}
			}
		}

		// Compose Result
		stt = Character.toString (Card.getCharCard (tmp[0]));
		stt = stt + Card.getCharCard (tmp[1]);
		stt = stt + Card.getCharCard (tmp[2]);
		stt = stt + Card.getCharCard (tmp[3]);
		stt = stt + Card.getCharCard (tmp[4]);

		return stt;
	}

	/**
	 * Determines if this combination contains a full house.
	 *
	 * @return String of the form 'xy' (x = the triple card and y = the pair),
	 * '0' if the combination has no full house.
	 */
	public String getFullHouse ()
	{
		String stt = "0";

		if (cards[0].getRank () == cards[1].getRank () && cards[1].getRank () == cards[2].getRank ())
		{
			if (cards[3].getRank () == cards[4].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[3].getCharCard ();

				return stt;
			}
			if (cards[4].getRank () == cards[5].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[4].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[0].getCharCard ());
				stt = stt + cards[5].getCharCard ();

				return stt;
			}
		}
		if (cards[1].getRank () == cards[2].getRank () && cards[2].getRank () == cards[3].getRank ())
		{
			if (cards[4].getRank () == cards[5].getRank ())
			{
				stt = Character.toString (cards[1].getCharCard ());
				stt = stt + cards[4].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[1].getCharCard ());
				stt = stt + cards[5].getCharCard ();

				return stt;
			}
		}
		if (cards[2].getRank () == cards[3].getRank () && cards[3].getRank () == cards[4].getRank ())
		{
			if (cards[0].getRank () == cards[1].getRank ())
			{
				stt = Character.toString (cards[2].getCharCard ());
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
			if (cards[5].getRank () == cards[6].getRank ())
			{
				stt = Character.toString (cards[2].getCharCard ());
				stt = stt + cards[5].getCharCard ();

				return stt;
			}
		}
		if (cards[3].getRank () == cards[4].getRank () && cards[4].getRank () == cards[5].getRank ())
		{
			if (cards[0].getRank () == cards[1].getRank ())
			{
				stt = Character.toString (cards[3].getCharCard ());
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
            if (cards[1].getRank () == cards[2].getRank ())
			{
				stt = Character.toString (cards[3].getCharCard ());
				stt = stt + cards[1].getCharCard ();

				return stt;
			}
		}
		if (cards[4].getRank () == cards[5].getRank () && cards[5].getRank () == cards[6].getRank ())
		{
			if (cards[0].getRank () == cards[1].getRank ())
			{
				stt = Character.toString (cards[4].getCharCard ());
				stt = stt + cards[0].getCharCard ();

				return stt;
			}
			if (cards[1].getRank () == cards[2].getRank ())
			{
				stt = Character.toString (cards[4].getCharCard ());
				stt = stt + cards[1].getCharCard ();

				return stt;
			}
			if (cards[2].getRank () == cards[3].getRank ())
			{
				stt = Character.toString (cards[4].getCharCard ());
				stt = stt + cards[2].getCharCard ();

				return stt;
			}
		}

		return stt;
	}
    
    /**
	 * Determines if this combination contains a quad (4 of a kind).
	 *
	 * @return String of the form 'xy' (x = card that forms the quad, y = kicker),
	 * '0' if the combination doesn't have a quad.
	 */
    public String getQuad ()
    {
        String stt = "0";

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

			return stt;
		}
		if (cards[2].getRank () == cards[3].getRank () && cards[3].getRank () == cards[4].getRank () && cards[4].getRank () == cards[5].getRank ())
		{
			stt = Character.toString (cards[2].getCharCard ());
			stt = stt + cards[0].getCharCard ();

			return stt;
		}
		if (cards[3].getRank () == cards[4].getRank () && cards[4].getRank () == cards[5].getRank () && cards[5].getRank () == cards[6].getRank ())
		{
			stt = Character.toString (cards[3].getCharCard ());
			stt = stt + cards[0].getCharCard ();

			return stt;
		}

		return stt;
	}
    
	/**
	 * Determines if this combination contains a straight flush.
	 *
	 * @return The highest card of the straight flush in the form of a string,
	 * '0' if the combination has no straight flush.
	 */
	public String getStraightFlush ()
	{
		String stt = "0";
		int[] tmp = new int[7];
		int i, j, k, tmp_dim;
		Card[] ids = new Card[7];
		Card aux;
		char domin_color;

		//both ranks and colors are needed... soooo basically the whole cards
		for (i = 0; i < 7; ++i)
		{
			ids[i] = cards[i];
		}

		// Group cards by color
		for (i = 0; i < 6; ++i)
		{
			for (j = i + 1; j < 7; ++j)
			{
				if (ids[i].getColor () > ids[j].getColor ())
				{
					aux = ids[i];
					ids[i] = ids[j];
					ids[j] = aux;
				}
			}
		}

		// Create array of cards that have dominant color
		domin_color = ids[3].getColor ();
		tmp_dim = 0;
		for (i = 0; i < 6; ++i)
		{
			if (ids[i].getColor () == domin_color)
			{
				tmp[tmp_dim] = ids[i].getRank ();
				++tmp_dim;
			}
		}

		// If there are less than 5 cards of the same color, there is no straight flush
		if (tmp_dim < 5)
		{
			return stt;
		}

		// Sort the cards in descending order
		for (i = 0; i < tmp_dim - 1; ++i)
		{
			for (j = i + 1; j < tmp_dim; ++j)
			{
				if (tmp[i] < tmp[j])
				{
					k = tmp[i];
					tmp[i] = tmp[j];
					tmp[j] = k;
				}
			}
		}

		//search for straights among the cards with dominant color

		if (tmp[0] - 1 == tmp[1] && tmp[1] - 1 == tmp[2] && tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4])
		{
			stt = Character.toString (Card.getCharCard (tmp[0]));
			return stt;
		}
		if (tmp_dim > 5)
		{
			if (tmp[1] - 1 == tmp[2] && tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4] && tmp[4] - 1 == tmp[5])
			{
				stt = Character.toString (Card.getCharCard (tmp[1]));
				return stt;
			}
		}
		if (tmp_dim > 6)
		{
			if (tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4] && tmp[4] - 1 == tmp[5] && tmp[5] - 1 == tmp[6])
			{
				stt = Character.toString (Card.getCharCard (tmp[2]));
				return stt;
			}
		}

		// A special case is the straight flush composed of A, 2, 3, 4, 5.
		if (tmp[0] == 14 && tmp[tmp_dim - 4] == 5 && tmp[tmp_dim - 3] == 4 && tmp[tmp_dim - 2] == 3 && tmp[tmp_dim - 1] == 2)
		{
			stt = "5";
		}

		return stt;
	}

	/**
	 * Determines if the combination contains a royal flush.
	 *
	 * @return true if this combination contains a royal flush, false otherwise.
	 */
	public boolean isRoyalFlush ()
	{
		return (getStraightFlush ().equals ("A"));
	}

	/**
	 * Compares this object with another <code>TexasCombination</code> object.
	 *
	 * @param c The object to be compared.
	 *
	 * @return true if the parameter c equals this object, false otherwise.
	 */
	@Override
	public boolean equals (Object c)
	{
		if (c == null)
		{
			return false;
		}
		
		if (c == this)
		{
			return true;
		}

		if (! getClass ().equals (c.getClass ()))
		{
			return false;
		}

		TexasCombination param = (TexasCombination) c;

		for (int i = 0; i < 7; ++i)
		{
			if (! cards[i].equals (param.getCard (i)))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns a hash code for this TexasCombination.
	 *
	 * @return a hash code for this TexasCombination.
	 */
	@Override
	public int hashCode ()
	{
		int rez = 0;

		for (int i = 0; i < 7; ++i)
		{
			rez += cards[i].hashCode ();
		}

		return rez;
	}

	/**
	 * Returns a string representation of this TexasCombination.
	 *
	 * @return a string representation of this TexasCombination. Length will be exactly 14 characters, the cards
	 * are in descending order of their rank.
	 */
	@Override
	public String toString ()
	{
		StringBuilder rez = new StringBuilder ();

		for (int i = 0; i < 7; ++i)
		{
			rez.append (cards[i].toString ());
		}

		return rez.toString ();
	}
}