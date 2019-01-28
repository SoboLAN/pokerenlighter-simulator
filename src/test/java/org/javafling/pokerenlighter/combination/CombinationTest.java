package org.javafling.pokerenlighter.combination;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Radu Murzea
 */
public class CombinationTest
{
	/**
	 * Test of setCards method, of class Combination.
	 */
	@Test
	public void testSetCards()
	{
		Card initialCard = new Card('3', 'c');
		Card a = new Card(4, 'c');
		Card b = new Card('J', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		Combination instance = new Combination (initialCard, initialCard, initialCard, initialCard, initialCard);
		instance.setCards (a, b, c, d, e);
		
		assertEquals(instance.getCard (0), d);
		assertEquals(instance.getCard (1), b);
		assertEquals(instance.getCard (2), c);
		assertEquals(instance.getCard (3), e);
		assertEquals(instance.getCard (4), a);
	}

	/**
	 * Test of getCard method, of class Combination.
	 */
	@Test
	public void testGetCard()
	{
		Card a = new Card(4, 'c');
		Card b = new Card('J', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		Card expResult = c;
		Card result = instance.getCard (2);
		assertEquals (expResult, result);
		
		a = new Card(6, 's');
		b = new Card('Q', 's');
		c = new Card('3', 'd');
		d = new Card(6, 'c');
		e = new Card('K', 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = d;
		result = instance.getCard (3);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getHighCard method, of class Combination.
	 */
	@Test
	public void testGetHighCard()
	{
		Card a = new Card(4, 'c');
		Card b = new Card('J', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination(a, b, c, d, e);
		String expResult = "AJ854";
		String result = instance.getHighCard ();
		assertEquals (expResult, result);
		
		a = new Card(4, 'c');
		b = new Card('J', 's');
		c = new Card(4, 'd');
		d = new Card(10, 'c');
		e = new Card(9, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "JT944";
		result = instance.getHighCard ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getOnePair method, of class Combination.
	 */
	@Test
	public void testGetOnePair()
	{
		Card a = new Card(4, 'c');
		Card b = new Card('J', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination(a, b, c, d, e);
		String expResult = "0";
		String result = instance.getOnePair ();
		assertEquals (expResult, result);
		
		a = new Card(4, 'c');
		b = new Card('A', 's');
		c = new Card('8', 'd');
		d = new Card(14, 'c');
		e = new Card(5, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "A854";
		result = instance.getOnePair ();
		assertEquals (expResult, result);
		
		a = new Card(9, 'c');
		b = new Card('A', 's');
		c = new Card('9', 'd');
		d = new Card(14, 'c');
		e = new Card(9, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "A999";
		result = instance.getOnePair ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getTwoPair method, of class Combination.
	 */
	@Test
	public void testGetTwoPair()
	{
		Card a = new Card(4, 'c');
		Card b = new Card('J', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination(a, b, c, d, e);
		String expResult = "0";
		String result = instance.getTwoPair ();
		assertEquals (expResult, result);
		
		a = new Card(11, 'c');
		b = new Card('7', 's');
		c = new Card('J', 'd');
		d = new Card(14, 'c');
		e = new Card(7, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "J7A";
		result = instance.getTwoPair ();
		assertEquals (expResult, result);
		
		a = new Card(9, 'c');
		b = new Card('A', 's');
		c = new Card('9', 'd');
		d = new Card(14, 'c');
		e = new Card(9, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "A99";
		result = instance.getTwoPair ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getThreeOfAKind method, of class Combination.
	 */
	@Test
	public void testGetThreeOfAKind()
	{
		Card a = new Card(4, 'c');
		Card b = new Card('8', 's');
		Card c = new Card('8', 'd');
		Card d = new Card(14, 'c');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getThreeOfAKind ();
		assertEquals (expResult, result);
		
		a = new Card(11, 'c');
		b = new Card('K', 's');
		c = new Card('J', 'd');
		d = new Card(11, 'c');
		e = new Card(7, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "JK7";
		result = instance.getThreeOfAKind ();
		assertEquals (expResult, result);
		
		a = new Card(4, 'c');
		b = new Card('K', 's');
		c = new Card('4', 'd');
		d = new Card(4, 'h');
		e = new Card(13, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "4KK";
		result = instance.getThreeOfAKind ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getStraight method, of class Combination.
	 */
	@Test
	public void testGetStraight()
	{
		Card a = new Card(7, 'c');
		Card b = new Card(6, 's');
		Card c = new Card(5, 'd');
		Card d = new Card(4, 'c');
		Card e = new Card(9, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getStraight ();
		assertEquals (expResult, result);
		
		a = new Card(12, 'c');
		b = new Card(13, 's');
		c = new Card(11, 'd');
		d = new Card(9, 'c');
		e = new Card(10, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "K";
		result = instance.getStraight ();
		assertEquals (expResult, result);
		
		//wheel
		a = new Card(3, 'c');
		b = new Card(14, 's');
		c = new Card(4, 'd');
		d = new Card(2, 'h');
		e = new Card(5, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "5";
		result = instance.getStraight ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFlush method, of class Combination.
	 */
	@Test
	public void testGetFlush()
	{
		Card a = new Card(9, 'd');
		Card b = new Card(8, 'd');
		Card c = new Card(3, 'd');
		Card d = new Card(12, 'd');
		Card e = new Card(9, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getFlush ();
		assertEquals (expResult, result);
		
		a = new Card(7, 'h');
		b = new Card(10, 'h');
		c = new Card(11, 'h');
		d = new Card(9, 'h');
		e = new Card(4, 'h');
		
		instance.setCards (a, b, c, d, e);
		expResult = "JT974";
		result = instance.getFlush ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getFullHouse method, of class Combination.
	 */
	@Test
	public void testGetFullHouse()
	{
		Card a = new Card(9, 'd');
		Card b = new Card(5, 'd');
		Card c = new Card(9, 's');
		Card d = new Card(12, 'd');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getFullHouse ();
		assertEquals (expResult, result);
		
		a = new Card(7, 'h');
		b = new Card(10, 'h');
		c = new Card(10, 'c');
		d = new Card(7, 's');
		e = new Card(7, 'd');
		
		instance.setCards (a, b, c, d, e);
		expResult = "7T";
		result = instance.getFullHouse ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getQuad method, of class Combination.
	 */
	@Test
	public void testGetQuad()
	{
		Card a = new Card(4, 'd');
		Card b = new Card(12, 'c');
		Card c = new Card(9, 's');
		Card d = new Card(12, 'd');
		Card e = new Card(5, 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getQuad ();
		assertEquals (expResult, result);
		
		a = new Card(7, 'h');
		b = new Card(10, 'h');
		c = new Card(10, 'c');
		d = new Card(7, 's');
		e = new Card(7, 'd');
		
		instance.setCards (a, b, c, d, e);
		expResult = "0";
		result = instance.getQuad ();
		assertEquals (expResult, result);
		
		a = new Card(12, 'h');
		b = new Card(4, 'h');
		c = new Card(12, 'c');
		d = new Card(12, 's');
		e = new Card(12, 'd');
		
		instance.setCards (a, b, c, d, e);
		expResult = "Q4";
		result = instance.getQuad ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getStraightFlush method, of class Combination.
	 */
	@Test
	public void testGetStraightFlush()
	{
		Card a = new Card(8, 'h');
		Card b = new Card(9, 'h');
		Card c = new Card('T', 's');
		Card d = new Card('J', 'h');
		Card e = new Card('Q', 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		String expResult = "0";
		String result = instance.getStraightFlush ();
		assertEquals (expResult, result);
		
		a = new Card(9, 'd');
		b = new Card(10, 'd');
		c = new Card(11, 'd');
		d = new Card(12, 'd');
		e = new Card(13, 'd');
		
		instance.setCards (a, b, c, d, e);
		expResult = "K";
		result = instance.getStraightFlush ();
		assertEquals (expResult, result);
		
		a = new Card(3, 'c');
		b = new Card(4, 'c');
		c = new Card(5, 'c');
		d = new Card(2, 'c');
		e = new Card(14, 'c');
		
		instance.setCards (a, b, c, d, e);
		expResult = "5";
		result = instance.getStraightFlush ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getRoyalFlush method, of class Combination.
	 */
	@Test
	public void testGetRoyalFlush()
	{
		Card a = new Card(8, 'h');
		Card b = new Card(9, 'h');
		Card c = new Card('T', 'h');
		Card d = new Card('J', 'h');
		Card e = new Card('Q', 'h');
		
		Combination instance = new Combination (a, b, c, d, e);
		boolean expResult = false;
		boolean result = instance.getRoyalFlush ();
		assertEquals (expResult, result);
		
		a = new Card(14, 'd');
		b = new Card(10, 'd');
		c = new Card(11, 'd');
		d = new Card(12, 'd');
		e = new Card(13, 'd');
		
		instance.setCards (a, b, c, d, e);
		expResult = true;
		result = instance.getRoyalFlush ();
		assertEquals (expResult, result);
		
		a = new Card(3, 'c');
		b = new Card(4, 'c');
		c = new Card(5, 'c');
		d = new Card(2, 'c');
		e = new Card(14, 'c');
		
		instance.setCards (a, b, c, d, e);
		expResult = false;
		result = instance.getRoyalFlush ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of equals method, of class Combination.
	 */
	@Test
	public void testEquals()
	{
		//situation 1
		Card a = new Card(8, 'd');
		Card b = new Card(9, 's');
		Card c = new Card('T', 'h');
		Card d = new Card(7, 'd');
		Card e = new Card(2, 'c');
		
		Combination instance = new Combination (a, b, c, d, e);
		
		Combination anotherInstance = new Combination (b, e, c, a, d);
		
		boolean expResult = true;
		boolean result = instance.equals (anotherInstance);
		assertEquals (expResult, result);
		
		//situation 2
		instance.setCards (a, b, new Card ('Q', 'd'), d, e);
		
		expResult = false;
		result = instance.equals (anotherInstance);
		
		assertEquals (expResult, result);
		
		//situation 3
		expResult = false;
		result = instance.equals(null);
		assertEquals (expResult, result);
		
		//situation 4
		expResult = true;
		result = instance.equals(instance);
		assertEquals (expResult, result);
	}

	/**
	 * Test of toString method, of class Combination.
	 */
	@Test
	public void testToString()
	{
		Card a = new Card(8, 'd');
		Card b = new Card(9, 's');
		Card c = new Card('T', 'h');
		Card d = new Card(7, 'd');
		Card e = new Card(2, 'c');
		
		Combination instance = new Combination (a, b, c, d, e);
		
		String expResult = "Th9s8d7d2c";
		String result = instance.toString ();
		assertEquals (expResult, result);
	}
}