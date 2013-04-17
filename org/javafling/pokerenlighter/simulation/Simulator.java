package org.javafling.pokerenlighter.simulation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingWorker;
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
 * 
 * PlayerProfile player = new PlayerProfile (HandType.EXACTCARDS);
 * Card[] cards = {new Card ('A', 'h'), new Card ('K', 'h'), new Card ('6', 's'), new Card ('3', 'd')};
 * player.setCards (cards);
 * 
 * simulator.addPlayer (player);
 * simulator.addPlayer (new PlayerProfile (HandType.RANDOM));
 * simulator.addPlayer (new PlayerProfile (HandType.RANDOM));
 * 
 * simulator.start ();
 * 
 * simulator.addPropertyChangeListener (this);
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
public final class Simulator
{
	private PokerType gameType;
	private ArrayList<PlayerProfile> profiles;
	private int nrRounds;
	private ArrayList<SimulationWorker> workers;
	
	private ExecutorService executor;
	private CountDownLatch latch;
	
	private PropertyChangeSupport pcs;

	public Simulator (PokerType type, int rounds)
	{
		gameType = type;
		nrRounds = rounds;
		profiles = new ArrayList<> ();
		workers = new ArrayList<> ();
		pcs = new PropertyChangeSupport (this);
	}

	public void addPlayer (PlayerProfile player)
	{
		profiles.add (player);
	}
	
	public void start ()
	{
		int nrOfWorkers = SystemUtils.getNrOfLogicalCPUs ();
		latch = new CountDownLatch (nrOfWorkers);
		int roundsPerWorker = getNrOfRoundsPerWorker (nrOfWorkers);
		
		executor = Executors.newFixedThreadPool (nrOfWorkers);
	
		for (int i = 0; i < nrOfWorkers; i++)
		{
			SimulationWorker worker = new SimulationWorker (i, gameType, profiles, roundsPerWorker, latch);
			executor.execute (worker);
			workers.add (worker);
		}
		
		new Supervisor ().execute ();
	}
	
	private int getNrOfRoundsPerWorker (int workers)
	{
		while (nrRounds % workers != 0)
		{
			nrRounds++;
		}
		
		return nrRounds / workers;
	}
	
	public void stop ()
	{
		executor.shutdownNow ();
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
			
			stop ();
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
