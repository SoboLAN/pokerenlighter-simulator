package org.javafling.pokerenlighter.simulation;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingWorker;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.combination.OmahaCombination;
import org.javafling.pokerenlighter.combination.TexasCombination;

/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public class SimulationWorker extends SwingWorker<Void, Integer>
{	
	//simulation data
	private PokerType gameType;
	private ArrayList<PlayerProfile> profiles;
	private int rounds;
	private Card[] communityCards;
	private int nrPlayers;
	
	//temporary results containment
	private int[] wins, loses, ties;
	private SimulationWorkerResult simResult;
	
	//other properties necessary for operations
	private int ID;
	private CountDownLatch latch;
	private int updateInterval;
	
	public SimulationWorker(int ID,
							PokerType gameType,
							ArrayList<PlayerProfile> profiles,
							Card[] communityCards,
							int rounds,
							CountDownLatch latch)
	{
		this.ID = ID;
		this.gameType = gameType;
		this.profiles = profiles;
		this.communityCards = communityCards;
		this.rounds = rounds;
		this.latch = latch;
		this.updateInterval = 100;
		this.nrPlayers = profiles.size ();
	}
	
	public int getID ()
	{
		return ID;
	}	
	
	/**
	 * Tells the Simulation engine how often to report progress. The progress will be reported by
	 * firing a property change event on all listening PropertyChangeListeners.
	 * <br />
	 * Note: Calling this method AFTER the SimulationWorker started execution will have no effect. If
	 * this method is not called at all, then the progress will be reported only once, when the
	 * SimulationWorker is finished.
	 * 
	 * @param updateInterval the update interval. Value must be a strictly pozitive integer divisible by 100.
	 * 
	 * @throws IllegalArgumentException if the parameter is an invalid value.
	 */
	public void setUpdateInterval (int updateInterval)
	{	
		if (updateInterval <= 0 || 100 % updateInterval != 0)
		{
			throw new IllegalArgumentException ("invalid update interval value");
		}
	
		this.updateInterval = updateInterval;
	}
	
	@Override
	protected Void doInBackground ()
	{
		wins = new int[nrPlayers];
		loses = new int[nrPlayers];
		ties = new int[nrPlayers];
		
		for (int i = 0; i < nrPlayers; i++)
		{
			wins[i] = loses[i] = ties[i] = 0;
		}
	
		if (gameType == PokerType.TEXAS_HOLDEM)
		{
			texasHoldemRun ();
		}
		else if (gameType == PokerType.OMAHA)
		{
			omahaRun ();
		}
		else if (gameType == PokerType.OMAHA_HILO)
		{
			omahaHiLoRun ();
		}
			
		return null;
	}
	
	private void texasHoldemRun ()
	{		
		Deck deck = new Deck ();
		
		removeUsedCards (deck);
		
		Card[][] playerCards = new Card[nrPlayers][7];
		
		for (int i = 0; i < nrPlayers; i++)
		{
			if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
			{
				Card[] excards = profiles.get (i).getCards ();
				
				playerCards[i][0] = excards[0];
				playerCards[i][1] = excards[1];
			}
		}
		
		TexasCombination[] playerCombinations = new TexasCombination[nrPlayers];
		String[] playerHands = new String[nrPlayers];
		Card[] currentHand = new Card[7];
		
		//main simulation loop
		for (int current_round = 1; current_round <= rounds && ! Thread.currentThread ().isInterrupted (); current_round++)
		{
			deck.shuffle (10);
			
			//this is to ensure that all players that selected a range hand will have cards
			//that fit into those ranges
			//WARNING: if range(s) is/are narrow, this will have significant performance impact (!!)
			boolean okRanges = true;
			do
			{
				boolean okRangesInside = true;
				for (int i = 0; i < profiles.size (); i++)
				{
					if (profiles.get (i).getHandType () == HandType.RANGE &&
						! profiles.get (i).getRange ().containsHand (deck.getCard (i), deck.getCard (i + nrPlayers)))
					{
						okRangesInside = false;
						break;
					}
				}
				
				if (! okRangesInside)
				{
					//minimum shuffle should be enough
					deck.shuffle (5);
				}
				
				okRanges = okRangesInside;
			} while (! okRanges);
			
			//determine what each player has
			for (int i = 0; i < nrPlayers; i++)
			{
				if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
				{
					currentHand[0] = playerCards[i][0];
					currentHand[1] = playerCards[i][1];
				}
				else
				{
					currentHand[0] = deck.getCard (i);
					currentHand[1] = deck.getCard (i + nrPlayers);
				}
				
				//flop
				currentHand[2] = communityCards[0] == null ? deck.getCard (1 + 2 * nrPlayers) : communityCards[0];
				currentHand[3] = communityCards[1] == null ? deck.getCard (2 + 2 * nrPlayers) : communityCards[1];
				currentHand[4] = communityCards[2] == null ? deck.getCard (3 + 2 * nrPlayers) : communityCards[2];
	
				//turn
				currentHand[5] = communityCards[3] == null ? deck.getCard (5 + 2 * nrPlayers) : communityCards[3];
				
				//river
				currentHand[6] = communityCards[4] == null ? deck.getCard (7 + 2 * nrPlayers) : communityCards[4];
				
				if (playerCombinations[i] == null)
				{
					playerCombinations[i] = new TexasCombination(currentHand);
				}
				else
				{
					playerCombinations[i].setCards (currentHand);
				}
				
				playerHands[i] = playerCombinations[i].getCombination ();
			}
			
			int[] winningPlayers = getWinners (playerHands);
			
			//multiple winners a.k.a. a tie
			if (winningPlayers.length > 1)
			{
				for (int i = 0; i < winningPlayers.length; i++)
				{
					ties[winningPlayers[i]]++;
				}
			}
			//only 1 winner
			else
			{
				wins[winningPlayers[0]]++;
			}
			
			for (int i = 0; i < nrPlayers; i++)
			{
				if (! contains (winningPlayers, i))
				{
					loses[i]++;
				}
			}
			
			if (((current_round * 100) / rounds) % updateInterval == 0)
			{
				setProgress ((current_round) * 100 / rounds);
			}
		}
	}
	
	//checks if the the element "nr" is found in the array "arr"
	private boolean contains (int[] arr, int nr)
	{
		for (int i = 0; i < arr.length; ++i)
		{
			if (arr[i] == nr)
			{
				return true;
			}
		}

		return false;
	}
	
	// Handles the situation where 2 (or more) players have the same rank in a hand (for
	// example: 3 players have three of a kind).
	//
	// The parameter hands array of strings containing the combinations to compare. The first character
	// should be the rank of the combination.
	//
	// Returns array of integers representing the IDs of the winning hands. For example: if the
	// parameter contains 6 hands and it is determined that the first and last hand are winners,
	// then the returning array will contain 0 and 5 (in this order) (0-based index).
	private int[] getWinners (String[] hands)
	{
		int j, k, compare_result, nr_winners = nrPlayers;
		boolean[] eliminated = new boolean[nrPlayers];
		int[] winners;

		for (j = 0; j < nrPlayers; ++j)
		{
			eliminated[j] = false;
		}

		for (j = 0; j < nrPlayers - 1; ++j)
		{
			for (k = j + 1; k < nrPlayers; ++k)
			{
				if (! eliminated[j] && ! eliminated[k])
				{
					compare_result = compareHands (hands[j], hands[k]);

					if (compare_result == 1)
					{
						eliminated[k] = true;
						nr_winners--;
					}
					else if (compare_result == 2)
					{
						eliminated[j] = true;
						nr_winners--;
					}
				}
			}
		}

		winners = new int[nr_winners];
		k = 0;

		for (j = 0; j < nrPlayers; ++j)
		{
			if (! eliminated[j])
			{
				winners[k++] = j;
			}
		}

		return winners;
	}
	
	// Compares two combinations.
	//
	// parameter hand1 = The first combination.
	// parameter hand2  = The second combination.
	//
	// returns 1 if the combination hand1 is better, 2 if hand2 is better, 0 if they are equal.
	private int compareHands (String hand1, String hand2)
	{
		int r1 = (int) (hand1.charAt (0)) - 48,
			r2 = (int) (hand2.charAt (0)) - 48;

		//compare ranks
		//if one is bigger than the other, it's done
		if (r1 > r2)
		{
			return 1;
		}
		else if (r2 > r1)
		{
			return 2;
		}

		//check for royal flushes
		if (r1 == 9)
		{
			return (r2 == 9) ? 0 : 1;
		}
		else if (r2 == 9)
		{
			return 2;
		}

		int hand_length = hand1.length () - 1;

		for (int i = 1; i <= hand_length; ++i)
		{
			if (Card.getRank (hand1.charAt (i)) > Card.getRank (hand2.charAt (i)))
			{
				return 1;
			}
			else if (Card.getRank (hand1.charAt (i)) < Card.getRank (hand2.charAt (i)))
			{
				return 2;
			}
		}

		return 0;
	}
	
	private int[] getWinnersLo (String[] hands)
	{
		int i;
		boolean lohands = false;

		for (i = 0; i < nrPlayers; ++i)
		{
			if (! hands[i].equals ("0"))
			{
				lohands = true;
				break;
			}
		}

		if (! lohands)
		{
			int[] winners = new int[1];
			winners[0] = -1;
			return winners;
		}

		boolean[] eliminated = new boolean[nrPlayers];

		int j, compare_result, nr_winners;

		for (i = 0; i < nrPlayers; ++i)
		{
			eliminated[i] = false;
		}

		nr_winners = nrPlayers;

		for (i = 0; i < nrPlayers - 1; ++i)
		{
			for (j = i + 1; j < nrPlayers; ++j)
			{
				if (! eliminated[i] && ! eliminated[j])
				{
					compare_result = compareHandsLo (hands[i], hands[j]);

					if (compare_result == 1)
					{
						eliminated[j] = true;
						--nr_winners;
					}
					else if (compare_result == 2)
					{
						eliminated[i] = true;
						--nr_winners;
					}
				}
			}
		}

		int[] winners = new int[nr_winners];
		j = 0;

		for (i = 0; i < nrPlayers; ++i)
		{
			if (! eliminated[i])
			{
				winners[j] = i;
				++j;
			}
		}

		return winners;
	}

	private int compareHandsLo (String hand1, String hand2)
	{
		if (hand1.equals ("0"))
		{			
			return hand2.equals ("0") ? 0 : 2;
		}
		else
		{
			if (hand2.equals ("0"))
			{
				return 1;
			}
		}

		for (int i = 0; i < 5; i++)
		{
			if (Card.getRank (hand1.charAt (i)) > Card.getRank (hand2.charAt (i)))
			{
				return 2;
			}
			else if (Card.getRank (hand2.charAt (i)) > Card.getRank (hand1.charAt (i)))
			{
				return 1;
			}
		}

		return 0;
	}
	
	private void omahaRun ()
	{
		Deck deck = new Deck ();
		
		removeUsedCards (deck);
		
		Card[][] playerCards = new Card[nrPlayers][4];
		
		for (int i = 0; i < nrPlayers; i++)
		{
			if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
			{
				Card[] excards = profiles.get (i).getCards ();
				
				playerCards[i][0] = excards[0];
				playerCards[i][1] = excards[1];
				playerCards[i][2] = excards[2];
				playerCards[i][3] = excards[3];
			}
		}
		
		OmahaCombination[] playerCombinations = new OmahaCombination[nrPlayers];
		String[] playerHands = new String[nrPlayers];
		Card[] currentHand = new Card[9];

		//main simulation loop
		for (int current_round = 1; current_round <= rounds && ! Thread.currentThread ().isInterrupted (); current_round++)
		{
			deck.shuffle (10);
									
			//determine what each player has
			for (int i = 0; i < nrPlayers; i++)
			{
				if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
				{
					currentHand[0] = playerCards[i][0];
					currentHand[1] = playerCards[i][1];
					currentHand[2] = playerCards[i][2];
					currentHand[3] = playerCards[i][3];
				}
				else
				{
					currentHand[0] = deck.getCard (i);
					currentHand[1] = deck.getCard (i + nrPlayers);
					currentHand[2] = deck.getCard (i + 2 * nrPlayers);
					currentHand[3] = deck.getCard (i + 3 * nrPlayers);
				}
				
				//flop
				currentHand[4] = communityCards[0] == null ? deck.getCard (1 + 4 * nrPlayers) : communityCards[0];
				currentHand[5] = communityCards[1] == null ? deck.getCard (2 + 4 * nrPlayers) : communityCards[1];
				currentHand[6] = communityCards[2] == null ? deck.getCard (3 + 4 * nrPlayers) : communityCards[2];
	
				//turn
				currentHand[7] = communityCards[3] == null ? deck.getCard (5 + 4 * nrPlayers) : communityCards[3];
				
				//river
				currentHand[8] = communityCards[4] == null ? deck.getCard (7 + 4 * nrPlayers) : communityCards[4];
				
				if (playerCombinations[i] == null)
				{
					playerCombinations[i] = new OmahaCombination(currentHand);
				}
				else
				{
					playerCombinations[i].setCards (currentHand);
				}
				
				playerHands[i] = playerCombinations[i].getCombination ();
			}
			
			int[] winningPlayers = getWinners (playerHands);
			
			//multiple winners a.k.a. a tie
			if (winningPlayers.length > 1)
			{
				for (int i = 0; i < winningPlayers.length; i++)
				{
					ties[winningPlayers[i]]++;
				}
			}
			//only 1 winner
			else
			{
				wins[winningPlayers[0]]++;
			}
			
			for (int i = 0; i < nrPlayers; i++)
			{
				if (! contains (winningPlayers, i))
				{
					loses[i]++;
				}
			}
			
			if (((current_round * 100) / rounds) % updateInterval == 0)
			{
				setProgress ((current_round) * 100 / rounds);
			}
		}
	}
	
	private void omahaHiLoRun ()
	{
		Deck deck = new Deck ();
		
		removeUsedCards (deck);
		
		Card[][] playerCards = new Card[nrPlayers][4];
		
		for (int i = 0; i < nrPlayers; i++)
		{
			if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
			{
				Card[] excards = profiles.get (i).getCards ();
				
				playerCards[i][0] = excards[0];
				playerCards[i][1] = excards[1];
				playerCards[i][2] = excards[2];
				playerCards[i][3] = excards[3];
			}
		}
		
		OmahaCombination[] playerCombinations = new OmahaCombination[nrPlayers];
		String[] playerHands = new String[nrPlayers];
		String[] playerHandsLo = new String[nrPlayers];
		Card[] currentHand = new Card[9];
		
		boolean[] tmpWins = new boolean[nrPlayers];
		boolean[] tmpTies = new boolean[nrPlayers];

		//main simulation loop
		for (int current_round = 1; current_round <= rounds && ! Thread.currentThread ().isInterrupted (); current_round++)
		{
			deck.shuffle (10);

			//determine what each player has
			for (int i = 0; i < nrPlayers; i++)
			{
				if (profiles.get (i).getHandType () == HandType.EXACTCARDS)
				{
					currentHand[0] = playerCards[i][0];
					currentHand[1] = playerCards[i][1];
					currentHand[2] = playerCards[i][2];
					currentHand[3] = playerCards[i][3];
				}
				else
				{
					currentHand[0] = deck.getCard (i);
					currentHand[1] = deck.getCard (i + nrPlayers);
					currentHand[2] = deck.getCard (i + 2 * nrPlayers);
					currentHand[3] = deck.getCard (i + 3 * nrPlayers);
				}
				
				//flop
				currentHand[4] = communityCards[0] == null ? deck.getCard (1 + 4 * nrPlayers) : communityCards[0];
				currentHand[5] = communityCards[1] == null ? deck.getCard (2 + 4 * nrPlayers) : communityCards[1];
				currentHand[6] = communityCards[2] == null ? deck.getCard (3 + 4 * nrPlayers) : communityCards[2];
	
				//turn
				currentHand[7] = communityCards[3] == null ? deck.getCard (5 + 4 * nrPlayers) : communityCards[3];
				
				//river
				currentHand[8] = communityCards[4] == null ? deck.getCard (7 + 4 * nrPlayers) : communityCards[4];
				
				if (playerCombinations[i] == null)
				{
					playerCombinations[i] = new OmahaCombination(currentHand);
				}
				else
				{
					playerCombinations[i].setCards (currentHand);
				}
				
				playerHands[i] = playerCombinations[i].getCombination ();
				playerHandsLo[i] = playerCombinations[i].getLoCombination ();
			}
			
			//TO DO:
			//from this point downwards, implement the recording mechanism (CAREFULLY!)
			
			int[] winningPlayers = getWinners (playerHands);
			int[] winningPlayersLo = getWinnersLo (playerHandsLo);
			
			//let's assume no one wins... at first
			for (int i = 0; i < nrPlayers; i++)
			{
				tmpWins[i] = tmpTies[i] = false;
			}
			
			//determine wins & ties for the hi part
			if (winningPlayers.length > 1)
			{
				for (int i = 0; i < winningPlayers.length; i++)
				{
					tmpTies[i] = true;
				}
			}
			else
			{
				tmpWins[winningPlayers[0]] = true;
			}
			
			//determine wins & ties for the lo part
			if (winningPlayersLo.length > 1)
			{
				for (int i = 0; i < winningPlayersLo.length; i++)
				{
					tmpTies[i] = true;
				}
			}
			else if (winningPlayersLo[0] != -1)
			{
				tmpWins[winningPlayersLo[0]] = true;
			}
			
			//now fill in the records
			for (int i = 0; i < nrPlayers; i++)
			{
				if (tmpWins[i])
				{
					wins[i]++;
				}
				else if (tmpTies[i])
				{
					ties[i]++;
				}
				else
				{
					loses[i]++;
				}
			}
						
			if (((current_round * 100) / rounds) % updateInterval == 0)
			{
				setProgress ((current_round) * 100 / rounds);
			}
		}
	}
	
	private void removeUsedCards (Deck deck)
	{
		for (PlayerProfile profile : profiles)
		{
			if (profile.getHandType () == HandType.EXACTCARDS)
			{
				Card[] pcards = profile.getCards ();
				
				for (Card pcard : pcards)
				{
					deck.removeCard (pcard);
				}
			}
		}
		for (int i = 0; i < communityCards.length; i++)
		{
			if (communityCards[i] != null)
			{
				deck.removeCard (communityCards[i]);
			}
		}
	}
	
	public SimulationWorkerResult getResult ()
	{
		return simResult;
	}
	
	@Override
	protected void done()
	{
		//build result here
		
		if (getProgress () == 100)
		{
			double[] winP = new double[nrPlayers];
			double[] losesP = new double[nrPlayers];
			double[] tiesP = new double [nrPlayers];

			for (int i = 0; i < nrPlayers; i++)
			{
				winP[i] = (100.0 * wins[i]) / rounds;
				losesP[i] = (100.0 * loses[i]) / rounds;
				tiesP[i] = (100.0 * ties[i]) / rounds;
			}

			simResult = new SimulationWorkerResult (winP, tiesP, losesP);

		}
		
		latch.countDown();
	}
}
