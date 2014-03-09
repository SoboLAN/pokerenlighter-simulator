/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javafling.pokerenlighter.combination;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Radu Murzea
 */
public class OmahaCombinationTest
{
	/**
	 * Test of setCards method, of class OmahaCombination.
	 */
	@Test
	public void testSetCards()
	{
		Card[] initialCards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('7', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (initialCards);
		
		Card[] newCards = {
			new Card('A', 'h'),
			new Card('K', 's'),
			new Card('9', 'd'),
			new Card('2', 'h'),
			new Card('5', 'd'),
			new Card('J', 'c'),
			new Card('T', 's'),
			new Card('7', 'd'),
			new Card('7', 'c')
		};
		
		instance.setCards (newCards);
		
		assertEquals(newCards[0], instance.getCard (0));
		assertEquals(newCards[1], instance.getCard (1));
		assertEquals(newCards[2], instance.getCard (2));
		assertEquals(newCards[3], instance.getCard (3));
		assertEquals(newCards[4], instance.getCard (4));
		assertEquals(newCards[5], instance.getCard (5));
		assertEquals(newCards[6], instance.getCard (6));
		assertEquals(newCards[7], instance.getCard (7));
		assertEquals(newCards[8], instance.getCard (8));
	}

	/**
	 * Test of getCard method, of class OmahaCombination.
	 */
	@Test
	public void testGetCard()
	{
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('7', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		
		assertEquals(cards[0], instance.getCard (0));
		assertEquals(cards[1], instance.getCard (1));
		assertEquals(cards[2], instance.getCard (2));
		assertEquals(cards[3], instance.getCard (3));
		assertEquals(cards[4], instance.getCard (4));
		assertEquals(cards[5], instance.getCard (5));
		assertEquals(cards[6], instance.getCard (6));
		assertEquals(cards[7], instance.getCard (7));
		assertEquals(cards[8], instance.getCard (8));
	}
	
	/**
	 * Test recognition of high card of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationHighCard()
	{
		//high card: AKJ85
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('4', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "0AKJ85";
		assertEquals (expResult, instance.getCombination ());
	}

	/**
	 * Test recognition of pairs of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationPair()
	{
		//a pair of Tens with A, K, 8 kickers
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "1TAK8";
		assertEquals (expResult, instance.getCombination ());
		
		//no pair
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('7', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "0AKJT8";
		assertEquals (expResult, instance.getCombination ());
	}
	
	/**
	 * Test recognition of 2 pair of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationTwoPair()
	{
		//a pair of Tens and a pair of nines: 2T9K
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('9', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "2T9K";
		assertEquals (expResult, instance.getCombination ());
		
		//a quad of fives, should not be detected as 2 pair
		Card[] cards2 = {
			new Card('5', 'h'),
			new Card('5', 's'),
			new Card('7', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('5', 'd'),
			new Card('A', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "75A";
		assertEquals (expResult, instance.getCombination ());
	}
	
	/**
	 * Test recognition of 2 pair of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationSet()
	{
		//a set of Eights: 38AK
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('8', 'h'),
			new Card('A', 'h'),
			new Card('8', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "38AK";
		assertEquals (expResult, instance.getCombination ());
		
		//2 pair should not be detected as a set
		Card[] cards2 = {
			new Card('K', 'h'),
			new Card('5', 's'),
			new Card('7', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('K', 'd'),
			new Card('7', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "2K7T";
		assertEquals (expResult, instance.getCombination ());
	}
	
	/**
	 * Test recognition of straights of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationStraight()
	{
		//a straight, Jack high
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('7', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "4J";
		assertEquals (expResult, instance.getCombination ());
	}
	
	/**
	 * Test recognition of flushes of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationFlush()
	{
		//flush hand: AQJ64
		Card[] cards = {
			new Card('T', 'd'),
			new Card('6', 's'),
			new Card('Q', 's'),
			new Card('5', 'c'),
			new Card('2', 'c'),
			new Card('4', 's'),
			new Card('A', 's'),
			new Card('8', 'h'),
			new Card('J', 's')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "5AQJ64";
		assertEquals (expResult, instance.getCombination ());
		
		//a little trickier: all spades, so multiple flushes. highest is AQT87
		Card[] cards2 = {
			new Card('T', 's'),
			new Card('6', 's'),
			new Card('Q', 's'),
			new Card('5', 's'),
			new Card('2', 's'),
			new Card('3', 's'),
			new Card('A', 's'),
			new Card('8', 's'),
			new Card('7', 's')
		};
		
		instance.setCards(cards2);
		expResult = "5AQT87";
		assertEquals (expResult, instance.getCombination ());
	}
	
	/**
	 * Test recognition of full houses of getCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetCombinationFullHouse()
	{
		//threes full of Kings
		Card[] cards = {
			new Card('3', 'h'),
			new Card('J', 's'),
			new Card('3', 's'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('K', 's'),
			new Card('3', 'c'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		String expResult = "63K";
		assertEquals (expResult, instance.getCombination ());
		
		//there are 6 (!!) different full houses here:
		//jacks full of fives
		//jacks full of sevens
		//fives full of sevens
		//fives full of jacks
		//sevens full of jacks
		//sevens full of fives
		//the algorithm should pick Jacks full of sevens as the best one (6J7)
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('5', 's'),
			new Card('7', 's'),
			new Card('J', 's'),
			new Card('J', 'd'),
			new Card('7', 'h'),
			new Card('5', 'c'),
			new Card('5', 'd'),
			new Card('7', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "6J7";
		assertEquals (expResult, instance.getCombination ());
	}

	/**
	 * Test of getLoCombination method, of class OmahaCombination.
	 */
	@Test
	public void testGetLoCombination()
	{
		//Lo hand: 8642A
		Card[] cards = {
			new Card('J', 'h'),
			new Card('6', 's'),
			new Card('T', 's'),
			new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c')
		};
		
		OmahaCombination instance = new OmahaCombination (cards);
		
		String expResult = "8642A";
		assertEquals (expResult, instance.getLoCombination ());
		
		//no Lo hand
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c')
		};
		
		instance.setCards(cards2);
		
		expResult = "0";
		assertEquals (expResult, instance.getLoCombination ());
		
		//all low cards, best lo hand is the wheel
		Card[] cards3 = {
			new Card('8', 'h'),
			new Card('6', 's'),
			new Card('2', 's'),
			new Card('5', 'h'),
			new Card('A', 'd'),
			new Card('2', 'c'),
			new Card('3', 's'),
			new Card('4', 'd'),
			new Card('6', 'c')
		};
		
		instance.setCards(cards3);
		
		expResult = "5432A";
		assertEquals (expResult, instance.getLoCombination ());
	}

	/**
	 * Test of equals method, of class OmahaCombination.
	 */
	@Test
	public void testEquals()
	{
		System.out.println ("equals");
		Object oc = null;
		OmahaCombination instance = null;
		boolean expResult = false;
		boolean result = instance.equals (oc);
		assertEquals (expResult, result);
	}

	/**
	 * Test of toString method, of class OmahaCombination.
	 */
	@Test
	public void testToString()
	{
		System.out.println ("toString");
		OmahaCombination instance = null;
		String expResult = "";
		String result = instance.toString ();
		assertEquals (expResult, result);
	}
}