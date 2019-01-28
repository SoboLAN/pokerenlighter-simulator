package org.javafling.pokerenlighter.simulation.worker;

import org.javafling.pokerenlighter.combination.*;
import org.javafling.pokerenlighter.simulation.HandType;
import org.javafling.pokerenlighter.simulation.PlayerProfile;
import org.javafling.pokerenlighter.simulation.PokerType;

public class ShortDeckWorker extends SimulationWorker {
    public static abstract class ShortDeckWorkerBuilder<T extends ShortDeckWorkerBuilder<T>> extends SimulationWorker.WorkerBuilder<T> {
        @Override
        public ShortDeckWorker build() {
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

            return new ShortDeckWorker(this);
        }
    }

    private static class Builder2 extends ShortDeckWorkerBuilder<ShortDeckWorker.Builder2> {
        @Override
        protected ShortDeckWorker.Builder2 self() {
            return this;
        }
    }

    public static ShortDeckWorkerBuilder<?> builder() {
        return new ShortDeckWorker.Builder2();
    }

    private ShortDeckWorker(ShortDeckWorkerBuilder<?> builder) {
        super(builder);
    }

    @Override
    public PokerType getGameType() {
        return PokerType.SHORT_DECK;
    }

    @Override
    public void doRun() {
        Deck deck = new Deck(36, 6, 13);

        this.removeUsedCards(deck);

        Card[][] playerCards = new Card[nrPlayers][7];

        for (int i = 0; i < nrPlayers; i++) {
            if (profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                Card[] excards = profiles.get(i).getCards();

                playerCards[i][0] = excards[0];
                playerCards[i][1] = excards[1];
            }
        }

        ShortDeckCombination[] playerCombinations = new ShortDeckCombination[nrPlayers];
        String[] playerHands = new String[nrPlayers];
        Card[] currentHand = new Card[7];

        //main simulation loop
        for (int current_round = 1; current_round <= rounds && !Thread.currentThread().isInterrupted(); current_round++) {
            deck.shuffle(10);

            //this is to ensure that all players that selected a range hand will have cards
            //that fit into those ranges
            //WARNING: if range(s) is/are narrow, this will have significant performance impact (!!)
            boolean okRanges = true;
            do {
                boolean okRangesInside = true;
                for (int i = 0; i < profiles.size(); i++) {
                    if (profiles.get(i).getHandType() == HandType.RANGE
                            && !profiles.get(i).getRange().containsHand(deck.getCard(i), deck.getCard(i + nrPlayers))) {
                        okRangesInside = false;
                        break;
                    }
                }

                if (!okRangesInside) {
                    //minimum shuffle should be enough
                    deck.shuffle(5);
                }

                okRanges = okRangesInside;
            } while (!okRanges);

            //determine what each player has
            for (int i = 0; i < nrPlayers; i++) {
                if (profiles.get(i).getHandType() == HandType.EXACTCARDS) {
                    currentHand[0] = playerCards[i][0];
                    currentHand[1] = playerCards[i][1];
                } else {
                    currentHand[0] = deck.getCard(i);
                    currentHand[1] = deck.getCard(i + nrPlayers);
                }

                //flop
                currentHand[2] = communityCards[0] == null ? deck.getCard(1 + 2 * nrPlayers) : communityCards[0];
                currentHand[3] = communityCards[1] == null ? deck.getCard(2 + 2 * nrPlayers) : communityCards[1];
                currentHand[4] = communityCards[2] == null ? deck.getCard(3 + 2 * nrPlayers) : communityCards[2];

                //turn
                currentHand[5] = communityCards[3] == null ? deck.getCard(5 + 2 * nrPlayers) : communityCards[3];

                //river
                currentHand[6] = communityCards[4] == null ? deck.getCard(7 + 2 * nrPlayers) : communityCards[4];

                if (playerCombinations[i] == null) {
                    playerCombinations[i] = new ShortDeckCombination(currentHand);
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
                wins[winningPlayers[0]]++;
            }

            for (int i = 0; i < nrPlayers; i++) {
                if (!contains(winningPlayers, i)) {
                    loses[i]++;
                }
            }

            if (((current_round * 100) / rounds) % updateInterval == 0) {
                this.progress = (current_round) * 100 / rounds;
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
