package org.javafling.pokerenlighter.container;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TexasHash extends PokerHash
{
	private TexasHand[] hands;
	
	/** Make a hash table for storing Texas Hold'em hands.
	*
	* @param size The size of the table.
	*
	* @param nrelem The maximum number of hands to be stored in this table.
	*/
	public TexasHash (int size, int nrelem)
	{
		super (size, nrelem);
		
		hands = new TexasHand[size];

		InputStreamReader in = null;
		BufferedReader buff;

		try
		{
			in = new InputStreamReader (getClass ().getResource ("cardtypes.txt").openStream ());
			buff = new BufferedReader (in);
			
			String line;
			TexasHand tmp_hc;
			int i;

			while ((line = buff.readLine ()) != null)
			{
				tmp_hc = new TexasHand (line.charAt (0), line.charAt (1), (line.charAt (2) == '0') ? false : true);

				i = hfunc (tmp_hc.toString ());

				while (hands[i] != null)
				{
					i = (i == HSIZE - 1) ? 0 : i + 1;
				}

				hands[i] = tmp_hc;
			}
		}
		catch (IOException e)
		{
			System.out.println ("error reading from file: " + e.getMessage ());
			System.exit (123);
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close ();
				}
				catch (IOException e2)
				{
				
				}
			}
		}
	}
	
	/** Finds a TexasHand object in the hashtable that contains the cards given as parameters.
	* You may provide the cards in any order you wish.
	*
	* @param card1 The first card of the TexasHand.
	*
	* @param card2 The second card of the TexasHand.
	*
	* @param suit The suit property. Should be true if the hand is suited, false if it is not.
	*
	* @return A reference to a TexasHand which contains the 2 cards given as parameters. If no such TexasHand object
	* exists, null is returned.
	*/
	public TexasHand getTexasHand (char card1, char card2, boolean suit)
	{
		TexasHand tmp_hc = new TexasHand (card1, card2, suit);

		int i = hfunc (tmp_hc.toString ());

		boolean gone_over = false;

		//keep going until we find what we want (or until it's pointless to keep searching)
		while (hands[i] == null || ! hands[i].compare (tmp_hc))	//TODO: fix this
		{
			i++;
			
			//if we're at the end of the table, skip to the beginning
			if (i == HSIZE)
			{
				//if we already skipped to the beginning once, we won't do it again.
				//the TexasHand is clearly not in the table.
				if (gone_over)
				{
					return null;
				}

				i = 0;
				gone_over = true;
			}
		}

		return hands[i];
	}

	/** Prints the contents of this hash table to a file. Empty locations are ignored.
	*
	* @param filename The name of the output file.
	*/
	public void dumpToFile (String filename)
	{
		dumpToFile (hands, filename);
	}
}