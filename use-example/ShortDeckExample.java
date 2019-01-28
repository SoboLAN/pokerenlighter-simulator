import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.*;

public class ShortDeckExample implements SimulationNotifiable {
    public Simulator simulator;

    public ShortDeckExample() {
        Card[] cards1 = Card.of("9s9d");
        PlayerProfile player1 = new PlayerProfile(HandType.EXACTCARDS, null, cards1);

        Card[] cards2 = Card.of("TsJh");
        PlayerProfile player2 = new PlayerProfile(HandType.EXACTCARDS, null, cards2);

        Simulator.SimulatorBuilder builder = new Simulator.SimulatorBuilder();

        builder.setGameType(PokerType.SHORT_DECK)
                .setNrRounds(100 * 1000)    //simulate for 10 thousand rounds
                .setNotifiable(this)        //call notification methods on "this" object
                .setUpdateInterval(10)      //call update method at progress intervals of at least 10 %
                .addPlayer(player1)
                .addPlayer(player2)
                .setFlop(Card.of("9c8dAh"));

        this.simulator = builder.build();
    }

    public void start() {
        this.simulator.start();
    }

    @Override
    public void onSimulationStart(SimulationEvent event) {
        int workers = (Integer) event.getEventData();
        System.out.println("Simulator started on " + workers + " threads");
    }

    @Override
    public void onSimulationDone(SimulationEvent event) {
        SimulationFinalResult result = (SimulationFinalResult) event.getEventData();

        double w0 = result.getWinPercentage(0);
        double l0 = result.getLosePercentage(0);
        double t0 = result.getTiePercentage(0);

        double w1 = result.getWinPercentage(1);
        double l1 = result.getLosePercentage(1);
        double t1 = result.getTiePercentage(1);

        System.out.println("Win 1: " + w0);
        System.out.println("Lose 1: " + l0);
        System.out.println("Tie 1: " + t0);

        System.out.println("Win 2: " + w1);
        System.out.println("Lose 2: " + l1);
        System.out.println("Tie 2: " + t1);

        long duration = result.getDuration();
        System.out.println("Duration: " + duration + " ms");
    }

    @Override
    public void onSimulationCancel(SimulationEvent event) {
        int progress = (Integer) event.getEventData();
        System.out.println("Simulation was stopped at " + progress + " percent");
    }

    @Override
    public void onSimulationProgress(SimulationEvent event) {
        int progress = (Integer) event.getEventData();
        System.out.println("Progress: " + progress + " %");
    }

    @Override
    public void onSimulationError(SimulationEvent event) {
        Exception e = (Exception) event.getEventData();
        System.err.println("The simulation encountered an error: " + e.getMessage());
    }

    public static void main(String[] args) {
        ShortDeckExample instance = new ShortDeckExample();

        instance.start();
    }
}
