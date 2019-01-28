package org.javafling.pokerenlighter.combination;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Radu Murzea
 */
public class DeckTest
{
	/**
	 * Test of removeCard method, of class Deck.
	 */
	@Test
	public void testRemoveCard()
	{
		Card c = new Card('T', 's');
		
		Deck instance = new Deck ();
		instance.removeCard (c);
		
		int index = instance.getCardIndex (c);
		
		assertEquals (-1, index);
	}

	/**
	 * Test of addCard method, of class Deck.
	 */
	@Test
	public void testAddCard()
	{
		Card c = new Card('J', 'd');
		Deck instance = new Deck ();
		
		instance.removeCard (c);
		
		int index = instance.getCardIndex (c);
		assertEquals(-1, index);
		
		instance.addCard (c);
		index = instance.getCardIndex (c);
		assertTrue(index != -1);
	}

	/**
	 * Test of isFull method, of class Deck.
	 */
	@Test
	public void testIsFull()
	{
		Deck instance = new Deck ();
		
		assertTrue(instance.isFull ());
		
		Card c = new Card('A', 'c');
		instance.removeCard (c);
		assertFalse (instance.isFull ());
		
	}

	/**
	 * Test of getSize method, of class Deck.
	 */
	@Test
	public void testGetSize()
	{
		Deck instance = new Deck ();
		
		//initially there should be 52 cards
		assertEquals(52, instance.getSize ());
		
		Card c1 = new Card('2', 'c');
		Card c2 = new Card('3', 'c');
		Card c3 = new Card('T', 's');
		
		instance.removeCard (c1);
		instance.removeCard (c2);
		instance.removeCard (c3);
		
		assertEquals (49, instance.getSize ());
	}

	/**
	 * Test of getCard method, of class Deck.
	 */
	@Test
	public void testGetCard()
	{
		Deck instance = new Deck ();
		
		Card c1 = instance.getCard (20);
		assertTrue(c1 instanceof Card);
		
		Card c2 = instance.getCard (147);
		assertFalse(c2 instanceof Card);
		
		Card c3 = instance.getCard (-9);
		assertFalse(c3 instanceof Card);
	}

	/**
	 * Test of getCardIndex method, of class Deck.
	 */
	@Test
	public void testGetCardIndex()
	{
		Card c = new Card('K', 'd');
		
		Deck instance = new Deck ();
		instance.shuffle (5);
		int result = instance.getCardIndex (c);
		
		assertTrue (result >= 0 && result < instance.getSize ());
	}
}