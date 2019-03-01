package org.javafling.pokerenlighter.simulation.worker;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.combination.Deck;
import org.javafling.pokerenlighter.combination.FiveCardOmahaCombination;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PokerType;

public class FiveCardOmahaHiLoWorker extends SimulationWorker
{
    public static abstract class FiveCardOmahaHiLoBuilder<T extends FiveCardOmahaHiLoBuilder<T>> extends SimulationWorker.WorkerBuilder<T>
    {
        @Override
        public FiveCardOmahaHiLoWorker build()
        {
            validate();
            return new FiveCardOmahaHiLoWorker(this);
        }

    }
    
    private static class Builder2 extends FiveCardOmahaHiLoBuilder<Builder2>
    {
        @Override
        protected Builder2 self()
        {
            return this;
        }
    }
    
    public static FiveCardOmahaHiLoBuilder<?> builder()
    {
        return new Builder2();
    }
    
    private FiveCardOmahaHiLoWorker(FiveCardOmahaHiLoBuilder<?> builder)
    {
        super(builder);
    }
    
    @Override
    public PokerType getGameType()
    {
        return PokerType.FOMAHA_HILO;
    }
    
    @Override
    public void doRun()
    {
        Deck deck = new Deck();
        
        removeUsedCards(deck);
        
        Card[][] playerCards = new Card[this.nrPlayers][5];
        
        for (int i = 0; i < this.nrPlayers; i++) {
            if (this.profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                Card[] excards = this.profiles.get(i).getCards();
                
                playerCards[i][0] = excards[0];
                playerCards[i][1] = excards[1];
                playerCards[i][2] = excards[2];
                playerCards[i][3] = excards[3];
                playerCards[i][4] = excards[4];
            }
        }
        
        FiveCardOmahaCombination[] playerCombinations = new FiveCardOmahaCombination[this.nrPlayers];
        String[] playerHands = new String[this.nrPlayers];
        String[] playerHandsLo = new String[this.nrPlayers];
        Card[] currentHand = new Card[10];
        
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
                    currentHand[4] = playerCards[i][4];
                } else {
                    currentHand[0] = deck.getCard(i);
                    currentHand[1] = deck.getCard(i + this.nrPlayers);
                    currentHand[2] = deck.getCard(i + 2 * this.nrPlayers);
                    currentHand[3] = deck.getCard(i + 3 * this.nrPlayers);
                    currentHand[4] = deck.getCard(i + 4 * this.nrPlayers);
                }
                
                //flop
                currentHand[5] = communityCards[0] == null ? deck.getCard(1 + 5 * this.nrPlayers) : communityCards[0];
                currentHand[6] = communityCards[1] == null ? deck.getCard(2 + 5 * this.nrPlayers) : communityCards[1];
                currentHand[7] = communityCards[2] == null ? deck.getCard(3 + 5 * this.nrPlayers) : communityCards[2];
    
                //turn
                currentHand[8] = communityCards[3] == null ? deck.getCard(5 + 5 * this.nrPlayers) : communityCards[3];
                
                //river
                currentHand[9] = communityCards[4] == null ? deck.getCard(7 + 5 * this.nrPlayers) : communityCards[4];
                
                if (playerCombinations[i] == null) {
                    playerCombinations[i] = new FiveCardOmahaCombination(currentHand);
                } else {
                    playerCombinations[i].setCards(currentHand);
                }
                
                playerHands[i] = playerCombinations[i].getCombination();
                playerHandsLo[i] = playerCombinations[i].getLoCombination();
            }
            
            //TO DO:
            //from this point downwards, implement the recording mechanism (CAREFULLY!)
            
            int[] winningPlayers = super.getWinners(playerHands);
            int[] winningPlayersLo = super.getWinnersLo(playerHandsLo);
            
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

            handleProgress(current_round);
        }
    }
}
