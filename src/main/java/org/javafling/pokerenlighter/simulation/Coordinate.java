package org.javafling.pokerenlighter.simulation;

/**
 * Representation for a x-y coordinate, used for the ranges functionality.
 * @author Radu Murzea
 */
class Coordinate
{
    //the row (x) of the coordinate
    public int x;
    
    //the column (y) of the coordinate
    public int y;
    
    /**
     * Constructor
     * @param x the row
     * @param y the column
     */
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}