package com.mithridat.nonoconverter.backend.solver;

import com.mithridat.nonoconverter.backend.Field;

import java.util.ArrayList;

/**
 * Nonogram storage class
 */
public class Nonogram {

    /**
     * Left and top nonogram grids
     */
    int[][] _left, _top;

    /**
     * Constructor by the nonogram field
     *
     * @param fieldNono - nonogram field
     */
    public Nonogram(Field fieldNono) {

    }

    /**
     * Method for comparing nonograms
     *
     * @param nono - another nonogram
     * @return true, if nonograms are equal
     *         false, otherwise
     */
    boolean isEqual(Nonogram nono) {
        return false;
    }

    /**
     * Method for getting number of left nonogram grid rows
     *
     * @return number of left nonogram grid rows
     */
    int getLeftRowsLength() {
        return _left.length;
    }

    /**
     * Method for getting number of top nonogram grid columns
     *
     * @return number of top nonogram grid columns
     */
    int getTopColsLength() {
        return _top.length;
    }

    /**
     * Method for getting left nonogram grid cell
     * with row number i and column number j
     *
     * @param i - row number
     * @param j - column number
     * @return left nonogram grid cell with row number i and column number j
     */
    int getLeftRowValue(int i, int j) {
        return _left[i][j];
    }

    /**
     * Method for getting top nonogram grid cell
     * with column number i and row number j
     *
     * @param i - column number
     * @param j - row number
     * @return top nonogram grid cell with column number i and row number j
     */
    int getTopColValue(int i, int j) {
        return _top[i][j];
    }

}
