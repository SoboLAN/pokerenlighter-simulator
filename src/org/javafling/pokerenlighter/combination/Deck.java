package org.javafling.pokerenlighter.combination;

/**
 * Representation of a standard deck of 52 (French) cards. There are 13 cards of each color (4 colors).
 * <br /><br />
 * This class is not thread-safe.
 * 
 * @author Radu Murzea
 * 
 * @version 1.2
*/
public class Deck
{
	//storage place for the cards
	private Card[] cards;
	
	//random number generator. Needed for the shuffle () method.
	private HighQualityRandomGenerator rand;
	
	//size of the deck (needed since adding and removal of cards from the deck is permitted)
	private int size;
	
	/**
	 * Constructs a deck of cards with the 52 unique cards.
	 * The initial order is: 2c, 3c, 4c, ... Kc, Ac, 2d, 3d, 4d, ... Kd, Ad, 2h, 3h, 4h, ... Kh, Ah,
	 * 2s, 3s, 4s, ... Ks, As.
	 * <br />
	 * By shuffling the deck, this arrangement is lost and cannot be restored.
	 */
	public Deck ()
	{
		size = 52;
		cards = new Card[size];

		int i, j;

		for (j = 2, i = 0; i < 13; ++j, ++i)
		{
			cards[i + 0 * 13] = new Card (j, 'c');
			cards[i + 1 * 13] = new Card (j, 'd');
			cards[i + 2 * 13] = new Card (j, 'h');
			cards[i + 3 * 13] = new Card (j, 's');
		}

		rand = new HighQualityRandomGenerator ();
	}

	/**
	 * Shuffles the <code>Deck</code>.
	 *
	 * @param intensity Specifies how much the <code>Deck</code> should be shuffled. Accepted values
	 * are between 5 and 30, 5 being least intense and 30 being most intense.
	 *
	 * @throws IllegalArgumentException if the intensity parameter is not between 5 and 30.
	 */
	public final void shuffle (int intensity)
	{
		if (intensity < 5 || intensity > 30)
		{
			throw new IllegalArgumentException ("invalid intensity range");
		}
		
		//repeat "intensity" times
		for (int j = 0; j < intensity; ++j)
		{
			//each card is swapped with another card (random)
			for (int i = 0; i < size - 1; ++i)
			{
				int r = i + (rand.nextInt (10) % (size - i));
				Card temp = cards[i];
				cards[i] = cards[r];
				cards[r] = temp;
			}
		}
	}
	
	/**
	 * Removes from the <code>Deck</code> the <code>Card</code> given by its parameter.
	 * If the <code>Card</code> is not in the <code>Deck</code> or the deck has a size
	 * less than 5, nothing happens.
	 *
	 * @param c The <code>Card</code> to be removed. 
	 */
	public final void removeCard (Card c)
	{
		if (c == null)
		{
			throw new NullPointerException ();
		}
		
		//check size of deck. The value is not randomly picked.
		//in Omaha (Hi/Lo), when there are 10 players playing (absolute maximum), there will be
		//10 * 4 = 40 cards for the players + 3 * 1 = 3 burn cards + 5 * 1 = 5 community cards in play.
		//52 - (40 + 3 + 5) = 52 - 48 = 4 remaining cards. So there is no reason to have
		//deck size smaller than 4.
		if (size <= 4)
		{
			return;
		}

		int i, j;

		//go through the whole deck
		for (i = 0; i < size; ++i)
		{
			//find the card
			if (cards[i].equals (c))
			{
				//when found, move following cards 1 position to the left
				//(basically losing the reference to the "killed" card)
				for (j = i; j < size - 1; ++j)
				{
					cards[j] = cards[j + 1];
				}

				//after the above operation, there are 2 references to the last card.
				//let's not keep that.
				cards[size - 1] = null;

				//one card is gone, so the size of the deck is smaller
				--size;

				return;
			}
		}
	}
	
	/**
	 * Adds a <code>Card</code> to the <code>Deck</code>. If the <code>Deck</code> is full or
	 * the <code>Card</code> already exists in the <code>Deck</code>, nothing happens.
	 *
	 * @param c The <code>Card</code> to be added. 
	 */
	public final void addCard (Card c)
	{
		if (c == null)
		{
			throw new NullPointerException ();
		}
		
		//if the deck is full... well, sorry.
		if (size == 52)
		{
			return;
		}
		
		int i;

		//if the card is already in the deck, do nothing.
		for (i = 0; i < size; ++i)
		{
			if (cards[i].equals (c))
			{
				return;
			}
		}

		//add the card and increase size
		cards[size++] = c;
	}

	/**
	 * Checks if the <code>Deck</code> is full (has all 52 cards).
	 *
	 * @return <code>true</code> if the <code>Deck</code> has 52 cards, <code>false</code> otherwise.
	 */
	public final boolean isFull ()
	{
		return (size == 52);
	}

	/**
	 * Returns the number of <code>Card</code>s in the <code>Deck</code>.
	 *
	 * @return The number of <code>Card</code>s.
	 */
	public final int getSize ()
	{
		return size;
	}

	/**
	 * Returns a reference to the x-th <code>Card</code> of the <code>Deck</code>.
	 *
	 * @param x The id of the <code>Card</code> to retrieve. The accepted
	 * values for x are in the range [0; size_of_deck - 1].
	 *
	 * @return The desired <code>Card</code>. If x is not in the required range, null is returned.
	 */
	public final Card getCard (int x)
	{
		return ((x < 0 || x >= size) ? null : cards[x]);
	}

	/**
	 * Searches for a <code>Card</code> inside the deck.
	 *
	 * @param c The <code>Card</code> to search for.
	 *
	 * @return The position in the <code>Deck</code> where the Card c is
	 * found, -1 if c is not in the <code>Deck</code>.
	 */
	public final int getCardIndex (Card c)
	{
		for (int i = 0; i < size; ++i)
		{
			if (cards[i].equals (c))
			{
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * Indicates whether some other <code>Deck</code> is equal to this one. Two decks are equal if
	 * they contain exactly the same cards (order of the cards is not relevant).
	 *
	 * @param dc The <code>Deck</code> with which to compare.
	 *
	 * @return true if this object contains exactly the same cards as the dc object, false otherwise.
	 */
	@Override
	public boolean equals (Object dc)
	{
		if (dc == null)
		{
			return false;
		}
		else if (dc == this)
		{
			return true;
		}

		//check to see if the parameter is a Deck
		if (! getClass ().equals (dc.getClass ()))
		{
			return false;
		}

		//now it's safe to cast it to Deck
		Deck param = (Deck) dc;

		//a different size means that the 2 objects are not equal.
		//there's not point in comparing any cards...
		if (size != param.getSize ())
		{
			return false;
		}

		Card[] fromdecklocal = new Card[size], fromdeckimport = new Card[size];
		Card aux;
		int i, j;

		//making some copies...
		for (i = 0; i < size; ++i)
		{
			fromdecklocal[i] = cards[i];
			fromdeckimport[i] = param.getCard (i);
		}

		//sort all cards from the deck according to rank AND color.
		for (i = 0; i < size - 1; ++i)
		{
			for (j = i + 1; j < size; ++j)
			{
				if (fromdecklocal[i].getRank () > fromdecklocal[j].getRank ())
				{
					aux = fromdecklocal[i]; fromdecklocal[i] = fromdecklocal[j]; fromdecklocal[j] = aux;
				}
				else if (fromdecklocal[i].getRank () == fromdecklocal[j].getRank ())
				{
					if (fromdecklocal[i].getColor () > fromdecklocal[j].getColor ())
					{
						aux = fromdecklocal[i]; fromdecklocal[i] = fromdecklocal[j]; fromdecklocal[j] = aux;
					}
				}

				if (fromdeckimport[i].getRank () > fromdeckimport[j].getRank ())
				{
					aux = fromdeckimport[i]; fromdeckimport[i] = fromdeckimport[j]; fromdeckimport[j] = aux;
				}
				else if (fromdeckimport[i].getRank () == fromdeckimport[j].getRank ())
				{
					if (fromdeckimport[i].getColor () > fromdeckimport[j].getColor ())
					{
						aux = fromdeckimport[i]; fromdeckimport[i] = fromdeckimport[j]; fromdeckimport[j] = aux;
					}
				}
			}
		}

		//let's see if they match
		for (i = 0; i < size; ++i)
		{
			if (! fromdecklocal[i].equals (fromdeckimport[i]))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns a string representation of this <code>Deck</code>.
	 *
	 * @return a string representation of this <code>Deck</code>, i.e. all the cards in the <code>Deck</code>.
	 * If the shuffle method was called, the cards will be in the shuffled order.
	 */
	@Override
	public String toString ()
	{
		StringBuilder rez = new StringBuilder ();

		for (int i = 0; i < size; ++i)
		{
			rez.append (cards[i].toString ());
		}

		return rez.toString ();
	}
}