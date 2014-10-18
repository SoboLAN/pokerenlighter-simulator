package org.javafling.pokerenlighter.combination;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Radu Murzea
 */
public class FiveCardOmahaCombinationTest
{
	/**
	 * Test of setCards method, of class FiveCardOmahaCombination.
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
			new Card('K', 'c'),
            new Card('3', 'h')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(initialCards);
		
		Card[] newCards = {
			new Card('A', 'h'),
			new Card('K', 's'),
			new Card('9', 'd'),
			new Card('2', 'h'),
			new Card('5', 'd'),
			new Card('J', 'c'),
			new Card('T', 's'),
			new Card('7', 'd'),
			new Card('7', 'c'),
            new Card('J', 'd')
		};
		
		instance.setCards(newCards);
		
		assertEquals(newCards[0], instance.getCard(0));
		assertEquals(newCards[1], instance.getCard(1));
		assertEquals(newCards[2], instance.getCard(2));
		assertEquals(newCards[3], instance.getCard(3));
		assertEquals(newCards[4], instance.getCard(4));
		assertEquals(newCards[5], instance.getCard(5));
		assertEquals(newCards[6], instance.getCard(6));
		assertEquals(newCards[7], instance.getCard(7));
		assertEquals(newCards[8], instance.getCard(8));
        assertEquals(newCards[9], instance.getCard(9));
	}

	/**
	 * Test of getCard method, of class FiveCardOmahaCombination.
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
            new Card('K', 's'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		
		assertEquals(cards[0], instance.getCard(0));
		assertEquals(cards[1], instance.getCard(1));
		assertEquals(cards[2], instance.getCard(2));
		assertEquals(cards[3], instance.getCard(3));
		assertEquals(cards[4], instance.getCard(4));
		assertEquals(cards[5], instance.getCard(5));
		assertEquals(cards[6], instance.getCard(6));
		assertEquals(cards[7], instance.getCard(7));
		assertEquals(cards[8], instance.getCard(8));
        assertEquals(cards[9], instance.getCard(9));
	}
	
	/**
	 * Test recognition of high card of getCombination method, of class FiveCardOmahaCombination.
	 */
	@Test
	public void testGetCombinationHighCard()
	{
		//high card: AKJ86
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
			new Card('A', 'h'),
			new Card('4', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
            new Card('2', 'h'),
			new Card('6', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		String expResult = "0AKJ86";
		assertEquals(expResult, instance.getCombination());
	}

	/**
	 * Test recognition of pairs of getCombination method, of class FiveCardOmahaCombination.
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
            new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('3', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		String expResult = "1TAK8";
		assertEquals(expResult, instance.getCombination());
		
		//no pair
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('7', 's'),
			new Card('A', 'h'),
            new Card('5', 'c'),
			new Card('8', 's'),
            new Card('3', 'h'),
            new Card('T', 'd'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "0AKJT8";
		assertEquals(expResult, instance.getCombination());
	}
	
	/**
	 * Test recognition of 2 pair of getCombination method, of class FiveCardOmahaCombination.
	 */
	@Test
	public void testGetCombinationTwoPair()
	{
		//a pair of Tens and a pair of nines: 2T9K
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
            new Card('3', 'd'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('9', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination (cards);
		String expResult = "2T9K";
		assertEquals(expResult, instance.getCombination());
		
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
            new Card('4', 's'),
			new Card('A', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "75A";
		assertEquals(expResult, instance.getCombination());
	}
	
	/**
	 * Test recognition of 2 pair of getCombination method, of class FiveCardOmahaCombination.
	 */
	@Test
	public void testGetCombinationSet()
	{
		//a set of Eights: 38AK
		Card[] cards = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('8', 'h'),
            new Card('7', 'c'),
			new Card('A', 'h'),
			new Card('8', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		String expResult = "38AK";
		assertEquals(expResult, instance.getCombination());
		
		//2 pair should not be detected as a set
		Card[] cards2 = {
			new Card('K', 'h'),
			new Card('5', 's'),
			new Card('7', 's'),
            new Card('2', 'h'),
			new Card('A', 'h'),
			new Card('T', 'd'),
			new Card('5', 'c'),
			new Card('8', 's'),
			new Card('K', 'd'),
			new Card('7', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "2K7T";
		assertEquals(expResult, instance.getCombination());
	}
	
	/**
	 * Test recognition of straights of getCombination method, of class FiveCardOmahaCombination.
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
            new Card('6', 'c'),
			new Card('T', 'd'),
			new Card('7', 'c'),
			new Card('8', 's'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		String expResult = "4J";
		assertEquals(expResult, instance.getCombination());
	}
	
	/**
	 * Test recognition of flushes of getCombination method, of class FiveCardOmahaCombination.
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
            new Card('9', 'h'),
			new Card('4', 's'),
			new Card('A', 's'),
			new Card('8', 'h'),
			new Card('J', 's')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination (cards);
		String expResult = "5AQJ64";
		assertEquals(expResult, instance.getCombination());
		
		//a little trickier: all spades, so multiple flushes. highest is AKQ87
		Card[] cards2 = {
			new Card('T', 's'),
			new Card('6', 's'),
			new Card('Q', 's'),
			new Card('5', 's'),
            new Card('K', 's'),
			new Card('2', 's'),
			new Card('3', 's'),
			new Card('A', 's'),
			new Card('8', 's'),
			new Card('7', 's')
		};
		
		instance.setCards(cards2);
		expResult = "5AKQ87";
		assertEquals(expResult, instance.getCombination());
	}
	
	/**
	 * Test recognition of full houses of getCombination method, of class FiveCardOmahaCombination.
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
            new Card('7', 'c'),
			new Card('T', 'd'),
			new Card('K', 's'),
			new Card('3', 'c'),
			new Card('2', 'd'),
			new Card('K', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		String expResult = "63K";
		assertEquals(expResult, instance.getCombination());
		
		//there are a lot (!!) different full houses here:
		//jacks full of fives
		//jacks full of sevens
		//fives full of sevens
		//fives full of jacks
		//sevens full of jacks
		//sevens full of fives
        //a big part of them are in multiple combinations that result in the same rank
		//the algorithm should pick Jacks full of sevens as the best one (6J7)
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('5', 's'),
			new Card('7', 's'),
			new Card('J', 's'),
            new Card('J', 'c'),
			new Card('J', 'd'),
			new Card('7', 'h'),
			new Card('5', 'c'),
			new Card('5', 'd'),
			new Card('7', 'c')
		};
		
		instance.setCards(cards2);
		expResult = "6J7";
		assertEquals(expResult, instance.getCombination());
	}

	/**
	 * Test of getLoCombination method, of class FiveCardOmahaCombination.
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
            new Card('9', 'c'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c')
		};
		
		FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		
		String expResult = "8642A";
		assertEquals(expResult, instance.getLoCombination());
		
		//no Lo hand
		Card[] cards2 = {
			new Card('J', 'h'),
			new Card('9', 's'),
			new Card('T', 's'),
            new Card('K', 'c'),
			new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c')
		};
		
		instance.setCards(cards2);
		
		expResult = "0";
		assertEquals(expResult, instance.getLoCombination());
		
		//all low cards, best lo hand is the wheel
		Card[] cards3 = {
			new Card('8', 'h'),
			new Card('6', 's'),
			new Card('2', 's'),
			new Card('5', 'h'),
            new Card('3', 'c'),
			new Card('A', 'd'),
			new Card('2', 'c'),
			new Card('3', 's'),
			new Card('4', 'd'),
			new Card('6', 'c')
		};
		
		instance.setCards(cards3);
		
		expResult = "5432A";
		assertEquals(expResult, instance.getLoCombination());
	}

	/**
	 * Test of equals method, of class FiveCardOmahaCombination.
	 */
	@Test
	public void testEquals()
	{
		Card[] cards = {
			new Card('J', 'h'),
			new Card('6', 's'),
			new Card('T', 's'),
            new Card('9', 'c'),
			new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c')
		};
        
        FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
        
        //same cards but in different order
        Card[] cards2 = {
            new Card('9', 'c'),
			new Card('T', 's'),
            new Card('2', 'h'),
			new Card('6', 's'),
			new Card('J', 'h'),
			new Card('A', 'c'),
            new Card('4', 'd'),
			new Card('T', 'd'),
			new Card('8', 's'),
			new Card('Q', 'c'),
		};
        
        FiveCardOmahaCombination instance2 = new FiveCardOmahaCombination(cards2);
        
		boolean result = instance.equals(instance2);
		assertEquals(true, result);
	}
    
    /**
	 * Test of equals method, of class FiveCardOmahaCombination. False situation
	 */
	@Test
	public void testNotEquals()
	{
		Card[] cards = {
			new Card('J', 'h'),
			new Card('6', 's'),
			new Card('2', 'h'),
            new Card('9', 'c'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
			new Card('4', 'd'),
			new Card('A', 'c'),
            new Card('T', 's'),
		};
        
        FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
        
        //same cards but the order was changed so much that equality must be non-existent
        Card[] cards2 = {
			new Card('T', 's'),
            new Card('2', 'h'),
            new Card('4', 'd'),
            new Card('A', 'c'),
            new Card('8', 's'),
			new Card('J', 'h'),
			new Card('T', 'd'),
            new Card('6', 's'),
            new Card('9', 'c'),
			new Card('Q', 'c'),
		};
        
        FiveCardOmahaCombination instance2 = new FiveCardOmahaCombination(cards2);
        
		boolean result = instance.equals(instance2);
		assertEquals (false, result);
	}

	/**
	 * Test of toString method, of class FiveCardOmahaCombination.
	 */
	@Test
	public void testToString()
	{
        Card[] cards = {
			new Card('J', 'h'),
			new Card('6', 's'),
			new Card('2', 'h'),
			new Card('T', 'd'),
			new Card('Q', 'c'),
			new Card('8', 's'),
            new Card('5', 'c'),
			new Card('4', 'd'),
			new Card('A', 'c'),
            new Card('T', 's'),
		};
        
        FiveCardOmahaCombination instance = new FiveCardOmahaCombination(cards);
		
		String expResult = "Jh6s2hTdQc8s5c4dAcTs";
		String result = instance.toString();
		assertEquals(expResult, result);
	}
}