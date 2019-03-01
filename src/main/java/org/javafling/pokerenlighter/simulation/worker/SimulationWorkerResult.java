package org.javafling.pokerenlighter.simulation.worker;

/**
 * Result of the worker job.
 * @author Radu Murzea
 */
public class SimulationWorkerResult
{
    private double[] wins, ties, loses;
    
    public SimulationWorkerResult(double[] wins, double[] ties, double[] loses)
    {
        if (wins == null || ties == null || loses == null) {
            throw new NullPointerException();
        } else if (wins.length != ties.length || ties.length != loses.length) {
            throw new IllegalArgumentException();
        }
        
        this.wins = wins;
        this.loses = loses;
        this.ties = ties;
    }
    
    public int getNrOfPlayers()
    {
        return wins.length;
    }
    
    public double getWinPercentage(int player)
    {
        return wins[player];
    }
    
    public double getLosePercentage(int player)
    {
        return loses[player];
    }
    
    public double getTiePercentage(int player)
    {
        return ties[player];
    }
}
