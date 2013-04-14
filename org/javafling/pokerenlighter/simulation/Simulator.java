package org.javafling.pokerenlighter.simulation;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingWorker;
import org.javafling.pokerenlighter.main.SystemUtils;

/**
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

	public Simulator (PokerType type, int rounds)
	{
		gameType = type;
		nrRounds = rounds;
		profiles = new ArrayList<> ();
		workers = new ArrayList<> ();
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
		if (nrRounds % workers != 0)
		{
			while (nrRounds % workers != 0)
			{
				nrRounds++;
			}
		}
		
		return nrRounds / workers;
	}
	
	public void stop ()
	{
		
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
		}
	}
}
