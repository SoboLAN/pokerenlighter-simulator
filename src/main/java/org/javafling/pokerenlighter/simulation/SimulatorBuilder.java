package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;

import java.util.ArrayList;

public class SimulatorBuilder
{
    private PokerType gameType;
    private ArrayList<PlayerProfile> profiles;
    private int nrRounds;
    private Card[] communityCards = new Card[5];
    private int updateInterval;
    private SimulationNotifiable notifiable;

    public PokerType getGameType()
    {
        return this.gameType;
    }

    public SimulationNotifiable getNotifiable()
    {
        return this.notifiable;
    }

    public ArrayList<PlayerProfile> getProfiles()
    {
        return this.profiles;
    }

    public int getUpdateInterval()
    {
        return this.updateInterval;
    }

    public int getNrRounds()
    {
        return this.nrRounds;
    }

    public Card[] getCommunityCards()
    {
        return this.communityCards;
    }

    public SimulatorBuilder setGameType(PokerType gameType)
    {
        this.gameType = gameType;
        return this;
    }

    public SimulatorBuilder addPlayer(PlayerProfile profile)
    {
        if (this.profiles == null) {
            this.profiles = new ArrayList<>();
        }

        this.profiles.add(profile);

        return this;
    }

    public SimulatorBuilder setFlop(Card[] flopCards)
    {
        this.communityCards[0] = flopCards[0];
        this.communityCards[1] = flopCards[1];
        this.communityCards[2] = flopCards[2];

        return this;
    }

    public SimulatorBuilder setTurn(Card turn)
    {
        this.communityCards[3] = turn;
        return this;
    }

    public SimulatorBuilder setRiver(Card river)
    {
        this.communityCards[4] = river;
        return this;
    }

    public SimulatorBuilder setNrRounds(int nrRounds)
    {
        this.nrRounds = nrRounds;
        return this;
    }

    public SimulatorBuilder setUpdateInterval(int updateInterval)
    {
        this.updateInterval = updateInterval;
        return this;
    }

    public SimulatorBuilder setNotifiable(SimulationNotifiable notifiable)
    {
        this.notifiable = notifiable;
        return this;
    }

    private boolean isPredictableResult()
    {
        boolean commSet = true;
        for (int i = 0; i < 5; i++) {
            if (this.communityCards[i] == null) {
                commSet = false;
                break;
            }
        }

        if (commSet) {
            boolean correctPlayerTypes = false;
            for (PlayerProfile profile : this.profiles) {
                if (profile.getHandType() != HandType.EXACTCARDS) {
                    correctPlayerTypes = true;
                    break;
                }
            }

            if (! correctPlayerTypes) {
                return true;
            }
        }

        return false;
    }

    private Card[] getAllCards()
    {
        ArrayList<Card> cards = new ArrayList<>();

        for (Card card : this.communityCards) {
            if (card != null) {
                cards.add(card);
            }
        }

        for (PlayerProfile profile : this.profiles) {
            if (profile.getHandType() == HandType.EXACTCARDS) {
                Card[] playerCards = profile.getCards();
                for (Card card : playerCards) {
                    if (card != null) {
                        cards.add(card);
                    }
                }
            }
        }

        Card[] result = new Card[cards.size()];

        return cards.toArray(result);
    }

    private SimulatorBuilder validate(boolean async)
    {
        if (async && (this.updateInterval <= 0 || 100 % this.updateInterval != 0)) {
            throw new IllegalStateException("Invalid update interval value");
        } else if (this.gameType == null) {
            throw new IllegalStateException("Invalid game type value");
        } else if (this.nrRounds <= 0) {
            throw new IllegalStateException("Invalid rounds value");
        } else if (async && this.notifiable == null) {
            throw new IllegalStateException("No notifiable was set");
        } else if (this.profiles == null || this.profiles.size() < 2) {
            throw new IllegalStateException("Invalid or insufficient player profiles");
        }

        for (PlayerProfile profile : this.profiles) {
            if (profile == null) {
                throw new NullPointerException("NULL PlayerProfile was used");
            }
        }

        //flop cards must either be all equal to NULL or all different from NULL
        //if this is not the case (the "!" operator marks that), an exception is thrown
        if (! (
            (this.communityCards[0] == null && this.communityCards[1] == null && this.communityCards[2] == null)
            ||
            (this.communityCards[0] != null && this.communityCards[1] != null && this.communityCards[2] != null)
            )) {
            throw new IllegalStateException("Invalid flop cards");
        }

        //if the turn is set, but not the flop, that's not OK
        if (this.communityCards[3] != null && this.communityCards[0] == null) {
            throw new IllegalStateException("Turn card can not exist without flop");
        }

        //if the river is set, but not the turn, that's not OK
        if (this.communityCards[4] != null && this.communityCards[3] == null) {
            throw new IllegalStateException("River card can not exist without the turn card");
        }

        // if the result is predictable we just need one round
        if (this.isPredictableResult()) {
            this.nrRounds = 1;
        }

        Card[] allCards = this.getAllCards();
        for (int i = 0; i < allCards.length - 1; i++) {
            for (int j = i + 1; j < allCards.length; j++) {
                if (allCards[i].equals(allCards[j])) {
                    throw new IllegalStateException("No duplicate cards allowed");
                }
            }
        }

        return this;
    }

    public Simulator build() {
        validate(true);
        return new Simulator(this);
    }

    public SyncSimulator buildSync() {
        validate(false);
        return new SyncSimulator(this);
    }

}
