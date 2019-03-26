package com.mithridat.nonoconverter.backend;

import java.util.ArrayList;

/**
 * Nonogram storage class
 */
class Nonogram {

    /**
     * Left and top nonogram grids
     */
    private ArrayList<Integer>[] _left, _top;

    /**
     * Constructor by the nonogram field
     *
     * @param fieldNono - nonogram field
     */
    Nonogram(Field fieldNono) {

    }

    /**
     * Method for getting number of left nonogram grid rows
     *
     * @return number of left nonogram grid rows
     */
    public int getLeftRows() {
        return _left.length;
    }

    /**
     * Method for getting number of top nonogram grid columns
     *
     * @return number of top nonogram grid columns
     */
    public int getTopCols() {
        return _top.length;
    }

    /**
     * Method for getting left nonogram grid row with number i
     *
     * @param i - left nonogram grid row number
     * @return left nonogram grid row with number i
     */
    public ArrayList<Integer> getLeftRow(int i) {
        return _left[i];
    }

    /**
     * Method for getting top nonogram grid column with number i
     *
     * @param i - top nonogram grid column number
     * @return top nonogram grid column with number i
     */
    public ArrayList<Integer> getTopCol(int i) {
        return _top[i];
    }

}
