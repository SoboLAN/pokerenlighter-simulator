package org.javafling.pokerenlighter.combination;

public class ShortDeckCombination extends TexasCombination {

    /**
     * Constructs a ShortDeckCombination object.
     * <br />
     * <strong>WARNING:</strong> The constructor does not check for duplicate cards or NULL values.
     * To avoid unexpected behaviour, don't provide such input.
     *
     * @param c the cards. This array must contain 7 Card objects.
     */
    public ShortDeckCombination(Card[] c) {
        super(c);
    }

    @Override
    public String getCombination() {
        //check cache
        if (result != null) {
            return result;
        }

        String tmprank;

        if (isRoyalFlush ()) {
            result = "9";
            return result;
        }

        tmprank = getStraightFlush();

        if (! tmprank.equals("0")) {
            result = "8" + tmprank;
            return result;
        }

        tmprank = getQuad();

        if (! tmprank.equals ("0")) {
            result = "7" + tmprank;
            return result;
        }

        tmprank = getFlush();

        if (! tmprank.equals("0")) {
            result = "6" + tmprank;
            return result;
        }

        tmprank = getFullHouse();

        if (! tmprank.equals ("0")) {
            result = "5" + tmprank;
            return result;
        }

        tmprank = getStraight();

        if (! tmprank.equals("0")) {
            result = "4" + tmprank;
            return result;
        }

        tmprank = getThreeOfAKind();

        if (! tmprank.equals("0")) {
            result = "3" + tmprank;
            return result;
        }

        tmprank = getTwoPair();

        if (! tmprank.equals("0")) {
            result = "2" + tmprank;
            return result;
        }

        tmprank = getOnePair();

        if (! tmprank.equals("0")) {
            result = "1" + tmprank;
            return result;
        }

        //if none of the above is found, high card is the only one left
        result = "0" + getHighCard();

        return result;
    }

    @Override
    public String getStraight() {
        int[] ids = new int[7];
        int i, j, k;

        //use the ranks, it's easier
        for (i = 0; i < 7; ++i) {
            ids[i] = cards[i].getRank();
        }

        //eliminate duplicates (the big if statements at the end of the method will not work correctly if
        //there are duplicate cards)
        for (i = 0; i < 6; ++i) {
            ids[i] = (ids[i] == ids[i + 1]) ? 0 : ids[i];
        }

        // resort cards
        //duplicates, which are now = 0, will be moved in the back
        for (i = 0; i < 6; ++i) {
            for (j = i + 1; j < 7; ++j) {
                if (ids[i] < ids[j]) {
                    k = ids[i];
                    ids[i] = ids[j];
                    ids[j] = k;
                }
            }
        }

        //now let's search for straights

        if (ids[0] == ids[1] + 1 && ids[1] == ids[2] + 1 && ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1) {
            return Character.toString(Card.getCharCard(ids[0]));
        }

        if (ids[1] == ids[2] + 1 && ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1 && ids[4] == ids[5] + 1) {
            return Character.toString(Card.getCharCard(ids[1]));
        }

        if (ids[2] == ids[3] + 1 && ids[3] == ids[4] + 1 && ids[4] == ids[5] + 1 && ids[5] == ids[6]+ 1) {
            return Character.toString(Card.getCharCard(ids[2]));
        }

        // There is one aditional situation. That is the wheel
        if (ids[0] == 14) {
            if (ids[1] == 9 && ids[2] == 8 && ids[3] == 7 && ids[4] == 6) {
                return "5";
            }

            if (ids[2] == 9 && ids[3] == 8 && ids[4] == 7 && ids[5] == 6) {
                return "5";
            }

            if (ids[3] == 9 && ids[4] == 8 && ids[5] == 7 && ids[6] == 6) {
                return "5";
            }
        }

        return "0";
    }

    @Override
    public String getStraightFlush() {
        int[] tmp = new int[7];
        int i, j, k, tmp_dim;
        Card[] ids = new Card[7];
        Card aux;
        char domin_color;

        //both ranks and colors are needed... soooo basically the whole cards
        for (i = 0; i < 7; ++i) {
            ids[i] = cards[i];
        }

        //group cards by color
        for (i = 0; i < 6; ++i) {
            for (j = i + 1; j < 7; ++j) {
                if (ids[i].getColor() > ids[j].getColor()) {
                    aux = ids[i];
                    ids[i] = ids[j];
                    ids[j] = aux;
                }
            }
        }

        //create array of cards that have dominant color
        domin_color = ids[3].getColor();
        tmp_dim = 0;
        for (i = 0; i < 7; ++i) {
            if (ids[i].getColor() == domin_color) {
                tmp[tmp_dim] = ids[i].getRank();
                ++tmp_dim;
            }
        }

        //if there are less than 5 cards of the same color, there is no straight flush
        if (tmp_dim < 5) {
            return "0";
        }

        //sort the cards in descending order
        for (i = 0; i < tmp_dim - 1; ++i) {
            for (j = i + 1; j < tmp_dim; ++j) {
                if (tmp[i] < tmp[j]) {
                    k = tmp[i];
                    tmp[i] = tmp[j];
                    tmp[j] = k;
                }
            }
        }

        //search for straights among the cards with dominant color

        if (tmp[0] - 1 == tmp[1] && tmp[1] - 1 == tmp[2] && tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4]) {
            return Character.toString(Card.getCharCard(tmp[0]));
        }

        if (tmp_dim > 5) {
            if (tmp[1] - 1 == tmp[2] && tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4] && tmp[4] - 1 == tmp[5]) {
                return Character.toString(Card.getCharCard(tmp[1]));
            }
        }

        if (tmp_dim > 6) {
            if (tmp[2] - 1 == tmp[3] && tmp[3] - 1 == tmp[4] && tmp[4] - 1 == tmp[5] && tmp[5] - 1 == tmp[6]) {
                return Character.toString(Card.getCharCard(tmp[2]));
            }
        }

        // A special case is the straight flush composed of A, 6, 7, 8, 9.
        if (tmp[0] == 14 && tmp[tmp_dim - 4] == 9 && tmp[tmp_dim - 3] == 8 && tmp[tmp_dim - 2] == 7 && tmp[tmp_dim - 1] == 6) {
            return "5";
        }

        return "0";
    }
}
