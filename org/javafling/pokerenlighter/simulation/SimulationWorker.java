package org.javafling.pokerenlighter.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingWorker;

/**
 *
 * @author Murzea Radu
 * 
 * @version 1.0
 */
public class SimulationWorker extends SwingWorker<Void, Integer>
{
	private int ID;
	private CountDownLatch latch;
	private PokerType gameType;
	private ArrayList<PlayerProfile> profiles;
	private int rounds;
	
	public SimulationWorker(int ID,
							PokerType gameType,
							ArrayList<PlayerProfile> profiles,
							int rounds,
							CountDownLatch latch)
	{
		this.ID = ID;
		this.gameType = gameType;
		this.profiles = profiles;
		this.rounds = rounds;
		this.latch = latch;
	}
	
	@Override
	protected Void doInBackground () throws Exception
	{
		Random rand = new Random (System.currentTimeMillis ());
		
		Thread.sleep (ID * 1000 + rand.nextInt (1000));
		
		return null;
	}
	
	@Override
	protected void process (List<Integer> values)
	{
		
	}
	
	@Override
	protected void done()
	{
		latch.countDown();
	}
	
	
}
