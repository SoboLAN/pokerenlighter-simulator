package org.javafling.pokerenlighter.simulation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.javafling.pokerenlighter.combination.Card;

/**
 * Result of the simulation. Use the inner class ResultBuilder to build an object of this class.
 * @author Radu Murzea
 */
public class SimulationFinalResult
{
    private double[] wins, ties, loses;
    private int rounds;
    private long duration;
    private PokerType gameType;
    private Card[] flop;
    private Card turn;
    private Card river;
    private ArrayList<PlayerProfile> players;
    private int nrThreads;
    
    private DecimalFormat formatter;
    
    public static class ResultBuilder
    {
        private double[] wins, ties, loses;
        private int rounds;
        private long duration;
        private PokerType gameType;
        private Card[] flop;
        private Card turn;
        private Card river;
        private ArrayList<PlayerProfile> players;
        private int nrThreads;
        
        public ResultBuilder setGameType(PokerType gameType)
        {
            this.gameType = gameType;
            return this;
        }
        
        public ResultBuilder setPlayers(ArrayList<PlayerProfile> players)
        {
            this.players = players;
            return this;
        }
        
        public ResultBuilder setFlop(Card[] flop)
        {
            this.flop = flop;
            return this;
        }
        
        public ResultBuilder setTurn(Card turn)
        {
            this.turn = turn;
            return this;
        }
        
        public ResultBuilder setRiver(Card river)
        {
            this.river = river;
            return this;
        }
        
        public ResultBuilder setWins(double[] wins)
        {
            this.wins = wins;
            return this;
        }
        
        public ResultBuilder setLoses(double[] loses)
        {
            this.loses = loses;
            return this;
        }
        
        public ResultBuilder setTies(double[] ties)
        {
            this.ties = ties;
            return this;
        }
        
        public ResultBuilder setRounds(int rounds)
        {
            this.rounds = rounds;
            return this;
        }
        
        public ResultBuilder setThreads(int nrThreads)
        {
            this.nrThreads = nrThreads;
            return this;
        }
        
        public ResultBuilder setDuration(long duration)
        {
            this.duration = duration;
            return this;
        }
        
        public SimulationFinalResult build()
        {
            if (gameType == null
                || players == null
                || wins == null
                || ties == null
                || loses == null
                || wins.length != ties.length
                || ties.length != loses.length
                || rounds <= 0
                || duration <= 0
                || nrThreads <= 0)
            {
                throw new IllegalStateException();
            }
            
            if (flop != null) {
                if (flop.length != 3
                    || flop[0] == null
                    || flop[1] == null
                    || flop[2] == null
                    || flop[0].equals (flop[1])
                    || flop[0].equals (flop[2])
                    || flop[1].equals (flop[2]))
                {
                    throw new IllegalStateException();
                }
            }
            
            return new SimulationFinalResult(this);
        }
    }
    
    //constructor
    private SimulationFinalResult(ResultBuilder builder)
    {
        this.players = builder.players;
        this.wins = builder.wins;
        this.loses = builder.loses;
        this.ties = builder.ties;
        this.flop = builder.flop;
        this.turn = builder.turn;
        this.river = builder.river;
        this.rounds = builder.rounds;
        this.duration = builder.duration;
        this.gameType = builder.gameType;
        this.nrThreads = builder.nrThreads;
        
        formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(2);
    }
    
    public int getNrOfPlayers()
    {
        return wins.length;
    }
    
    public int getNrOfThreads()
    {
        return nrThreads;
    }
    
    public Card[] getFlop()
    {
        return flop;
    }
    
    public Card getTurn()
    {
        return turn;
    }
    
    public Card getRiver()
    {
        return river;
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
    
    public String getFormattedWinPercentage(int player)
    {
        return formatter.format(wins[player]);
    }
    
    public String getFormattedLosePercentage(int player)
    {
        return formatter.format(loses[player]);
    }
    
    public String getFormattedTiePercentage(int player)
    {
        return formatter.format(ties[player]);
    }
    
    public long getDuration()
    {
        return duration;
    }
    
    public int getRounds()
    {
        return rounds;
    }
    
    public PokerType getPokerType()
    {
        return gameType;
    }
    
    public PlayerProfile getPlayer(int player)
    {
        return players.get(player);
    }
}
