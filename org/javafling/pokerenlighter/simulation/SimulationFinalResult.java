package org.javafling.pokerenlighter.simulation;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Murzea Radu
 */
public class SimulationFinalResult
{
	private double[] wins, ties, loses;
	private int rounds;
	private long duration;
	private PokerType gameType;
	private ArrayList<PlayerProfile> players;
	private int nrThreads;
	
	private DecimalFormat formatter;
	
	public SimulationFinalResult (PokerType gameType, ArrayList<PlayerProfile> players,
									double[] wins, double[] ties, double[] loses, int rounds,
									int nrThreads, long duration)
	{
		if (players == null || wins == null || ties == null || loses == null)
		{
			throw new NullPointerException ();
		}
		else if (wins.length != ties.length || ties.length != loses.length || rounds <= 0 || duration <= 0)
		{
			throw new IllegalArgumentException ();
		}
		
		this.players = players;
		this.wins = wins;
		this.loses = loses;
		this.ties = ties;
		this.rounds = rounds;
		this.duration = duration;
		this.gameType = gameType;
		this.nrThreads = nrThreads;
		
		formatter = new DecimalFormat ();
		formatter.setMaximumFractionDigits (2);
	}
	
	public int getNrOfPlayers ()
	{
		return wins.length;
	}
	
	public int getNrOfThreads ()
	{
		return nrThreads;
	}
	
	public double getWinPercentage (int player)
	{
		return wins[player];
	}
	
	public double getLosePercentage (int player)
	{
		return loses[player];
	}
	
	public double getTiePercentage (int player)
	{
		return ties[player];
	}
	
	public String getFormattedWinPercentage (int player)
	{
		return formatter.format (wins[player]);
	}
	
	public String getFormattedLosePercentage (int player)
	{
		return formatter.format (loses[player]);
	}
	
	public String getFormattedTiePercentage (int player)
	{
		return formatter.format (ties[player]);
	}
	
	public long getDuration ()
	{
		return duration;
	}
	
	public int getRounds ()
	{
		return rounds;
	}
	
	public PokerType getPokerType ()
	{
		return gameType;
	}
	
	public PlayerProfile getPlayer (int player)
	{
		return players.get (player);
	}
}
