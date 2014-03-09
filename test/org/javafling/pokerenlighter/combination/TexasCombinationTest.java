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
public class TexasCombinationTest
{
	/**
	 * Test of setCards method, of class TexasCombination.
	 */
	@Test
	public void testSetCards()
	{
		Card[] constructorCards = {
			new Card('5', 'c'),
			new Card('2', 'd'),
			new Card('7', 'h'),
			new Card('K', 's'),
			new Card('T', 'c'),
			new Card('4', 'd'),
			new Card('A', 'h')
		};
		TexasCombination instance = new TexasCombination (constructorCards);
		
		Card[] setterCards = {
			new Card('6', 'c'),
			new Card('4', 'd'),
			new Card('9', 'h'),
			new Card('T', 's'),
			new Card('Q', 'c'),
			new Card('3', 'd'),
			new Card('K', 'h')
		};
		
		instance.setCards (setterCards.clone ());

		assertEquals(setterCards[6], instance.getCard (0));
		assertEquals(setterCards[4], instance.getCard (1));
		assertEquals(setterCards[3], instance.getCard (2));
		assertEquals(setterCards[2], instance.getCard (3));
		assertEquals(setterCards[0], instance.getCard (4));
		assertEquals(setterCards[1], instance.getCard (5));
		assertEquals(setterCards[5], instance.getCard (6));
	}

	/**
	 * Test of getCard method, of class TexasCombination.
	 */
	@Test
	public void testGetCard()
	{
		Card[] constructorCards = {
			new Card('A', 'c'),
			new Card('K', 'd'),
			new Card('T', 'h'),
			new Card('7', 's'),
			new Card('4', 'c'),
			new Card('3', 'd'),
			new Card('2', 'h')
		};
		TexasCombination instance = new TexasCombination (constructorCards);

		assertEquals(constructorCards[0], instance.getCard (0));
		assertEquals(constructorCards[1], instance.getCard (1));
		assertEquals(constructorCards[2], instance.getCard (2));
		assertEquals(constructorCards[3], instance.getCard (3));
		assertEquals(constructorCards[4], instance.getCard (4));
		assertEquals(constructorCards[5], instance.getCard (5));
		assertEquals(constructorCards[6], instance.getCard (6));
	}

	/**
	 * Test of getHighCard method, of class TexasCombination.
	 */
	@Test
	public void testGetHighCard()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('3', 'h'),
			new Card('A', 's'),
			new Card('K', 'c'),
			new Card('T', 'd'),
			new Card('2', 'h')
		};
		TexasCombination instance = new TexasCombination (constructorCards.clone());
		
		String highCards = instance.getHighCard ();
		
		assertEquals(constructorCards[3].getCharCard (), highCards.charAt (0));
		assertEquals(constructorCards[4].getCharCard (), highCards.charAt (1));
		assertEquals(constructorCards[5].getCharCard (), highCards.charAt (2));
		assertEquals(constructorCards[0].getCharCard (), highCards.charAt (3));
		assertEquals(constructorCards[1].getCharCard (), highCards.charAt (4));
	}
	
	/**
	 * Test of getHighCard method, of class TexasCombination.
	 */
	@Test
	public void testGetHighCard2()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('K', 'h'),
			new Card('A', 's'),
			new Card('K', 'c'),
			new Card('T', 'd'),
			new Card('T', 'h')
		};
		TexasCombination instance = new TexasCombination (constructorCards.clone());
		
		String highCards = instance.getHighCard ();
		
		assertEquals(constructorCards[3].getCharCard (), highCards.charAt (0));
		assertEquals(constructorCards[2].getCharCard (), highCards.charAt (1));
		assertEquals(constructorCards[4].getCharCard (), highCards.charAt (2));
		assertEquals(constructorCards[5].getCharCard (), highCards.charAt (3));
		assertEquals(constructorCards[6].getCharCard (), highCards.charAt (4));
	}

	/**
	 * Test of getOnePair method, of class TexasCombination.
	 */
	@Test
	public void testGetOnePair()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('K', 'h'),
			new Card('A', 's'),
			new Card('J', 'c'),
			new Card('T', 'd'),
			new Card('T', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "TAKJ";
		String result = instance.getOnePair ();
		
		assertEquals (expResult, result);
	}
	
	/**
	 * Test 2 of getOnePair method, of class TexasCombination.
	 */
	@Test
	public void testGetOnePair2()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('K', 'h'),
			new Card('T', 's'),
			new Card('T', 'c'),
			new Card('T', 'd'),
			new Card('T', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "TKTT";
		String result = instance.getOnePair ();
		
		assertEquals (expResult, result);
	}
	
	/**
	 * Test 3 of getOnePair method, of class TexasCombination.
	 */
	@Test
	public void testGetOnePair3()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('K', 'h'),
			new Card('T', 's'),
			new Card('5', 'c'),
			new Card('3', 'd'),
			new Card('A', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "0";
		String result = instance.getOnePair ();
		
		assertEquals (expResult, result);
	}

	/**
	 * Test of getTwoPair method, of class TexasCombination.
	 */
	@Test
	public void testGetTwoPair()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('Q', 'h'),
			new Card('7', 's'),
			new Card('T', 'c'),
			new Card('J', 'd'),
			new Card('T', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "T7Q";
		String result = instance.getTwoPair ();
		
		assertEquals (expResult, result);
	}
	
	/**
	 * Test 2 of getTwoPair method, of class TexasCombination.
	 */
	@Test
	public void testGetTwoPair2()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('7', 'h'),
			new Card('7', 's'),
			new Card('T', 'c'),
			new Card('J', 'd'),
			new Card('T', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "T7J";
		String result = instance.getTwoPair ();
		
		assertEquals (expResult, result);
	}
	
	/**
	 * Test 3 of getTwoPair method, of class TexasCombination.
	 */
	@Test
	public void testGetTwoPair3()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('6', 'h'),
			new Card('6', 's'),
			new Card('6', 'c'),
			new Card('J', 'd'),
			new Card('K', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "0";
		String result = instance.getTwoPair ();
		
		assertEquals (expResult, result);
	}

	/**
	 * Test of getThreeOfAKind method, of class TexasCombination.
	 */
	@Test
	public void testGetThreeOfAKind()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('4', 'd'),
			new Card('6', 'h'),
			new Card('6', 's'),
			new Card('6', 'c'),
			new Card('J', 'd'),
			new Card('K', 'h')
		};
		
		TexasCombination instance = new TexasCombination (constructorCards);
		String expResult = "6KJ";
		String result = instance.getThreeOfAKind();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getThreeOfAKind method, of class TexasCombination.
	 */
	@Test
	public void testGetThreeOfAKind2()
	{
		Card[] constructorCards = {
			new Card('2', 'c'),
			new Card('4', 'd'),
			new Card('6', 'h'),
			new Card('6', 's'),
			new Card('6', 'c'),
			new Card('6', 'd'),
			new Card('K', 'h')
		};

		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "6K6";
		String result = instance.getThreeOfAKind();

		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getThreeOfAKind method, of class TexasCombination.
	 */
	@Test
	public void testGetThreeOfAKind3()
	{
		Card[] constructorCards = {
			new Card('2', 'c'),
			new Card('4', 'd'),
			new Card('6', 'h'),
			new Card('9', 's'),
			new Card('6', 'c'),
			new Card('T', 'd'),
			new Card('K', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getThreeOfAKind();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of getStraight method, of class TexasCombination.
	 */
	@Test
	public void testGetStraight()
	{
		Card[] constructorCards = {
			new Card('5', 'c'),
			new Card('J', 'd'),
			new Card('6', 'h'),
			new Card('9', 's'),
			new Card('7', 'c'),
			new Card('A', 'd'),
			new Card('8', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "9";
		String result = instance.getStraight();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getStraight method, of class TexasCombination.
	 */
	@Test
	public void testGetStraight2()
	{
		Card[] constructorCards = {
			new Card('5', 'c'),
			new Card('J', 'd'),
			new Card('6', 'h'),
			new Card('9', 's'),
			new Card('7', 'c'),
			new Card('T', 'd'),
			new Card('8', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "J";
		String result = instance.getStraight();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getStraight method, of class TexasCombination.
	 */
	@Test
	public void testGetStraight3()
	{
		Card[] constructorCards = {
			new Card('5', 'c'),
			new Card('J', 'd'),
			new Card('6', 'h'),
			new Card('9', 's'),
			new Card('T', 'c'),
			new Card('T', 'd'),
			new Card('8', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getStraight();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 4 of getStraight method, of class TexasCombination.
	 */
	@Test
	public void testGetStraight4()
	{
		Card[] constructorCards = {
			new Card('5', 'c'),
			new Card('J', 'd'),
			new Card('3', 'h'),
			new Card('9', 's'),
			new Card('2', 'c'),
			new Card('4', 'd'),
			new Card('A', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "5";
		String result = instance.getStraight();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of getFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetFlush()
	{
		Card[] constructorCards = {
			new Card('4', 'c'),
			new Card('2', 'd'),
			new Card('Q', 'c'),
			new Card('A', 's'),
			new Card('K', 'c'),
			new Card('5', 'c'),
			new Card('6', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "KQ654";
		String result = instance.getFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetFlush2()
	{
		Card[] constructorCards = {
			new Card('4', 'c'),
			new Card('3', 'c'),
			new Card('Q', 'h'),
			new Card('7', 'c'),
			new Card('K', 's'),
			new Card('5', 'c'),
			new Card('6', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "76543";
		String result = instance.getFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetFlush3()
	{
		Card[] constructorCards = {
			new Card('4', 'c'),
			new Card('T', 'c'),
			new Card('Q', 'h'),
			new Card('7', 'd'),
			new Card('K', 's'),
			new Card('5', 'c'),
			new Card('6', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getFlush();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of getFullHouse method, of class TexasCombination.
	 */
	@Test
	public void testGetFullHouse()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('2', 's'),
			new Card('Q', 'h'),
			new Card('2', 'd'),
			new Card('K', 's'),
			new Card('7', 'h'),
			new Card('2', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "27";
		String result = instance.getFullHouse();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getFullHouse method, of class TexasCombination.
	 */
	@Test
	public void testGetFullHouse2()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('2', 's'),
			new Card('Q', 'h'),
			new Card('2', 'd'),
			new Card('Q', 's'),
			new Card('7', 'h'),
			new Card('2', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "2Q";
		String result = instance.getFullHouse();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getFullHouse method, of class TexasCombination.
	 */
	@Test
	public void testGetFullHouse3()
	{
		Card[] constructorCards = {
			new Card('7', 'c'),
			new Card('2', 's'),
			new Card('7', 'h'),
			new Card('2', 'd'),
			new Card('7', 'd'),
			new Card('7', 's'),
			new Card('2', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "72";
		String result = instance.getFullHouse();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 4 of getFullHouse method, of class TexasCombination.
	 */
	@Test
	public void testGetFullHouse4()
	{
		Card[] constructorCards = {
			new Card('2', 'c'),
			new Card('A', 's'),
			new Card('7', 'h'),
			new Card('T', 'd'),
			new Card('7', 'd'),
			new Card('J', 's'),
			new Card('2', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getFullHouse();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of getQuad method, of class TexasCombination.
	 */
	@Test
	public void testGetQuad()
	{
		Card[] constructorCards = {
			new Card('9', 'c'),
			new Card('K', 's'),
			new Card('9', 'h'),
			new Card('T', 'd'),
			new Card('9', 'd'),
			new Card('9', 's'),
			new Card('2', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "9K";
		String result = instance.getQuad();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getQuad method, of class TexasCombination.
	 */
	@Test
	public void testGetQuad2()
	{
		Card[] constructorCards = {
			new Card('9', 'c'),
			new Card('K', 's'),
			new Card('9', 'h'),
			new Card('T', 'd'),
			new Card('9', 'd'),
			new Card('5', 's'),
			new Card('5', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getQuad();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of getStraightFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetStraightFlush()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 's'),
			new Card('9', 'c'),
			new Card('J', 'c'),
			new Card('8', 'c'),
			new Card('2', 's'),
			new Card('Q', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "Q";
		String result = instance.getStraightFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getStraightFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetStraightFlush2()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 's'),
			new Card('9', 'c'),
			new Card('J', 'h'),
			new Card('8', 'c'),
			new Card('2', 's'),
			new Card('Q', 'c')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0";
		String result = instance.getStraightFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getStraightFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetStraightFlush3()
	{
		Card[] constructorCards = {
			new Card('T', 'h'),
			new Card('6', 's'),
			new Card('K', 'h'),
			new Card('J', 'h'),
			new Card('A', 'h'),
			new Card('2', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "A";
		String result = instance.getStraightFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 4 of getStraightFlush method, of class TexasCombination.
	 */
	@Test
	public void testGetStraightFlush4()
	{
		Card[] constructorCards = {
			new Card('3', 's'),
			new Card('8', 'd'),
			new Card('4', 's'),
			new Card('J', 's'),
			new Card('A', 's'),
			new Card('2', 's'),
			new Card('5', 's')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "5";
		String result = instance.getStraightFlush();
		
		assertEquals(expResult, result);
	}

	/**
	 * Test of isRoyalFlush method, of class TexasCombination.
	 */
	@Test
	public void testIsRoyalFlush()
	{
		Card[] constructorCards = {
			new Card('T', 'h'),
			new Card('6', 's'),
			new Card('K', 'h'),
			new Card('J', 'h'),
			new Card('A', 'h'),
			new Card('2', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		boolean expResult = true;
		boolean result = instance.isRoyalFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of isRoyalFlush method, of class TexasCombination.
	 */
	@Test
	public void testIsRoyalFlush2()
	{
		Card[] constructorCards = {
			new Card('T', 'h'),
			new Card('6', 's'),
			new Card('K', 'h'),
			new Card('J', 's'),
			new Card('A', 'h'),
			new Card('2', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		boolean expResult = false;
		boolean result = instance.isRoyalFlush();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 's'),
			new Card('3', 'd'),
			new Card('J', 's'),
			new Card('A', 'd'),
			new Card('2', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "0AQJT6";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 2 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination2()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 's'),
			new Card('3', 'd'),
			new Card('J', 's'),
			new Card('A', 'd'),
			new Card('3', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "13AQJ";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 3 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination3()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 's'),
			new Card('3', 'd'),
			new Card('J', 's'),
			new Card('T', 'd'),
			new Card('2', 's'),
			new Card('6', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "2T6J";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 4 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination4()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('7', 'd'),
			new Card('3', 'd'),
			new Card('7', 's'),
			new Card('A', 'd'),
			new Card('7', 'c'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "37AQ";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 5 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination5()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('Q', 's'),
			new Card('3', 'd'),
			new Card('J', 's'),
			new Card('5', 'd'),
			new Card('9', 's'),
			new Card('K', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "4K";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 6 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination6()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('6', 'h'),
			new Card('3', 'd'),
			new Card('J', 'h'),
			new Card('A', 'h'),
			new Card('2', 'h'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "5AQJ62";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 7 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination7()
	{
		Card[] constructorCards = {
			new Card('4', 'c'),
			new Card('6', 's'),
			new Card('4', 'd'),
			new Card('4', 's'),
			new Card('Q', 'd'),
			new Card('2', 's'),
			new Card('Q', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "64Q";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test 8 of getCombination method, of class TexasCombination.
	 */
	@Test
	public void testGetCombination8()
	{
		Card[] constructorCards = {
			new Card('T', 'c'),
			new Card('7', 's'),
			new Card('3', 'd'),
			new Card('7', 'd'),
			new Card('A', 'd'),
			new Card('7', 'c'),
			new Card('7', 'h')
		};
		
		TexasCombination instance = new TexasCombination(constructorCards);
		String expResult = "77A";
		String result = instance.getCombination();
		
		assertEquals(expResult, result);
	}
}