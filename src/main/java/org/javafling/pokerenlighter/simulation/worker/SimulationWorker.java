package org.javafling.pokerenlighter.simulation.worker;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;

import java.util.ArrayList;

/**
 * This class performs the actual simulations, based on the parameters passed to its constructor.
 * Its very advisable to pass a <code>WorkerNotifiable</code> instance to the constructor in order to get notified
 * of significant events that happen during processing (progress, termination, error etc.).
 * @author Radu Murzea
 */
public abstract class SimulationWorker implements Runnable
{
    protected int nrPlayers;
    
    //temporary results containment
    protected int[] wins, loses, ties;
    protected SimulationWorkerResult simResult;
    
    protected WorkerNotifiable notifiable;
    
    protected Card[] communityCards;
    
    //simulation data
    protected ArrayList<PlayerProfile> profiles;
    protected int rounds;
    
    //other properties necessary for operations
    protected int updateInterval;
    protected int progress;

    protected boolean async;
    
    public static abstract class WorkerBuilder<T extends WorkerBuilder<T>>
    {
        private WorkerNotifiable notifiable;
        private Card[] communityCards;
        private ArrayList<PlayerProfile> profiles;
        private int rounds;
        private int updateInterval;
        private boolean async = true;

        protected abstract T self();
        public abstract SimulationWorker build();

        protected void validate() {
            if (getRounds() <= 0) {
                throw new IllegalStateException("The number of rounds must be a strictly positive number");
            } else if (getProfiles() == null || getProfiles().size() < 2) {
                throw new IllegalStateException("There need to be at least 2 players in every simulation.");
            } else if (isAsync() && (getUpdateInterval() <= 0 || 100 % getUpdateInterval() != 0)) {
                throw new IllegalStateException("Invalid update interval value");
            } else if (isAsync() && getNotifiable() == null) {
                throw new IllegalStateException("There needs to be a notifiable for this worker");
            }

            for (PlayerProfile profile : getProfiles()) {
                if (profile == null) {
                    throw new NullPointerException();
                }
            }
        }

        public T setRounds(int rounds)
        {
            this.rounds = rounds;
            return self();
        }
        
        public T setCommunityCards(Card[] cards)
        {
            this.communityCards = cards;
            return self();
        }
        
        public T addPlayer(PlayerProfile profile)
        {
            if (this.profiles == null) {
                this.profiles = new ArrayList<>();
            }
            
            this.profiles.add(profile);
            
            return self();
        }
        
        public T setUpdateInterval(int updateInterval)
        {
            this.updateInterval = updateInterval;
            return self();
        }
        
        public T setNotifier(WorkerNotifiable notifiable)
        {
            this.notifiable = notifiable;
            return self();
        }

        public T setAsync(boolean async) {
            this.async = async;
            return self();
        }
        
        public WorkerNotifiable getNotifiable()
        {
            return notifiable;
        }

        public Card[] getCommunityCards()
        {
            return communityCards;
        }

        public ArrayList<PlayerProfile> getProfiles()
        {
            return profiles;
        }

        public int getRounds()
        {
            return rounds;
        }

        public int getUpdateInterval()
        {
            return updateInterval;
        }

        public boolean isAsync() { return async; }
    }
        
    protected SimulationWorker(WorkerBuilder<?> builder)
    {
        this.communityCards = builder.getCommunityCards();
        this.notifiable = builder.getNotifiable();
        this.profiles = builder.getProfiles();
        this.rounds = builder.getRounds();
        this.updateInterval = builder.getUpdateInterval();
        this.nrPlayers = builder.getProfiles().size();
        this.async = builder.isAsync();
    }
            
    public int getProgress()
    {
        return progress;
    }

    public boolean isAsync() { return async; }

    @Override
    public void run()
    {
        wins = new int[nrPlayers];
        loses = new int[nrPlayers];
        ties = new int[nrPlayers];
        
        for (int i = 0; i < nrPlayers; i++) {
            wins[i] = loses[i] = ties[i] = 0;
        }
        
        doRun();
    }
    
    public abstract void doRun();
    
    public abstract PokerType getGameType();
    
    public SimulationWorkerResult getResult()
    {
        return this.simResult;
    }
    
    //checks if the the element "nr" is found in the array "arr"
    protected boolean contains (int[] arr, int nr) {
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] == nr) {
                return true;
            }
        }

        return false;
    }
    
    protected void removeUsedCards(Deck deck)
    {
        for (PlayerProfile profile : this.profiles) {
            if (profile.getHandType () == HandType.EXACTCARDS) {
                Card[] pcards = profile.getCards();
                
                for (Card pcard : pcards) {
                    deck.removeCard(pcard);
                }
            }
        }
        
        for (Card communityCard : this.communityCards) {
            if (communityCard != null) {
                deck.removeCard(communityCard);
            }
        }
    }
    
    protected void buildWorkerResult()
    {
        double[] winP = new double[this.nrPlayers];
        double[] losesP = new double[this.nrPlayers];
        double[] tiesP = new double [this.nrPlayers];

        for (int i = 0; i < this.nrPlayers; i++) {
            winP[i] = (100.0 * this.wins[i]) / this.rounds;
            losesP[i] = (100.0 * this.loses[i]) / this.rounds;
            tiesP[i] = (100.0 * this.ties[i]) / this.rounds;
        }

        this.simResult = new SimulationWorkerResult(winP, tiesP, losesP);
    }
    
    // Handles the situation where 2 (or more) players have the same rank in a hand (for
    // example: 3 players have three of a kind).
    //
    // The parameter hands array of strings containing the combinations to compare. The first character
    // should be the rank of the combination.
    //
    // Returns array of integers representing the IDs of the winning hands. For example: if the
    // parameter contains 6 hands and it is determined that the first and last hand are winners,
    // then the returning array will contain 0 and 5 (in this order) (0-based index).
    protected int[] getWinners(String[] hands)
    {
        int j, k, compare_result, nr_winners = this.nrPlayers;
        boolean[] eliminated = new boolean[this.nrPlayers];
        int[] winners;

        for (j = 0; j < this.nrPlayers; ++j) {
            eliminated[j] = false;
        }

        for (j = 0; j < this.nrPlayers - 1; ++j) {
            for (k = j + 1; k < this.nrPlayers; ++k) {
                if (! eliminated[j] && ! eliminated[k]) {
                    compare_result = compareHands(hands[j], hands[k]);

                    if (compare_result == 1) {
                        eliminated[k] = true;
                        nr_winners--;
                    } else if (compare_result == 2) {
                        eliminated[j] = true;
                        nr_winners--;
                    }
                }
            }
        }

        winners = new int[nr_winners];
        k = 0;

        for (j = 0; j < this.nrPlayers; ++j) {
            if (! eliminated[j]) {
                winners[k++] = j;
            }
        }

        return winners;
    }
    
    protected int[] getWinnersLo(String[] hands)
    {
        int i;
        boolean lohands = false;

        for (i = 0; i < this.nrPlayers; ++i) {
            if (! hands[i].equals("0")) {
                lohands = true;
                break;
            }
        }

        if (! lohands) {
            int[] winners = new int[1];
            winners[0] = -1;
            return winners;
        }

        boolean[] eliminated = new boolean[this.nrPlayers];

        int j, compare_result, nr_winners;

        for (i = 0; i < this.nrPlayers; ++i) {
            eliminated[i] = false;
        }

        nr_winners = this.nrPlayers;

        for (i = 0; i < this.nrPlayers - 1; ++i) {
            for (j = i + 1; j < this.nrPlayers; ++j) {
                if (! eliminated[i] && ! eliminated[j]) {
                    compare_result = compareHandsLo(hands[i], hands[j]);

                    if (compare_result == 1) {
                        eliminated[j] = true;
                        --nr_winners;
                    } else if (compare_result == 2) {
                        eliminated[i] = true;
                        --nr_winners;
                    }
                }
            }
        }

        int[] winners = new int[nr_winners];
        j = 0;

        for (i = 0; i < this.nrPlayers; ++i) {
            if (! eliminated[i]) {
                winners[j] = i;
                ++j;
            }
        }

        return winners;
    }
    
    // Compares two combinations.
    //
    // parameter hand1 = The first combination.
    // parameter hand2  = The second combination.
    //
    // returns 1 if the combination hand1 is better, 2 if hand2 is better, 0 if they are equal.
    private int compareHands(String hand1, String hand2)
    {
        int r1 = (int) (hand1.charAt(0)) - 48, r2 = (int) (hand2.charAt(0)) - 48;

        //compare ranks
        //if one is bigger than the other, it's done
        if (r1 > r2) {
            return 1;
        } else if (r2 > r1) {
            return 2;
        }

        //check for royal flushes
        if (r1 == 9) {
            return (r2 == 9) ? 0 : 1;
        } else if (r2 == 9) {
            return 2;
        }

        int hand_length = hand1.length() - 1;

        for (int i = 1; i <= hand_length; ++i) {
            if (Card.getRank(hand1.charAt(i)) > Card.getRank(hand2.charAt(i))) {
                return 1;
            } else if (Card.getRank(hand1.charAt(i)) < Card.getRank(hand2.charAt(i))) {
                return 2;
            }
        }

        return 0;
    }

    private int compareHandsLo(String hand1, String hand2)
    {
        if (hand1.equals("0")) {
            return hand2.equals("0") ? 0 : 2;
        } else if (hand2.equals("0")) {
            return 1;
        }
        
        for (int i = 0; i < 5; i++) {
            if (Card.getRank(hand1.charAt(i)) > Card.getRank(hand2.charAt(i))) {
                return 2;
            } else if (Card.getRank(hand2.charAt(i)) > Card.getRank(hand1.charAt(i))) {
                return 1;
            }
        }

        return 0;
    }

    protected void handleProgress(int current_round) {
        if ((isAsync() && (((current_round * 100) / rounds) % updateInterval == 0)) ||
                !isAsync() && current_round >= rounds) {
            this.progress = (current_round) * 100 / rounds;
            WorkerEvent event;

            if (this.progress == 100) {
                this.buildWorkerResult();
                event = new WorkerEvent(WorkerEvent.EVENT_SIMWORKER_DONE, this.simResult);
                if(this.notifiable != null) {
                    this.notifiable.onSimulationDone(event);
                }
            } else {
                event = new WorkerEvent(WorkerEvent.EVENT_SIMWORKER_PROGRESS, this.progress);
                if(this.notifiable != null) {
                    this.notifiable.onSimulationProgress(event);
                }
            }
        }
    }

}
