package org.javafling.pokerenlighter.combination;

/**
 * Representation of a standard deck of 52 (French) cards. There are 13 cards of each color (4 colors).
 * <br /><br />
 * This class is not thread-safe.
 *
 * @author Radu Murzea
*/
public class Deck
{
    //storage place for the cards
    protected Card[] cards;

    //random number generator. Needed for the shuffle () method.
    protected HighQualityRandomGenerator rand;

    //size of the deck (needed since adding and removal of cards from the deck is permitted)
    protected int size;

    protected int fullDeckSize;

    /**
     * Constructs a deck of cards with the 52 unique cards.
     * The initial order is: 2c, 3c, 4c, ... Kc, Ac, 2d, 3d, 4d, ... Kd, Ad, 2h, 3h, 4h, ... Kh, Ah,
     * 2s, 3s, 4s, ... Ks, As.
     * <br />
     * By shuffling the deck, this arrangement is lost and cannot be restored.
     */
    public Deck() {
        this(52, 2, 13);
    }

    public Deck(int deckSize, int lowCard, int highCard)
    {
        size = deckSize;
        fullDeckSize = deckSize;
        cards = new Card[size];

        int offset = highCard - lowCard + 2;
        for (int j = lowCard, i = 0; i < offset; ++j, ++i) {
            cards[i + 0 * offset] = new Card(j, 'c');
            cards[i + 1 * offset] = new Card(j, 'd');
            cards[i + 2 * offset] = new Card(j, 'h');
            cards[i + 3 * offset] = new Card(j, 's');
        }

        rand = new HighQualityRandomGenerator();
    }

    public int getFullDeckSize() {
        return fullDeckSize;
    }

    /**
     * Shuffles the <code>Deck</code>.
     *
     * @param intensity Specifies how much the <code>Deck</code> should be shuffled. Accepted values
     * are between 5 and 30, 5 being least intense and 30 being most intense.
     *
     * @throws IllegalArgumentException if the intensity parameter is not between 5 and 30.
     */
    public final void shuffle(int intensity)
    {
        if (intensity < 5 || intensity > 30) {
            throw new IllegalArgumentException("Invalid intensity range");
        }

        //repeat "intensity" times
        for (int j = 0; j < intensity; ++j) {
            //each card is swapped with another card (random)
            for (int i = 0; i < size - 1; ++i) {
                int r = i + (rand.nextInt (10) % (size - i));
                Card temp = cards[i];
                cards[i] = cards[r];
                cards[r] = temp;
            }
        }
    }

    /**
     * Removes from the <code>Deck</code> the <code>Card</code> given by its parameter.
     * If the <code>Card</code> is not in the <code>Deck</code> nothing happens.
     *
     * @param c The <code>Card</code> to be removed.
     */
    public final void removeCard(Card c)
    {
        if (c == null) {
            throw new NullPointerException("Attempted to remove a NULL card from the deck");
        }

        //go through the whole deck
        for (int i = 0; i < size; ++i) {
            //find the card
            if (cards[i].equals(c)) {
                //when found, move following cards 1 position to the left
                //(basically losing the reference to the "killed" card)
                for (int j = i; j < size - 1; ++j) {
                    cards[j] = cards[j + 1];
                }

                //after the above operation, there are 2 references to the last card.
                //let's not keep that.
                //also, the size of the deck is smaller
                cards[--size] = null;

                return;
            }
        }
    }

    /**
     * Adds a <code>Card</code> to the <code>Deck</code>. If the <code>Deck</code> is full or
     * the <code>Card</code> already exists in the <code>Deck</code>, nothing happens.
     *
     * @param c The <code>Card</code> to be added.
     *
     * @throws NullPointerException if the parameter is NULL.
     */
    public final void addCard(Card c)
    {
        if (c == null) {
            throw new NullPointerException("Attempted to add a NULL card to the deck");
        }

        //if the deck is full... well, sorry.
        if (size == getFullDeckSize()) {
            return;
        }

        //if the card is already in the deck, do nothing.
        for (int i = 0; i < size; ++i) {
            if (cards[i].equals(c)) {
                return;
            }
        }

        //add the card and increase size
        cards[size++] = c;
    }

    /**
     * Checks if the <code>Deck</code> is full (has all 52 cards).
     *
     * @return <code>true</code> if the <code>Deck</code> has 52 cards, <code>false</code> otherwise.
     */
    public final boolean isFull()
    {
        return (size == getFullDeckSize());
    }

    /**
     * Returns the number of <code>Card</code>s in the <code>Deck</code>.
     *
     * @return The number of <code>Card</code>s.
     */
    public final int getSize()
    {
        return size;
    }

    /**
     * Returns a reference to the x-th <code>Card</code> of the <code>Deck</code>.
     *
     * @param x The id of the <code>Card</code> to retrieve. The accepted
     * values for x are in the range [0; size_of_deck - 1].
     *
     * @return The desired <code>Card</code>. If x is not in the required range, null is returned.
     */
    public final Card getCard(int x)
    {
        return ((x < 0 || x >= size) ? null : cards[x]);
    }

    /**
     * Searches for a <code>Card</code> inside the deck.
     *
     * @param c The <code>Card</code> to search for.
     *
     * @return The position in the <code>Deck</code> where the Card c is
     * found, -1 if c is not in the <code>Deck</code>.
     */
    public final int getCardIndex(Card c)
    {
        for (int i = 0; i < size; ++i) {
            if (cards[i].equals(c)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns a string representation of this <code>Deck</code>.
     *
     * @return a string representation of this <code>Deck</code>, i.e. all the cards in the <code>Deck</code>.
     * If the shuffle method was called, the cards will be in the shuffled order.
     */
    @Override
    public String toString()
    {
        StringBuilder rez = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            rez.append(cards[i].toString());
        }

        return rez.toString();
    }
}