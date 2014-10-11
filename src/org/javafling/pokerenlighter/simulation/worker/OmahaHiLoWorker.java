package org.javafling.pokerenlighter.simulation.worker;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.combination.OmahaCombination;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;

public class OmahaHiLoWorker extends SimulationWorker
{
    public static abstract class OmahaHiLoBuilder<T extends OmahaHiLoBuilder<T>> extends SimulationWorker.WorkerBuilder<T>
    {
        @Override
        public OmahaHiLoWorker build()
        {
            if (super.getRounds() <= 0) {
                throw new IllegalStateException("The number of rounds must be a strictly positive number");
            } else if (super.getProfiles() == null || super.getProfiles().size() < 2) {
                throw new IllegalStateException("There need to be at least 2 players in every simulation.");
            } else if (super.getUpdateInterval() <= 0 || 100 % super.getUpdateInterval() != 0) {
                throw new IllegalStateException("Invalid update interval value");
            } else if (super.getNotifiable() == null) {
                throw new IllegalStateException("There needs to be a notifiable for this worker");
            }
            
            for (PlayerProfile profile : super.getProfiles()) {
                if (profile == null) {
                    throw new NullPointerException();
                }
            }
            
            return new OmahaHiLoWorker(this);
        }
    }
    
    private static class Builder2 extends OmahaHiLoBuilder<Builder2>
    {
        @Override
        protected Builder2 self()
        {
            return this;
        }
    }
    
    public static OmahaHiLoBuilder<?> builder()
    {
        return new Builder2();
    }
    
    private OmahaHiLoWorker(OmahaHiLoBuilder<?> builder)
    {
        super(builder);
    }
    
    @Override
    public PokerType getGameType()
    {
        return PokerType.OMAHA_HILO;
    }
    
    @Override
    public void doRun()
    {
        Deck deck = new Deck();
        
        removeUsedCards(deck);
        
        Card[][] playerCards = new Card[this.nrPlayers][4];
        
        for (int i = 0; i < this.nrPlayers; i++) {
            if (this.profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                Card[] excards = this.profiles.get(i).getCards();
                
                playerCards[i][0] = excards[0];
                playerCards[i][1] = excards[1];
                playerCards[i][2] = excards[2];
                playerCards[i][3] = excards[3];
            }
        }
        
        OmahaCombination[] playerCombinations = new OmahaCombination[this.nrPlayers];
        String[] playerHands = new String[this.nrPlayers];
        String[] playerHandsLo = new String[this.nrPlayers];
        Card[] currentHand = new Card[9];
        
        boolean[] tmpWins = new boolean[this.nrPlayers];
        boolean[] tmpTies = new boolean[this.nrPlayers];

        //main simulation loop
        for (int current_round = 1; current_round <= rounds && ! Thread.currentThread().isInterrupted(); current_round++) {
            deck.shuffle(10);

            //determine what each player has
            for (int i = 0; i < this.nrPlayers; i++) {
                if (profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                    currentHand[0] = playerCards[i][0];
                    currentHand[1] = playerCards[i][1];
                    currentHand[2] = playerCards[i][2];
                    currentHand[3] = playerCards[i][3];
                } else {
                    currentHand[0] = deck.getCard(i);
                    currentHand[1] = deck.getCard(i + this.nrPlayers);
                    currentHand[2] = deck.getCard(i + 2 * this.nrPlayers);
                    currentHand[3] = deck.getCard(i + 3 * this.nrPlayers);
                }
                
                //flop
                currentHand[4] = communityCards[0] == null ? deck.getCard(1 + 4 * this.nrPlayers) : communityCards[0];
                currentHand[5] = communityCards[1] == null ? deck.getCard(2 + 4 * this.nrPlayers) : communityCards[1];
                currentHand[6] = communityCards[2] == null ? deck.getCard(3 + 4 * this.nrPlayers) : communityCards[2];
    
                //turn
                currentHand[7] = communityCards[3] == null ? deck.getCard(5 + 4 * this.nrPlayers) : communityCards[3];
                
                //river
                currentHand[8] = communityCards[4] == null ? deck.getCard(7 + 4 * this.nrPlayers) : communityCards[4];
                
                if (playerCombinations[i] == null) {
                    playerCombinations[i] = new OmahaCombination(currentHand);
                } else {
                    playerCombinations[i].setCards(currentHand);
                }
                
                playerHands[i] = playerCombinations[i].getCombination();
                playerHandsLo[i] = playerCombinations[i].getLoCombination();
            }
            
            //TO DO:
            //from this point downwards, implement the recording mechanism (CAREFULLY!)
            
            int[] winningPlayers = getWinners (playerHands);
            int[] winningPlayersLo = getWinnersLo (playerHandsLo);
            
            //let's assume no one wins... at first
            for (int i = 0; i < this.nrPlayers; i++) {
                tmpWins[i] = tmpTies[i] = false;
            }
            
            //determine wins & ties for the hi part
            if (winningPlayers.length > 1) {
                for (int i = 0; i < winningPlayers.length; i++) {
                    tmpTies[winningPlayers[i]] = true;
                }
            } else {
                tmpWins[winningPlayers[0]] = true;
            }
            
            //determine wins & ties for the lo part
            if (winningPlayersLo.length > 1) {
                for (int i = 0; i < winningPlayersLo.length; i++) {
                    tmpTies[winningPlayersLo[i]] = true;
                }
            } else if (winningPlayersLo[0] != -1) {
                tmpWins[winningPlayersLo[0]] = true;
            }
            
            //now fill in the records
            for (int i = 0; i < this.nrPlayers; i++) {
                if (tmpWins[i]) {
                    this.wins[i]++;
                } else if (tmpTies[i]) {
                    this.ties[i]++;
                } else {
                    this.loses[i]++;
                }
            }
                        
            if (((current_round * 100) / this.rounds) % this.updateInterval == 0) {
                this.progress = (current_round) * 100 / this.rounds;
                WorkerEvent event;
                
                if (this.progress == 100) {
                    this.buildWorkerResult();
                    event = new WorkerEvent(WorkerEvent.EVENT_SIMWORKER_DONE, this.simResult);
                    this.notifiable.onSimulationDone(event);
                } else {
                    event = new WorkerEvent(WorkerEvent.EVENT_SIMWORKER_PROGRESS, this.progress);
                    this.notifiable.onSimulationProgress(event);
                }
            }
        }
    }
}
