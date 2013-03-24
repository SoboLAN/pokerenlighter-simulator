package org.javafling.pokerenlighter.container;

/** This class is a very simple representation of a poker hand. It is useful to store the number
* of times a hand has occurred and the number of times someone has won with it.
*/
public abstract class PokerHand
{
	//storage for the number of occurrences
	private int occurrence;

	//storage for the number of wins
	private int wins;

	/** Constructor.
	*/
	public PokerHand ()
	{
		occurrence = 0;
		wins = 0;
	}

	/** Increases the number of occurrences for this PokerHand.
	*/
	public final void increaseOccurrence ()
	{
		++occurrence;
	}
	
	/** Returns the number of occurrences for this PokerHand.
	*
	* @return the number of occurrences.
	*/
	public final int getOccurrence ()
	{
		return occurrence;
	}

	/** Increases the number of wins for this PokerHand.
	*/
	public final void increaseWins ()
	{
		++wins;
	}

	/** Returns the number of wins for this PokerHand.
	*
	* @return the number of wins.
	*/
	public final int getWins ()
	{
		return wins;
	}

	/** Returns the win percentage of this PokerHand, based on the number
	* of occurrences and number of wins.
	* 
	* @return the win percentage.
	*/
	public final double getWinPercentage ()
	{
		if (occurrence == 0)
		{
			return 0;
		}
		else
		{
			return ((wins * 100.0) / occurrence);
		}
	}
}