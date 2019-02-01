import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;
import org.javafling.pokerenlighter.simulation.SimulationEvent;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult;
import org.javafling.pokerenlighter.simulation.SimulationNotifiable;
import org.javafling.pokerenlighter.simulation.Simulator;
import org.javafling.pokerenlighter.simulation.SimulatorBuilder;

public class Example implements SimulationNotifiable
{
    public Simulator simulator;
    
    public Example()
    {
        Card[] flopCards = {new Card(3, 'c'), new Card('J', 's'), new Card(10, 'd')};
        
        Card[] cards = {new Card('A', 'h'), new Card('K', 'h'), new Card(6, 's'), new Card(3, 'd')};
        PlayerProfile player = new PlayerProfile(HandType.EXACTCARDS, null, cards);
        
        SimulatorBuilder builder = new SimulatorBuilder();
        
        builder.setGameType(PokerType.OMAHA)
            .setNrRounds(100 * 1000)    //simulate for 100 thousand rounds
            .setNotifiable(this)        //call notification methods on "this" object
            .setUpdateInterval(10)      //call update method at progress intervals of at least 10 %
            .addPlayer(player)
            .addPlayer(new PlayerProfile(HandType.RANDOM, null, null))
            .addPlayer(new PlayerProfile(HandType.RANDOM, null, null))
            .setFlop(flopCards);
        
        this.simulator = builder.build();
    }
    
    public void start()
    {
        this.simulator.start();
    }
    
    @Override
    public void onSimulationStart(SimulationEvent event)
    {
        int workers = (Integer) event.getEventData();
        System.out.println("Simulator started on " + workers + " threads");
    }
    
    @Override
    public void onSimulationDone(SimulationEvent event)
    {
        SimulationFinalResult result = (SimulationFinalResult) event.getEventData();
        
        double w0 = result.getWinPercentage(0);
        double l0 = result.getLosePercentage(0);
        double t0 = result.getTiePercentage(0);
        
        double w1 = result.getWinPercentage(1);
        double l1 = result.getLosePercentage(1);
        double t1 = result.getTiePercentage(1);
        
        double w2 = result.getWinPercentage(2);
        double l2 = result.getLosePercentage(2);
        double t2 = result.getTiePercentage(2);
        
        System.out.println("Win 1: " + w0);
        System.out.println("Lose 1: " + l0);
        System.out.println("Tie 1: " + t0);
        
        System.out.println("Win 2: " + w1);
        System.out.println("Lose 2: " + l1);
        System.out.println("Tie 2: " + t1);
        
        System.out.println("Win 3: " + w2);
        System.out.println("Lose 3: " + l2);
        System.out.println("Tie 3: " + t2);
        
        long duration = result.getDuration();
        System.out.println("Duration: " + duration + " ms");
    }
    
    @Override
    public void onSimulationCancel(SimulationEvent event)
    {
        int progress = (Integer) event.getEventData();
        System.out.println("Simulation was stopped at " + progress + " percent");
    }
    
    @Override
    public void onSimulationProgress(SimulationEvent event)
    {
        int progress = (Integer) event.getEventData();
        System.out.println("Progress: " + progress + " %");
    }
    
    @Override
    public void onSimulationError(SimulationEvent event)
    {
        Exception e = (Exception) event.getEventData();
        System.err.println("The simulation encountered an error: " + e.getMessage());
    }
    
    public static void main(String[] args)
    {
        Example instance = new Example();
        
        instance.start();
    }
}
