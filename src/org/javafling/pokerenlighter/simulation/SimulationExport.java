package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;

/**
 * Provides export functionality for simulation results.
 * 
 * @author Radu Murzea
 */
public class SimulationExport
{
    private static final String LINE_END = System.lineSeparator();
    
    private static final String TAB_CHAR = Character.toString('\t');
    
    /**
     * Constructs a XML representation of the simulation result and returns it in the form of
     * a String.
     * 
     * @param result the result of the simulation that must be converted to XML.
     * 
     * @return a String containing an XML representation of the provided result. If the result
     * is null, then an empty String is returned.
     */
    public static String getResultXMLString(SimulationFinalResult result)
    {
        if (result == null) {
            return "";
        }
        
        StringBuilder xml = new StringBuilder();
        
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>")
            .append(LINE_END)
            .append("<simulation>")
            .append(LINE_END)
            .append(getNestingCharacters(1))
            .append("<meta>")
            .append(LINE_END)
            .append(getNestingCharacters(2))
            .append("<playercount>")
            .append(result.getNrOfPlayers())
            .append("</playercount>")
            .append(LINE_END)
            .append(getNestingCharacters(2))
            .append("<rounds>")
            .append(result.getRounds ())
            .append("</rounds>")
            .append(LINE_END)
            .append(getNestingCharacters(2))
            .append("<type>")
            .append(result.getPokerType().toString())
            .append("</type>")
            .append(LINE_END)
            .append(getNestingCharacters(2))
            .append("<threads>")
            .append(result.getNrOfThreads())
            .append("</threads>")
            .append(LINE_END)
            .append(getNestingCharacters(2))
            .append("<duration unit=\"milliseconds\">")
            .append(result.getDuration())
            .append("</duration>")
            .append(LINE_END)
            .append(getNestingCharacters(1))
            .append("</meta>")
            .append(LINE_END)
            .append(getNestingCharacters(1))
            .append("<players>")
            .append(LINE_END);
        
        for (int i = 1; i <= result.getNrOfPlayers(); i++) {
            xml.append(getNestingCharacters(2))
                .append("<player id=\"")
                .append(i)
                .append("\">")
                .append(LINE_END)
                .append(getNestingCharacters(3));
            
            PlayerProfile profile = result.getPlayer(i - 1);
            
            xml.append("<handtype>");
            xml.append(profile.getHandType().toString());
            xml.append("</handtype>");
            xml.append(LINE_END);
            
            if (profile.getHandType() == HandType.EXACTCARDS) {
                Card[] cards = profile.getCards();
                for (Card card : cards) {
                    xml.append(getNestingCharacters(3))
                        .append("<card>")
                        .append(card.toString())
                        .append("</card>")
                        .append(LINE_END);
                }
            } else if (profile.getHandType() == HandType.RANGE) {
                Range range = profile.getRange();
                for (int row = 0; row < 13; row++) {
                    for (int col = 0; col < 13; col++) {
                        if (range.getValue(row, col)) {
                            xml.append(getNestingCharacters(3));
                            xml.append("<cardtype>");
                            xml.append(Range.rangeNames[row][col]);
                            xml.append("</cardtype>");
                            xml.append(LINE_END);
                        }
                    }
                }
            }
            
            xml.append(getNestingCharacters(2));
            xml.append("</player>");
            xml.append(LINE_END);
        }
        
        xml.append(getNestingCharacters(1));
        xml.append("</players>");
        xml.append(LINE_END);
        
        Card[] flop = result.getFlop();
        Card turn = result.getTurn();
        Card river = result.getRiver();
        
        if (flop != null || turn != null || river != null) {
            xml.append(getNestingCharacters(1));
            xml.append("<community>");
            xml.append(LINE_END);
            
            if (flop != null) {
                xml.append(getNestingCharacters(2))
                    .append("<flop id=\"1\">")
                    .append(flop[0].toString())
                    .append("</flop>")
                    .append(LINE_END)
                    .append(getNestingCharacters(2))
                    .append("<flop id=\"2\">")
                    .append(flop[1].toString())
                    .append("</flop>")
                    .append(LINE_END)
                    .append(getNestingCharacters(2))
                    .append("<flop id=\"3\">")
                    .append(flop[2].toString())
                    .append("</flop>")
                    .append(LINE_END);
            }
            
            if (turn != null) {
                xml.append(getNestingCharacters(2))
                    .append("<turn>")
                    .append(turn.toString())
                    .append("</turn>")
                    .append(LINE_END);
            }
            
            if (river != null) {
                xml.append(getNestingCharacters(2))
                    .append("<river>")
                    .append(river.toString())
                    .append("</river>")
                    .append(LINE_END);
            }
            
            xml.append(getNestingCharacters(1))
                .append("</community>")
                .append(LINE_END);
        }
        
        xml.append(getNestingCharacters(1))
            .append("<result>")
            .append(LINE_END);
        
        for (int i = 1; i <= result.getNrOfPlayers(); i++) {
            xml.append(getNestingCharacters(2))
                .append("<player id=\"")
                .append(i)
                .append("\">")
                .append(LINE_END)
                .append(getNestingCharacters(3))
                .append("<wins>")
                .append(result.getFormattedWinPercentage(i - 1))
                .append("</wins>")
                .append(LINE_END)
                .append(getNestingCharacters(3))
                .append("<loses>")
                .append(result.getFormattedLosePercentage(i - 1))
                .append("</loses>")
                .append(LINE_END)
                .append(getNestingCharacters(3))
                .append("<ties>")
                .append(result.getFormattedTiePercentage(i - 1))
                .append("</ties>")
                .append(LINE_END)
                .append(getNestingCharacters(2))
                .append("</player>")
                .append(LINE_END);
        }

        xml.append(getNestingCharacters(1))
            .append("</result>")
            .append(LINE_END)
            .append("</simulation>");
        
        return xml.toString();
    }
    
    //returns the specified number of tab characters as a String
    private static String getNestingCharacters(int nestedLevel)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestedLevel; i++) {
            sb.append(TAB_CHAR);
        }
        
        return sb.toString();
    }
}
