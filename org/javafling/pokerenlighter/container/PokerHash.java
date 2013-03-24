package org.javafling.pokerenlighter.container;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/** Custom implementation of a hash table. There are exactly 270725 different 4-card combinations that a player
* can have in Omaha, so a data structure with extremely high speed of retrieval was needed. 
*/
public abstract class PokerHash
{
	/** This is the size of the hash table.
	*/
	protected int HSIZE;

	/** The number of elements in the hash table.
	*/
	protected int HELEM;

	/** Creates a hash table designed to store Texas Hold'em or Omaha hands inside.
	*
	* @param size the size of the table.
	* @param nrelem maximum number of elements to be stored inside the table.
	*/
	public PokerHash (int size, int nrelem)
	{
		if (nrelem >= size)
		{
			throw new IllegalArgumentException ();
		}

		HSIZE = size;
		HELEM = nrelem;
	}

	/** The function used to hash the hands (Strings of 3 or 8 characters each), a.k.a. APHash.
	* In Omaha, the performance of this hash function is: Max probes: 286, Average probes: 3.61. Results were
	* obtained at 70% load factor. Although is doesn't seem like it, 286 is very small, because it represents
	* less than 0.1 % of the table's size.
	*
	* @param tois The string to be hashed.
	*
	* @return The hash value. This will be between 0 and HSIZE - 1.
	*/
	protected final int hfunc (String tois)
	{
		long hash = 0xAAAAAAAA;	//in decimal this is 2,863,311,530

		for (int i = 0; i < tois.length (); ++i)
		{
			if ((i & 1) == 0)
			{
				hash ^= ((hash << 7) ^ tois.charAt(i) * (hash >> 3));
			}
			else
			{
				hash ^= (~((hash << 11) + tois.charAt(i) ^ (hash >> 5)));
			}
		}

		return (int) (Math.abs (hash % HSIZE));
	}

	//partitioning function used for quicksort
	private int partitionize (PokerHand[] v, int p, int r)
	{
		int i, j;
		PokerHand x, aux;

		x = v[p];

		i = p - 1;
		j = r + 1;

		while (true)
		{
			do
			{
				--j;
			} while (v[j].getWinPercentage () < x.getWinPercentage ());

			do
			{
				++i;
			} while (v[i].getWinPercentage () > x.getWinPercentage ());

			if (i < j)
			{
				aux = v[i];
				v[i] = v[j];
				v[j] = aux;
			}
			else
			{
				return j;
			}
		}
	}

	//quick sort implementation. It is used to sort the Hands before dumping the results in a file
	private void quick_sort (PokerHand[] v, int p, int r)
	{
		int q;

		if (p < r)
		{
			q = partitionize (v, p, r);
			quick_sort (v, p, q);
			quick_sort (v, q + 1, r);
		}
	}

	public void dumpToFile (PokerHand[] hands, String filename)
	{
		PokerHand[] tmp_hands = new PokerHand[HELEM];

		int i, j;
		
		for (i = 0, j = 0; i < HSIZE; ++i)
		{
			if (hands[i] != null)
			{
				tmp_hands[j] = hands[i];
				++j;
			}
		}

		quick_sort (tmp_hands, 0, j - 1);

		BufferedWriter out = null;
		
		DecimalFormat df = new DecimalFormat ();
		df.setMaximumFractionDigits (2);

		try
		{
			out = new BufferedWriter (new FileWriter (filename));

			out.write ("Results (in descending order of winning percentage):");
			out.newLine ();
			out.newLine ();
			
			for (i = 0; i < j; ++i)
			{
				out.write (tmp_hands[i].toString () + ": Won in " + tmp_hands[i].getWins () + " of " + tmp_hands[i].getOccurrence () + " cases");
				out.write (" (" + df.format (tmp_hands[i].getWinPercentage ()) + " %)");
				out.newLine ();
			}

			out.close ();
		}
		catch (IOException e)
		{
			System.err.println ("Error while writing results to " + filename + ": " + e.getMessage ());
			System.exit (4);
		}
	}
}