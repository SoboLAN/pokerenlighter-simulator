package org.javafling.pokerenlighter.combination;

/** Represents the combination of cards a player can have in Omaha.
 * That combination is made of the 4 cards the player is holding and the
 * 5 community cards.
 * <br /><br />
 * This class is not thread-safe.
 * 
 * @author Murzea Radu
 * 
 * @version 1.2
 */
public final class OmahaCombination
{
	//because of the limitations (and complexity) of combining 9 cards to a Omaha combination,
	//multiple combinations are used to represent it. They are stored in this field.
	private Combination[] combs;

	//storage for the cards that form this Omaha combination. this field is useful only to the
	//equals, hashCode and toString methods. the actual "omaha computation" is done using the
	//combs field.
	private Card[] cards;
	
	private String result_hi;
	
	private String result_lo;

	/** Constructs the combination.
	 *
	 * @param newCards The <code>Card</code>s that make this combination. The size must
	 * equal 9 (the first 4 <code>Card</code>s are what the player is holding, the next 5
	 * are the community <code>Card</code>s). This order is very important if you want correct
	 * results.
	 * <br />
	 * <strong>WARNING:</strong> The constructor accepts duplicate cards. To avoid unexpected
	 * behaviour, don't provide such input.
	 *
	 * @exception IllegalArgumentException if the size of the parameter is different than 9.
	 * 
	 * @exception NullPointerException if the parameter is null or if it contains null values.
	 */
	public OmahaCombination (Card[] newCards)
	{
		if (newCards == null)
		{
			throw new NullPointerException ();
		}
		
		//check length of argument
		if (newCards.length != 9)
		{
			throw new IllegalArgumentException ("invalid number of cards provided");
		}
		
		for (int i = 0; i < 9; i++)
		{
			if (newCards[i] == null)
			{
				throw new NullPointerException ();
			}
		}

		this.cards = newCards;

		combs = new Combination[10 * 6];

		//next, all 60 possible combinations are created. a more elegant iteration
		//over them is very possible, but that would reduce speed. And in Omaha simulations,
		//speed is something extremely precious. (the simulations usually take hours).

		//5, 6, 7 combination
		combs[0] = new Combination (newCards[0], newCards[1], newCards[4], newCards[5], newCards[6]);
		combs[1] = new Combination (newCards[0], newCards[2], newCards[4], newCards[5], newCards[6]);
		combs[2] = new Combination (newCards[0], newCards[3], newCards[4], newCards[5], newCards[6]);
		combs[3] = new Combination (newCards[1], newCards[2], newCards[4], newCards[5], newCards[6]);
		combs[4] = new Combination (newCards[1], newCards[3], newCards[4], newCards[5], newCards[6]);
		combs[5] = new Combination (newCards[2], newCards[3], newCards[4], newCards[5], newCards[6]);

		//5, 6, 8 combination
		combs[6] = new Combination (newCards[0], newCards[1], newCards[4], newCards[5], newCards[7]);
		combs[7] = new Combination (newCards[0], newCards[2], newCards[4], newCards[5], newCards[7]);
		combs[8] = new Combination (newCards[0], newCards[3], newCards[4], newCards[5], newCards[7]);
		combs[9] = new Combination (newCards[1], newCards[2], newCards[4], newCards[5], newCards[7]);
		combs[10] = new Combination (newCards[1], newCards[3], newCards[4], newCards[5], newCards[7]);
		combs[11] = new Combination (newCards[2], newCards[3], newCards[4], newCards[5], newCards[7]);

		//5, 7, 8 combination
		combs[12] = new Combination (newCards[0], newCards[1], newCards[4], newCards[6], newCards[7]);
		combs[13] = new Combination (newCards[0], newCards[2], newCards[4], newCards[6], newCards[7]);
		combs[14] = new Combination (newCards[0], newCards[3], newCards[4], newCards[6], newCards[7]);
		combs[15] = new Combination (newCards[1], newCards[2], newCards[4], newCards[6], newCards[7]);
		combs[16] = new Combination (newCards[1], newCards[3], newCards[4], newCards[6], newCards[7]);
		combs[17] = new Combination (newCards[2], newCards[3], newCards[4], newCards[6], newCards[7]);

		//5, 7, 9 combination
		combs[18] = new Combination (newCards[0], newCards[1], newCards[4], newCards[6], newCards[8]);
		combs[19] = new Combination (newCards[0], newCards[2], newCards[4], newCards[6], newCards[8]);
		combs[20] = new Combination (newCards[0], newCards[3], newCards[4], newCards[6], newCards[8]);
		combs[21] = new Combination (newCards[1], newCards[2], newCards[4], newCards[6], newCards[8]);
		combs[22] = new Combination (newCards[1], newCards[3], newCards[4], newCards[6], newCards[8]);
		combs[23] = new Combination (newCards[2], newCards[3], newCards[4], newCards[6], newCards[8]);

		//5, 6, 9 combination
		combs[24] = new Combination (newCards[0], newCards[1], newCards[4], newCards[5], newCards[8]);
		combs[25] = new Combination (newCards[0], newCards[2], newCards[4], newCards[5], newCards[8]);
		combs[26] = new Combination (newCards[0], newCards[3], newCards[4], newCards[5], newCards[8]);
		combs[27] = new Combination (newCards[1], newCards[2], newCards[4], newCards[5], newCards[8]);
		combs[28] = new Combination (newCards[1], newCards[3], newCards[4], newCards[5], newCards[8]);
		combs[29] = new Combination (newCards[2], newCards[3], newCards[4], newCards[5], newCards[8]);

		//5, 8, 9 combination
		combs[30] = new Combination (newCards[0], newCards[1], newCards[4], newCards[7], newCards[8]);
		combs[31] = new Combination (newCards[0], newCards[2], newCards[4], newCards[7], newCards[8]);
		combs[32] = new Combination (newCards[0], newCards[3], newCards[4], newCards[7], newCards[8]);
		combs[33] = new Combination (newCards[1], newCards[2], newCards[4], newCards[7], newCards[8]);
		combs[34] = new Combination (newCards[1], newCards[3], newCards[4], newCards[7], newCards[8]);
		combs[35] = new Combination (newCards[2], newCards[3], newCards[4], newCards[7], newCards[8]);

		//6, 7, 8 combination
		combs[36] = new Combination (newCards[0], newCards[1], newCards[5], newCards[6], newCards[7]);
		combs[37] = new Combination (newCards[0], newCards[2], newCards[5], newCards[6], newCards[7]);
		combs[38] = new Combination (newCards[0], newCards[3], newCards[5], newCards[6], newCards[7]);
		combs[39] = new Combination (newCards[1], newCards[2], newCards[5], newCards[6], newCards[7]);
		combs[40] = new Combination (newCards[1], newCards[3], newCards[5], newCards[6], newCards[7]);
		combs[41] = new Combination (newCards[2], newCards[3], newCards[5], newCards[6], newCards[7]);

		//6, 7, 9 combination
		combs[42] = new Combination (newCards[0], newCards[1], newCards[5], newCards[6], newCards[8]);
		combs[43] = new Combination (newCards[0], newCards[2], newCards[5], newCards[6], newCards[8]);
		combs[44] = new Combination (newCards[0], newCards[3], newCards[5], newCards[6], newCards[8]);
		combs[45] = new Combination (newCards[1], newCards[2], newCards[5], newCards[6], newCards[8]);
		combs[46] = new Combination (newCards[1], newCards[3], newCards[5], newCards[6], newCards[8]);
		combs[47] = new Combination (newCards[2], newCards[3], newCards[5], newCards[6], newCards[8]);

		//6, 8, 9 combination
		combs[48] = new Combination (newCards[0], newCards[1], newCards[5], newCards[7], newCards[8]);
		combs[49] = new Combination (newCards[0], newCards[2], newCards[5], newCards[7], newCards[8]);
		combs[50] = new Combination (newCards[0], newCards[3], newCards[5], newCards[7], newCards[8]);
		combs[51] = new Combination (newCards[1], newCards[2], newCards[5], newCards[7], newCards[8]);
		combs[52] = new Combination (newCards[1], newCards[3], newCards[5], newCards[7], newCards[8]);
		combs[53] = new Combination (newCards[2], newCards[3], newCards[5], newCards[7], newCards[8]);

		//7, 8, 9 combination
		combs[54] = new Combination (newCards[0], newCards[1], newCards[6], newCards[7], newCards[8]);
		combs[55] = new Combination (newCards[0], newCards[2], newCards[6], newCards[7], newCards[8]);
		combs[56] = new Combination (newCards[0], newCards[3], newCards[6], newCards[7], newCards[8]);
		combs[57] = new Combination (newCards[1], newCards[2], newCards[6], newCards[7], newCards[8]);
		combs[58] = new Combination (newCards[1], newCards[3], newCards[6], newCards[7], newCards[8]);
		combs[59] = new Combination (newCards[2], newCards[3], newCards[6], newCards[7], newCards[8]);
	}
	
	/** Sets new Cards for this OmahaCombination object.
	 * <br />
	 * <strong>WARNING</strong>: Just like the constructor, this method accepts duplicate cards.
	 *
	 * @param newCards the cards. This array must contain 9 Card objects.
	 *
	 * @exception IllegalArgumentException if the size of the argument is different than 9.
	 * 
	 * @exception NullPointerException if the parameter is null or if it contains null values.
	 */
	public void setCards (Card[] newCards)
	{
		if (newCards == null)
		{
			throw new NullPointerException ();
		}
		
		//check length of argument
		if (newCards.length != 9)
		{
			throw new IllegalArgumentException ("invalid number of cards provided");
		}
		
		for (int i = 0; i < 9; i++)
		{
			if (newCards[i] == null)
			{
				throw new NullPointerException ();
			}
		}

		this.cards = newCards;

		//next, all 60 possible combinations are created. a more elegant iteration
		//over them is very possible, but that would reduce speed. And in Omaha simulations,
		//speed is something extremely precious. (the simulations usually take hours).

		//5, 6, 7 combination
		combs[0].setCards (newCards[0], newCards[1], newCards[4], newCards[5], newCards[6]);
		combs[1].setCards (newCards[0], newCards[2], newCards[4], newCards[5], newCards[6]);
		combs[2].setCards (newCards[0], newCards[3], newCards[4], newCards[5], newCards[6]);
		combs[3].setCards (newCards[1], newCards[2], newCards[4], newCards[5], newCards[6]);
		combs[4].setCards (newCards[1], newCards[3], newCards[4], newCards[5], newCards[6]);
		combs[5].setCards (newCards[2], newCards[3], newCards[4], newCards[5], newCards[6]);

		//5, 6, 8 combination
		combs[6].setCards (newCards[0], newCards[1], newCards[4], newCards[5], newCards[7]);
		combs[7].setCards (newCards[0], newCards[2], newCards[4], newCards[5], newCards[7]);
		combs[8].setCards (newCards[0], newCards[3], newCards[4], newCards[5], newCards[7]);
		combs[9].setCards (newCards[1], newCards[2], newCards[4], newCards[5], newCards[7]);
		combs[10].setCards (newCards[1], newCards[3], newCards[4], newCards[5], newCards[7]);
		combs[11].setCards (newCards[2], newCards[3], newCards[4], newCards[5], newCards[7]);

		//5, 7, 8 combination
		combs[12].setCards (newCards[0], newCards[1], newCards[4], newCards[6], newCards[7]);
		combs[13].setCards (newCards[0], newCards[2], newCards[4], newCards[6], newCards[7]);
		combs[14].setCards (newCards[0], newCards[3], newCards[4], newCards[6], newCards[7]);
		combs[15].setCards (newCards[1], newCards[2], newCards[4], newCards[6], newCards[7]);
		combs[16].setCards (newCards[1], newCards[3], newCards[4], newCards[6], newCards[7]);
		combs[17].setCards (newCards[2], newCards[3], newCards[4], newCards[6], newCards[7]);

		//5, 7, 9 combination
		combs[18].setCards (newCards[0], newCards[1], newCards[4], newCards[6], newCards[8]);
		combs[19].setCards (newCards[0], newCards[2], newCards[4], newCards[6], newCards[8]);
		combs[20].setCards (newCards[0], newCards[3], newCards[4], newCards[6], newCards[8]);
		combs[21].setCards (newCards[1], newCards[2], newCards[4], newCards[6], newCards[8]);
		combs[22].setCards (newCards[1], newCards[3], newCards[4], newCards[6], newCards[8]);
		combs[23].setCards (newCards[2], newCards[3], newCards[4], newCards[6], newCards[8]);

		//5, 6, 9 combination
		combs[24].setCards (newCards[0], newCards[1], newCards[4], newCards[5], newCards[8]);
		combs[25].setCards (newCards[0], newCards[2], newCards[4], newCards[5], newCards[8]);
		combs[26].setCards (newCards[0], newCards[3], newCards[4], newCards[5], newCards[8]);
		combs[27].setCards (newCards[1], newCards[2], newCards[4], newCards[5], newCards[8]);
		combs[28].setCards (newCards[1], newCards[3], newCards[4], newCards[5], newCards[8]);
		combs[29].setCards (newCards[2], newCards[3], newCards[4], newCards[5], newCards[8]);

		//5, 8, 9 combination
		combs[30].setCards (newCards[0], newCards[1], newCards[4], newCards[7], newCards[8]);
		combs[31].setCards (newCards[0], newCards[2], newCards[4], newCards[7], newCards[8]);
		combs[32].setCards (newCards[0], newCards[3], newCards[4], newCards[7], newCards[8]);
		combs[33].setCards (newCards[1], newCards[2], newCards[4], newCards[7], newCards[8]);
		combs[34].setCards (newCards[1], newCards[3], newCards[4], newCards[7], newCards[8]);
		combs[35].setCards (newCards[2], newCards[3], newCards[4], newCards[7], newCards[8]);

		//6, 7, 8 combination
		combs[36].setCards (newCards[0], newCards[1], newCards[5], newCards[6], newCards[7]);
		combs[37].setCards (newCards[0], newCards[2], newCards[5], newCards[6], newCards[7]);
		combs[38].setCards (newCards[0], newCards[3], newCards[5], newCards[6], newCards[7]);
		combs[39].setCards (newCards[1], newCards[2], newCards[5], newCards[6], newCards[7]);
		combs[40].setCards (newCards[1], newCards[3], newCards[5], newCards[6], newCards[7]);
		combs[41].setCards (newCards[2], newCards[3], newCards[5], newCards[6], newCards[7]);

		//6, 7, 9 combination
		combs[42].setCards (newCards[0], newCards[1], newCards[5], newCards[6], newCards[8]);
		combs[43].setCards (newCards[0], newCards[2], newCards[5], newCards[6], newCards[8]);
		combs[44].setCards (newCards[0], newCards[3], newCards[5], newCards[6], newCards[8]);
		combs[45].setCards (newCards[1], newCards[2], newCards[5], newCards[6], newCards[8]);
		combs[46].setCards (newCards[1], newCards[3], newCards[5], newCards[6], newCards[8]);
		combs[47].setCards (newCards[2], newCards[3], newCards[5], newCards[6], newCards[8]);

		//6, 8, 9 combination
		combs[48].setCards (newCards[0], newCards[1], newCards[5], newCards[7], newCards[8]);
		combs[49].setCards (newCards[0], newCards[2], newCards[5], newCards[7], newCards[8]);
		combs[50].setCards (newCards[0], newCards[3], newCards[5], newCards[7], newCards[8]);
		combs[51].setCards (newCards[1], newCards[2], newCards[5], newCards[7], newCards[8]);
		combs[52].setCards (newCards[1], newCards[3], newCards[5], newCards[7], newCards[8]);
		combs[53].setCards (newCards[2], newCards[3], newCards[5], newCards[7], newCards[8]);

		//7, 8, 9 combination
		combs[54].setCards (newCards[0], newCards[1], newCards[6], newCards[7], newCards[8]);
		combs[55].setCards (newCards[0], newCards[2], newCards[6], newCards[7], newCards[8]);
		combs[56].setCards (newCards[0], newCards[3], newCards[6], newCards[7], newCards[8]);
		combs[57].setCards (newCards[1], newCards[2], newCards[6], newCards[7], newCards[8]);
		combs[58].setCards (newCards[1], newCards[3], newCards[6], newCards[7], newCards[8]);
		combs[59].setCards (newCards[2], newCards[3], newCards[6], newCards[7], newCards[8]);
		
		result_hi = result_lo = null;
	}

	/** Returns the card with ID x.
	 *
	 * @param x The ID of the the card. Accepted values are between 0 and 8 inclusively.
	 *
	 * @return The card with the ID x. The same order as the one provided to the constructor is kept.
	 * If the parameter x is out of bounds, null is returned.
	 */
	public Card getCard (int x)
	{
		return (x < 0 || x > 8) ? null : cards[x];
	}

	/** Determines what is the best combination of cards for this particular player.
	 * In Omaha, you can only use 2 cards from your hand and 3 cards from the community cards
	 * to make a standard 5-card combination. The best is determined here.
	 *
	 * @return A string representation of the best combination.
	 * The first character of the string is the rank of the combination, followed by the cards
	 * that compose it.
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
		//cache check
		if (result_hi != null)
		{
			return result_hi;
		}
		
		String[] rezs = new String[60];
		int i, j, rezssize = 0, compare_result;

		//check every combination for a royal flush. If one is found, we are already done.
		for (i = 0; i < 60; ++i)
		{
			if (combs[i].getRoyalFlush ())
			{
				result_hi = "9";
				return result_hi;
			}
		}

		//check every combination for straight flushes. if one or more are found,
		//they will be added to the result-array.
		for (i = 0; i < 60; ++i)
		{
			if (! combs[i].getStraightFlush ().equals ("0"))
			{
				rezs[rezssize++] = combs[i].getStraightFlush ();
				result_hi = "8";
			}
		}

		//check every combination for quads. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getQuad ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getQuad ();
					result_hi = "7";
				}
			}
		}

		//check every combination for full houses. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getFullHouse ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getFullHouse ();
					result_hi = "6";
				}
			}
		}

		//check every combination for flushes. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getFlush ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getFlush ();
					result_hi = "5";
				}
			}
		}

		//check every combination for straights. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getStraight ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getStraight ();
					result_hi = "4";
				}
			}
		}

		//check every combination for sets. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getThreeOfAKind ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getThreeOfAKind ();
					result_hi = "3";
				}
			}
		}

		//check every combination for two pair. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getTwoPair ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getTwoPair ();
					result_hi = "2";
				}
			}
		}

		//check every combination for one pair. if one or more are found,
		//they will be added to the result-array.
		//this check will be avoided if something better was previously
		//found (i.e. the result-array is empty)
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				if (! combs[i].getOnePair ().equals ("0"))
				{
					rezs[rezssize++] = combs[i].getOnePair ();
					result_hi = "1";
				}
			}
		}

		//if the result-array is still empty (extremely rarely in Omaha, but it happens), then
		//all high cards are inserted.
		if (rezssize == 0)
		{
			for (i = 0; i < 60; ++i)
			{
				rezs[rezssize++] = combs[i].getHighCard ();
			}
			
			result_hi = "0";
		}

		//next, every combination is compared with every other combination
		//the "winning" combination will "eliminate" the losing one.
		//if there is a tie... well, it will probably be eliminated on a future comparison

		boolean[] eliminated = new boolean[rezssize];

        for (int tmpindex = 0; tmpindex < rezssize; tmpindex++)
        {
            eliminated[tmpindex] = false;
        }

		for (i = 0; i < rezssize - 1; ++i)
		{
			for (j = i + 1; j < rezssize; ++j)
			{
				if (! eliminated[i] && ! eliminated[j])
				{
					compare_result = compare_combs (rezs[i], rezs[j]);

					if (compare_result == 1)
					{
						eliminated[j] = true;
					}
					else if (compare_result == 2)
					{
						eliminated[i] = true;
					}
				}
			}
		}

		//next, we compose the result. The first non-eliminated combination found
		//is an excellent candidate to be returned (better combinations are not possible,
		//since this one would have been eliminated and weaker ones are already eliminated).
		
		for (i = 0; i < rezssize; ++i)
		{
			if (! eliminated[i])
			{
				result_hi = result_hi + rezs[i];
				
				return result_hi;
			}
		}

		//not having a winning combination is extremely unlikely (if I think about it it's quite impossible)
		//but nevertheless... if something like that does occur, nothing gets returned
		return null;
	}

	/** Checks if there is a Lo hand in this <code>OmahaCombination</code>. This is obviously
	 * useful only for Omaha Hi/Lo.
	 *
	 * @return A string representation of the Lo Combination. This would be a string of 5 characters, each
	 * representing a card that forms the Lo Combination. The cards are in descending order. If there is no
	 * lo combination, the string "0" is returned.
	 */
	public String getLoCombination ()
	{
		if (result_lo != null)
		{
			return result_lo;
		}
		
		int[][] rezs = new int[60][5];
		int[] ranks = new int[5];
		int i, j, compare_result, rezssize = 0;

		//go through every combination and look for Lo candidates
		for (i = 0; i < 60; ++i)
		{
			//what I need are the 5 cards that make up the combination.
			//getHighCard method gives me that
			String hc = combs[i].getHighCard ();

			//I will work with the numeric ranks of the cards.
			//it's a lot easier
			for (j = 0; j < 5; ++j)
			{
				ranks[j] = Card.getRank (hc.charAt (j));
			}

			//if there are duplicate cards, the combination is not a valid Lo candidate
			if (ranks[0] == ranks[1] || ranks[1] == ranks[2] || ranks[2] == ranks[3] || ranks[3] == ranks[4])
			{
				continue;
			}

			//if any of the 5 cards is bigger than 8, the combination is not a valid Lo candidate
			//exception if it is an Ace
			if ((ranks[0] > 8 && ranks[0] != 14) ||
				(ranks[1] > 8 && ranks[1] != 14) ||
				(ranks[2] > 8 && ranks[2] != 14) ||
				(ranks[3] > 8 && ranks[3] != 14) ||
				(ranks[4] > 8 && ranks[4] != 14))
				{
					continue;
				}

			//if all the checks are passed, I add the combination as a valid candidate
			for (j = 0; j < 5; ++j)
			{
				rezs[rezssize][j] = ranks[j];
			}

			++rezssize;
		}

		String stt = "0";

		//if no combination is a candidate for Lo, there's nothing more we can do
		if (rezssize == 0)
		{
			return stt;
		}

		//if there is an Ace, move it in the back and give it value 1 (instead of 14)
		//this way, the combination comparator is easier to implement (a lot easier, trust me)
		for (i = 0; i < rezssize; ++i)
		{
			if (rezs[i][0] == 14)
			{
				rezs[i][0] = rezs[i][1];
				rezs[i][1] = rezs[i][2];
				rezs[i][2] = rezs[i][3];
				rezs[i][3] = rezs[i][4];
				rezs[i][4] = 1;
			}
		}

		//next I do exactly as in the getCombination method.
		//every combination is compared with all the others and they "fight" with each other
		//the losers are eliminated. if there is a tie, the combination gets another chance

		boolean[] eliminated = new boolean[rezssize];

		for (int tmpindex = 0; tmpindex < rezssize; tmpindex++)
        {
            eliminated[tmpindex] = false;
        }
		
		for (i = 0; i < rezssize - 1; ++i)
		{
			for (j = i + 1; j < rezssize; ++j)
			{
				if (! eliminated[i] && ! eliminated[j])
				{
					compare_result = compare_combs_lo (rezs[i], rezs[j]);

					if (compare_result == 1)
					{
						eliminated[j] = true;
					}
					else if (compare_result == 2)
					{
						eliminated[i] = true;
					}
				}
			}
		}

		//now let's see who is still left standing...
		for (i = 0; i < rezssize; ++i)
		{
			if (! eliminated[i])
			{
				//turn the 1 back to 14 (Ace)
				//I must admit I almost forgot this :P
				rezs[i][4] = (rezs[i][4] == 1) ? 14 : rezs[i][4];

				//compose result
				stt = Character.toString (Card.getCharCard (rezs[i][0]));
				stt += Card.getCharCard (rezs[i][1]);
				stt += Card.getCharCard (rezs[i][2]);
				stt += Card.getCharCard (rezs[i][3]);
				stt += Card.getCharCard (rezs[i][4]);
				
				return stt;
			}
		}
		
		return stt;
	}
	
	//compares 2 lo hands (assumed length is 5). if hand1 wins, 1 is returned, if hand2 wins, 2 is returned.
	//if it's a tie, 0 is returned
	private int compare_combs_lo (int[] hand1, int[] hand2)
	{
		for (int i = 0; i < 5; ++i)
		{
			if (hand1[i] > hand2[i])
			{
				return 2;
			}
			else if (hand2[i] > hand1[i])
			{
				return 1;
			}
		}

		return 0;
	}

	//the lengths of the 2 parameters is assumed to be equal
	private int compare_combs (String hand1, String hand2)
	{
		for (int i = 0; i < hand1.length (); ++i)
		{
			if (Card.getRank (hand1.charAt (i)) > Card.getRank (hand2.charAt (i)))
			{
				return 1;
			}
			else if (Card.getRank (hand2.charAt (i)) > Card.getRank (hand1.charAt (i)))
			{
				return 2;
			}
		}

		return 0;
	}

	/** Checks if this object equals with the parameter oc.
	 *
	 * @param oc The object to be compared.
	 *
	 * @return true if this object equals oc, false otherwise. Please note that, if the order of the player's
	 * cards is different (i.e. the first 4 cards sent to the constructor) or if the order of the community cards
	 * is different (i.e. the last 5 cards sent to the constructor), equality is maintained.
	 */
	@Override
	public boolean equals (Object oc)
	{
		//"this" object cannot be equal to null
		if (oc == null)
		{
			return false;
		}
		
		if (oc == this)
		{
			return true;
		}

		if (! getClass ().equals (oc.getClass ()))
		{
			return false;
		}

		OmahaCombination param = (OmahaCombination) oc;

		Card[] local = new Card[9], imported = new Card[9];
		int i, j;
		Card aux;

		//let's start with the player's cards (i.e. the first 4 cards in the combination).

		//first let's import them
		for (i = 0; i < 4; ++i)
		{
			local[i] = cards[i];
			imported[i] = param.getCard (i);
		}

		//now let's sort by their rank (if they have the same rank, sorting by color is applied)
		for (i = 0; i < 4 - 1; ++i)
		{
			for (j = i + 1; j < 4; ++j)
			{
				if (local[i].getRank () < local[j].getRank ())
				{
					aux = local[i]; local[i] = local[j]; local[j] = aux;
				}
				else if (local[i].getRank () == local[j].getRank ())
				{
					if (local[i].getColor () > local[j].getColor ())
					{
						aux = local[i]; local[i] = local[j]; local[j] = aux;
					}
				}

				if (imported[i].getRank () < imported[j].getRank ())
				{
					aux = imported[i]; imported[i] = imported[j]; imported[j] = aux;
				}
				else if (imported[i].getRank () == imported[j].getRank ())
				{
					if (imported[i].getColor () > imported[j].getColor ())
					{
						aux = imported[i]; imported[i] = imported[j]; imported[j] = aux;
					}
				}
			}
		}

		//let's see if they match. If not, we're lucky and we won't have to stress out
		//the CPU with further computation of a totally useless method
		for (i = 0; i < 4; ++i)
		{
			if (! local[i].equals (imported[i]))
			{
				return false;
			}
		}

		//if the player's cards pass the test, let's move on to the community cards

		//let's import them first
		for (i = 4; i < 9; ++i)
		{
			local[i] = cards[i];
			imported[i] = param.getCard (i);
		}

		//sort them by their rank (and color if they have the same rank)
		for (i = 4; i < 9 - 1; ++i)
		{
			for (j = i + 1; j < 9; ++j)
			{
				if (local[i].getRank () < local[j].getRank ())
				{
					aux = local[i]; local[i] = local[j]; local[j] = aux;
				}
				else if (local[i].getRank () == local[j].getRank ())
				{
					if (local[i].getColor () > local[j].getColor ())
					{
						aux = local[i]; local[i] = local[j]; local[j] = aux;
					}
				}

				if (imported[i].getRank () < imported[j].getRank ())
				{
					aux = imported[i]; imported[i] = imported[j]; imported[j] = aux;
				}
				else if (imported[i].getRank () == imported[j].getRank ())
				{
					if (imported[i].getColor () > imported[j].getColor ())
					{
						aux = imported[i]; imported[i] = imported[j]; imported[j] = aux;
					}
				}
			}
		}

		//if they don't match, we have no equality
		for (i = 4; i < 9; ++i)
		{
			if (! local[i].equals (imported[i]))
			{
				return false;
			}
		}

		//if all tests pass, the 2 objects are equal
		return true;
	}

	/** Returns the hash code value for this <code>OmahaCombination</code>.
	 *
	 * @return a hash code value for this <code>OmahaCombination</code>.
	 */
	@Override
	public int hashCode ()
	{
		int rez = 0;

		for (int i = 0; i < 9; i++)
		{
			//add all the ranks
			rez += cards[i].hashCode ();
		}

		return rez;
	}

	/** Returns a string representation of this object.
	 *
	 * @return A <code>String</code> of 18 characters (2 for each <code>Card</code>) representing all the
	 * cards that this object consists of. The cards will be in the same order as they were provided to the
	 * constructor.
	 */
	@Override
	public String toString ()
	{
		StringBuilder rez = new StringBuilder ();

		for (int i = 0; i < 9; i++)
		{
			rez.append (cards[i].toString ());
		}

		return rez.toString ();
	}
}