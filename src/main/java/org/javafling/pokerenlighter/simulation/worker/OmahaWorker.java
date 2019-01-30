package org.javafling.pokerenlighter.simulation.worker;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.combination.OmahaCombination;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PokerType;

public class OmahaWorker extends SimulationWorker
{
    public static abstract class OmahaBuilder<T extends OmahaBuilder<T>> extends SimulationWorker.WorkerBuilder<T>
    {
        @Override
        public OmahaWorker build()
        {
            validate();
            return new OmahaWorker(this);
        }
    }
    
    private static class Builder2 extends OmahaBuilder<Builder2>
    {
        @Override
        protected Builder2 self()
        {
            return this;
        }
    }
    
    public static OmahaBuilder<?> builder()
    {
        return new Builder2();
    }
    
    private OmahaWorker(OmahaBuilder<?> builder)
    {
        super(builder);
    }
    
    @Override
    public PokerType getGameType()
    {
        return PokerType.OMAHA;
    }
    
    @Override
    public void doRun()
    {
        Deck deck = new Deck();
        
        removeUsedCards(deck);
        
        Card[][] playerCards = new Card[this.nrPlayers][4];
        
        for (int i = 0; i < this.nrPlayers; i++) {
            if (profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                Card[] excards = profiles.get(i).getCards();
                
                playerCards[i][0] = excards[0];
                playerCards[i][1] = excards[1];
                playerCards[i][2] = excards[2];
                playerCards[i][3] = excards[3];
            }
        }
        
        OmahaCombination[] playerCombinations = new OmahaCombination[this.nrPlayers];
        String[] playerHands = new String[this.nrPlayers];
        Card[] currentHand = new Card[9];

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
            }
            
            int[] winningPlayers = getWinners(playerHands);
            
            //multiple winners a.k.a. a tie
            if (winningPlayers.length > 1) {
                for (int i = 0; i < winningPlayers.length; i++) {
                    ties[winningPlayers[i]]++;
                }
            //only 1 winner
            } else {
                this.wins[winningPlayers[0]]++;
            }
            
            for (int i = 0; i < this.nrPlayers; i++) {
                if (! contains(winningPlayers, i)) {
                    this.loses[i]++;
                }
            }

            handleProgress(current_round);
        }
    }
}
