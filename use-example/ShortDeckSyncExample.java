import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.*;

public class ShortDeckSyncExample {
    public SyncSimulator simulator;

    public ShortDeckSyncExample() {
        Card[] cards1 = Card.of("9s9d");
        PlayerProfile player1 = new PlayerProfile(HandType.EXACTCARDS, null, cards1);

        Card[] cards2 = Card.of("TsJh");
        PlayerProfile player2 = new PlayerProfile(HandType.EXACTCARDS, null, cards2);

        SimulatorBuilder builder = new SimulatorBuilder();

        builder.setGameType(PokerType.SHORT_DECK)
                .setNrRounds(100 * 1000)    //simulate for 100 thousand rounds
                .addPlayer(player1)
                .addPlayer(player2)
                .setFlop(Card.of("9c8dAh"));

        this.simulator = builder.buildSync();
        SimulationFinalResult result = this.simulator.start();
        onSimulationDone(result);
    }

    public void start() {
        this.simulator.start();
    }


    public void onSimulationDone(SimulationFinalResult result) {
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

    public static void main(String[] args) {
        ShortDeckSyncExample instance = new ShortDeckSyncExample();
        instance.start();
    }

}
