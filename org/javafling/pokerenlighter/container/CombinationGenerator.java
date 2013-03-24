package org.javafling.pokerenlighter.container;

//----------------------------------------------------------------------
// The CombinationGenerator Java class systematically generates
// all combinations of n elements, taken r at a time. The algorithm is
// described by Kenneth H. Rosen, Discrete Mathematics and Its
// Applications, 2nd edition (NY: McGraw-Hill, 1991), pp. 284-286.
//----------------------------------------------------------------------

import java.math.BigInteger;

public class CombinationGenerator
{
	private int[] a;
	private int n;
	private int r;
	private BigInteger numLeft;
	private BigInteger total;

	//------------
	// Constructor
	//------------
	public CombinationGenerator (int n, int r)
	{
		if (r > n || n < 1)
		{
			throw new IllegalArgumentException ();
		}

		this.n = n;
		this.r = r;
		a = new int[r];
		BigInteger nFact = getFactorial (n);
		BigInteger rFact = getFactorial (r);
		BigInteger nminusrFact = getFactorial (n - r);
		total = nFact.divide (rFact.multiply (nminusrFact));
		reset ();
	}

	//------
	// Reset
	//------
	public void reset ()
	{
		for (int i = 0; i < a.length; a[i] = i, ++i);
		
		numLeft = new BigInteger (total.toString ());
	}

	//------------------------------------------------
	// Return number of combinations not yet generated
	//------------------------------------------------
	public BigInteger getNumLeft ()
	{
		return numLeft;
	}

	//-----------------------------
	// Are there more combinations?
	//-----------------------------
	public boolean hasMore ()
	{
		return numLeft.compareTo (BigInteger.ZERO) == 1;
	}

	//------------------------------------
	// Return total number of combinations
	//------------------------------------
	public BigInteger getTotal ()
	{
		return total;
	}

	//------------------
	// Compute factorial
	//------------------
	private static BigInteger getFactorial (int n)
	{
		BigInteger fact = BigInteger.ONE;
	
		for (int i = n; i > 1; --i)
		{
			fact = fact.multiply (new BigInteger (Integer.toString (i)));
		}
		
		return fact;
	}

	//--------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	//--------------------------------------------------------
	public int[] getNext ()
	{
		if (numLeft.equals (total))
		{
			numLeft = numLeft.subtract (BigInteger.ONE);
		
			return a;
		}

		int i = r - 1;
		while (a[i] == n - r + i)
		{
			--i;
		}
		
		++a[i];

		for (int j = i + 1; j < r; ++j)
		{
			a[j] = a[i] + j - i;
		}

		numLeft = numLeft.subtract (BigInteger.ONE);

		return a;
	}
}