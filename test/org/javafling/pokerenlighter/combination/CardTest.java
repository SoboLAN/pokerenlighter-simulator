package org.javafling.pokerenlighter.combination;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Radu Murzea
 */
public class CardTest
{
	/**
	 * Test of isSuited method, of class Card.
	 */
	@Test
	public void testIsSuited()
	{
		Card a = new Card('J', 'c');
		Card instance = new Card ('4', 'c');
		boolean expResult = true;
		boolean result = instance.isSuited (a);
		assertEquals (expResult, result);
		
		a = new Card(5, 's');
		instance = new Card (12, 's');
		expResult = true;
		result = instance.isSuited (a);
		assertEquals (expResult, result);
		
		a = new Card(7, 's');
		instance = new Card (13, 'd');
		expResult = false;
		result = instance.isSuited (a);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getRank method, of class Card.
	 */
	@Test
	public void testGetRank()
	{
		Card instance = new Card ('J', 'c');
		int expResult = 11;
		int result = instance.getRank ();
		assertEquals (expResult, result);
		
		instance = new Card (6, 'h');
		expResult = 6;
		result = instance.getRank ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getColor method, of class Card.
	 */
	@Test
	public void testGetColor()
	{
		Card instance = new Card('5', 's');
		char expResult = 's';
		char result = instance.getColor ();
		assertEquals (expResult, result);
		
		instance = new Card('A', 'd');
		expResult = 'd';
		result = instance.getColor ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getCharCard method, of class Card.
	 */
	@Test
	public void testGetCharCard()
	{
		Card instance = new Card (4, 'c');
		char expResult = '4';
		char result = instance.getCharCard ();
		assertEquals (expResult, result);
		
		instance = new Card ('K', 's');
		expResult = 'K';
		result = instance.getCharCard ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of getRank method, of class Card.
	 */
	@Test
	public void testGetRankStatic()
	{
		char c = '5';
		int expResult = 5;
		int result = Card.getRank (c);
		assertEquals (expResult, result);
		
		c = '8';
		expResult = 8;
		result = Card.getRank (c);
		assertEquals (expResult, result);
		
		c = 'Q';
		expResult = 12;
		result = Card.getRank (c);
		assertEquals (expResult, result);
	}

	/**
	 * Test of getCharCard method, of class Card.
	 */
	@Test
	public void testGetCharCardStatic()
	{
		int x = 7;
		char expResult = '7';
		char result = Card.getCharCard (x);
		assertEquals (expResult, result);
		
		x = 14;
		expResult = 'A';
		result = Card.getCharCard (x);
		assertEquals (expResult, result);
		
		x = 10;
		expResult = 'T';
		result = Card.getCharCard (x);
		assertEquals (expResult, result);
	}

	/**
	 * Test of equals method, of class Card.
	 */
	@Test
	public void testEquals()
	{
		Object c = new Card ('5', 'd');
		Card instance = new Card (5, 'd');
		boolean expResult = true;
		boolean result = instance.equals (c);
		assertEquals (expResult, result);
		
		c = new Card (9, 'd');
		instance = new Card ('J', 'd');
		expResult = false;
		result = instance.equals (c);
		assertEquals (expResult, result);
		
		c = null;
		instance = new Card ('K', 'd');
		expResult = false;
		result = instance.equals (c);
		assertEquals (expResult, result);
	}

	/**
	 * Test of hashCode method, of class Card.
	 */
	@Test
	public void testHashCode()
	{
		Card instance = new Card(6, 'c');
		int expResult = 6;
		int result = instance.hashCode ();
		assertEquals (expResult, result);
		
		instance = new Card(4, 'd');
		expResult = 8;
		result = instance.hashCode ();
		assertEquals (expResult, result);
		
		instance = new Card('J', 'h');
		expResult = 33;
		result = instance.hashCode ();
		assertEquals (expResult, result);
		
		instance = new Card(13, 's');
		expResult = 52;
		result = instance.hashCode ();
		assertEquals (expResult, result);
	}

	/**
	 * Test of toString method, of class Card.
	 */
	@Test
	public void testToString()
	{
		Card instance = new Card('Q', 'h');
		String expResult = "Qh";
		String result = instance.toString ();
		assertEquals (expResult, result);
		
		instance = new Card(13, 'd');
		expResult = "Kd";
		result = instance.toString ();
		assertEquals (expResult, result);
	}
}