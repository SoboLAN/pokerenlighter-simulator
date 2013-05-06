package org.javafling.pokerenlighter.simulation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.main.SystemUtils;

/**
 * The control center of the simulations. This class manages the progress of all the simulations and
 * centralizes them. It will split the simulations on multiple threads, so you can safely create a
 * <code>Simulator</code> and/or start it on any thread you want, even the GUI thread.
 * <br />
 * The progress of the simulation can be monitored by registering one or more
 * <code>PropertyChangeListener</code>s to it.
 * <br />Please note that the implementation of the Simulator
 * will only allow firing a property change event if the difference between the new progress value
 * and the old one is at least 10. This means that, if the simulation will progress from 18 % to 23 %,
 * it will not be reported; but if it progresses to 29 %, then it will be. This is to prevent a very
 * high frequency of updates.
 * <br />
 * A typical use of the simulator is this:
 * <br />
 * <pre>
 * Simulator simulator = new Simulator (PokerType.OMAHA, 100 * 1000);
 * simulator.setUpdateInterval (10);
 * 
 * Card[] cards = {new Card ('A', 'h'), new Card ('K', 'h'), new Card ('6', 's'), new Card ('3', 'd')};
 * PlayerProfile player = new PlayerProfile (HandType.EXACTCARDS, null, cards);
 * 
 * simulator.addPlayer (player);
 * simulator.addPlayer (new PlayerProfile (HandType.RANDOM, null, null));
 * simulator.addPlayer (new PlayerProfile (HandType.RANDOM, null, null));
 * 
 * simulator.addPropertyChangeListener (this);
 * 
 * simulator.start ();
 * 
 * public void propertyChange(PropertyChangeEvent event)
 * {
 *	if ((Integer) event.getNewValue () == 100)
 *	{
 *		SimulationResult result = simulator.getResult ();
 *
 *		//do something with the result
 *	}
 * }
 * </pre>
 * 
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public final class Simulator implements PropertyChangeListener
{
	//simulation data
	private PokerType gameType;
	private ArrayList<PlayerProfile> profiles;
	private int nrRounds;
	private Card[] communityCards;
	
	//workers
	private ArrayList<SimulationWorker> workers;
	
	//additional stuff needed for correct implementation
	private ExecutorService executor;
	private CountDownLatch latch;
	private PropertyChangeSupport pcs;
	private long startTime, endTime;
	private int updateInterval, totalProgress;
	private int nrOfWorkers;
	
	private SimulationFinalResult simulationResult;

	public Simulator (PokerType type, int rounds)
	{
		if (type == null || rounds <= 0)
		{
			throw new IllegalArgumentException ("invalid arguments");
		}
		
		gameType = type;
		nrRounds = rounds;
		profiles = new ArrayList<> ();
		communityCards = new Card[5];
		workers = new ArrayList<> ();
		pcs = new PropertyChangeSupport (this);
		updateInterval = 100;
		startTime = endTime = totalProgress = 0;
		nrOfWorkers = SystemUtils.getNrOfLogicalCPUs ();
	}
	
	/**
	 * Tells the Simulator how often to report progress. The progress will be reported by
	 * firing a property change event on all listening PropertyChangeListeners.
	 * <br />
	 * Note: Calling this method AFTER the Simulator started execution will have no effect. If
	 * this method is not called at all, then the progress will be reported only once, when the
	 * SimulationWorker is finished (equivalent with calling <code>setUpdateInterval(100)</code>).
	 * 
	 * @param updateInterval the update interval. Value must be a strictly pozitive integer divisible by 100.
	 * 
	 * @throws IllegalArgumentException if the parameter is an invalid value.
	 */
	public void setUpdateInterval (int percentage)
	{
		if (percentage <= 0 || 100 % percentage != 0)
		{
			throw new IllegalArgumentException ("invalid update interval value");
		}
		
		if (startTime == 0)
		{
			updateInterval = percentage;
		}
	}
	
	public int getProgress ()
	{
		return totalProgress;
	}
	
	public SimulationFinalResult getResult ()
	{
		return simulationResult;
	}

	/**
	 * Adds a player to the simulation.
	 * <br />
	 * Note: if the profile has a set range of 100 %, it will be substituted with a new profile
	 * that has random as the hand type.
	 * 
	 * @param player the player.
	 * 
	 * @throws NullPointerException if player is null
	 * 
	 * @throws IllegalArgumentException if at least one of the cards contained within the player's
	 * profile are already contained within the profiles of other previously added profiles; or if
	 * at least one of those cards is already set as part of the community cards. This exception will be
	 * thrown only if the profile has a set hand type of PokerType.EXACTCARDS.
	 */
	public void addPlayer (PlayerProfile player)
	{
		if (player == null)
		{
			throw new NullPointerException ("invalid player added");
		}
		
		if (player.getHandType () == HandType.EXACTCARDS)
		{
			Card[] newCards = player.getCards ();
			
			for (PlayerProfile profile : profiles)
			{
				if (profile.getHandType () == HandType.EXACTCARDS)
				{
					Card[] existingCards = profile.getCards ();

					for (int i = 0; i < existingCards.length; i++)
					{
						if (isCardInArray (existingCards[i], newCards))
						{
							throw new IllegalArgumentException ("card already exists");
						}
					}
				}
			}
			
			for (Card communityCard : communityCards)
			{
				if (communityCard != null)
				{
					if (isCardInArray (communityCard, newCards))
					{
						throw new IllegalArgumentException ("card already exists");
					}
				}
			}
		}
		
		//if the player has a range of 100 % set, then it's basically a random hand
		if (player.getHandType () == HandType.RANGE &&
			player.getRange ().getRangePercentage () == 100)
		{
			profiles.add (new PlayerProfile (HandType.RANDOM, null, null));
		}
		else
		{
			profiles.add (player);
		}
	}
	
	public void setFlop (Card[] flopCards)
	{
		if (flopCards == null)
		{
			throw new NullPointerException ("null card not allowed");
		}
		else if (flopCards.length != 3)
		{
			throw new IllegalArgumentException ("flop must have 3 cards");
		}
		else if (flopCards[0] == null || flopCards[1] == null || flopCards[2] == null)
		{
			throw new NullPointerException ("null card not allowed");
		}
		
		for (PlayerProfile profile : profiles)
		{
			if (profile.getHandType () != HandType.EXACTCARDS)
			{
				continue;
			}
			
			Card[] cards = profile.getCards ();
			
			for (int i = 0; i < cards.length; i++)
			{
				if (isCardInArray (cards[i], flopCards))
				{
					throw new IllegalArgumentException ("cards already exist");
				}
			}
		}
		
		if (flopCards[0].equals (communityCards[3]) ||
			flopCards[0].equals (communityCards[4]) ||
			flopCards[1].equals (communityCards[3]) ||
			flopCards[1].equals (communityCards[4]) ||
			flopCards[2].equals (communityCards[3]) ||
			flopCards[2].equals (communityCards[4]))
		{
			throw new IllegalArgumentException ("cards already exist");
		}
		
		communityCards[0] = flopCards[0];
		communityCards[1] = flopCards[1];
		communityCards[2] = flopCards[2];
	}
	
	public void setTurn (Card turnCard)
	{
		if (turnCard == null)
		{
			throw new NullPointerException ("null card not allowed");
		}
		
		communityCards[3] = turnCard;
	}
	
	public void setRiver (Card riverCard)
	{
		if (riverCard == null)
		{
			throw new NullPointerException ("null card not allowed");
		}
		
		communityCards[4] = riverCard;
	}
	
	private boolean isCardInArray (Card c, Card[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i].equals (c))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void start ()
	{
		boolean commSet = true;
		for (int i = 0; i < 5; i++)
		{
			if (communityCards[i] == null)
			{
				commSet = false;
				break;
			}
		}
		
		if (commSet)
		{
			boolean correctPlayerTypes = false;
			for (PlayerProfile profile : profiles)
			{
				if (profile.getHandType () != HandType.EXACTCARDS)
				{
					correctPlayerTypes = true;
					break;
				}
			}
			
			if (! correctPlayerTypes)
			{
				throw new IllegalStateException ("incorrect player profile types");
			}
		}

		latch = new CountDownLatch (nrOfWorkers);
		int roundsPerWorker = getNrOfRoundsPerWorker (nrOfWorkers);
		
		executor = Executors.newFixedThreadPool (nrOfWorkers);
	
		for (int i = 0; i < nrOfWorkers; i++)
		{
			SimulationWorker worker = new SimulationWorker (i,
															gameType,
															profiles,
															communityCards,
															roundsPerWorker,
															latch);
			worker.setUpdateInterval (getUpdateInterval (nrOfWorkers));
			worker.addPropertyChangeListener (this);
			executor.execute (worker);
			workers.add (worker);
		}
		
		new Supervisor ().execute ();
		
		startTime = System.currentTimeMillis ();
	}
	
	private int getNrOfRoundsPerWorker (int workers)
	{
		while (nrRounds % workers != 0)
		{
			nrRounds++;
		}
		
		return nrRounds / workers;
	}
	
	private int getUpdateInterval (int workers)
	{
		switch (workers)
		{
			case 1:
			case 2: return 10;
			case 3:
			case 4: return 20;
			default: return 25;
		}
	}
	
	public void stop ()
	{
		executor.shutdownNow ();
		
		for (SimulationWorker worker : workers)
		{
			worker.cancel (true);
		}
	}
	
	public int getNrOfThreads ()
	{
		return nrOfWorkers;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		//will be called when a SimulationWorker reports progress
		//WARNING: will be called on the EDT
		
		if (! evt.getPropertyName ().equals ("progress"))
		{
			return;
		}
		
		int combinedProgress = 0;
		for (int i = 0; i < nrOfWorkers; i++)
		{
			combinedProgress += workers.get (i).getProgress ();
		}
		combinedProgress /= nrOfWorkers;
		
		//the property change fire for 100 % is made in the Supervisor
		//this is to ensure that the result will be available when the fire is triggered
		if (combinedProgress - totalProgress > updateInterval && combinedProgress != 100)
		{
			pcs.firePropertyChange ("progress", totalProgress, combinedProgress);
			
			totalProgress = combinedProgress;
		}
	}
	
	private class Supervisor extends SwingWorker<Void, Void>
	{
		@Override
		protected Void doInBackground () throws Exception
		{
			latch.await ();
			
			return null;
		}
		
		@Override
		protected void done ()
		{
			//will be called when all workers are done
			
			executor.shutdownNow ();
	
			if (workers.get (0).isCancelled ())
			{
				return;
			}

			endTime = System.currentTimeMillis ();

			int nrPlayers = profiles.size ();
			
			double[] wins = new double[nrPlayers];
			double[] loses = new double[nrPlayers];
			double[] ties = new double[nrPlayers];
			
			for (int j = 0; j < nrPlayers; j++)
			{
				wins[j] = loses[j] = ties[j] = 0;
			}
			
			//sum up all the percentages
			for (int i = 0; i < nrOfWorkers; i++)
			{
				SimulationWorkerResult result = workers.get (i).getResult ();
				
				for (int j = 0; j < nrPlayers; j++)
				{
					wins[j] += result.getWinPercentage (j);
					loses[j] += result.getLosePercentage (j);
					ties[j] += result.getTiePercentage (j);
				}
			}
			
			//average is needed, so... divide
			for (int j = 0; j < nrPlayers; j++)
			{
				wins[j] /= nrOfWorkers;
				loses[j] /= nrOfWorkers;
				ties[j] /= nrOfWorkers;
			}

			long duration = endTime - startTime;
			
			simulationResult = new SimulationFinalResult (gameType, profiles, wins, ties, loses, nrRounds, duration);
			
			//tell the listeners that the simulation is done
			SwingUtilities.invokeLater (new LastPropertyCall ());
		}
	}
	
	private class LastPropertyCall implements Runnable
	{
		@Override
		public void run ()
		{
			pcs.firePropertyChange ("progress", totalProgress, 100);
			totalProgress = 100;
		}
	}
	
	/**
	 * Adds a <code>PropertyChangeListener</code> to the listener list. The <code>listener</code> is
	 * registered for all properties. The same <code>listener</code> object may be added more
	 * than once, and will be called as many times as it is added. If <code>listener</code> is
	 * <code>null</code>, no exception is thrown and no action is taken. 
	 * 
	 * @param listener the <code>PropertyChangeListener</code> to be added
	 */
	public void addPropertyChangeListener (PropertyChangeListener listener)
	{
		pcs.addPropertyChangeListener (listener);
	}
	
	/**
	 * Removes a <code>PropertyChangeListener</code> from the listener list. This removes a
	 * <code>PropertyChangeListener</code> that was registered for all properties.
	 * If <code>listener</code> was added more than once to the same event source, it will be notified
	 * one less time after being removed. If <code>listener</code> is <code>null</code>, or was
	 * never added, no exception is thrown and no action is taken. 
	 * 
	 * @param listener the <code>PropertyChangeListener</code> to be removed
	 */
	public void removePropertyChangeListener (PropertyChangeListener listener)
	{
		pcs.removePropertyChangeListener (listener);
	}
}
