package com.mithridat.nonoconverter.backend;

import java.util.ArrayList;

/**
 * Nonogram solving class
 */
class NonogramSolver {

    /**
     * Show what numbers are ready
     */
    private ArrayList<Boolean> _left, _top;

    /**
     * Nonogram
     */
    private Nonogram _nono;

    /**
     * Field
     */
    private Field _field;

    /**
     * Constructor by the nonogram
     *
     * @param nono - nonogram
     */
    NonogramSolver(Nonogram nono) {
        _nono = nono;
    }

    /**
     * Method for setting nonogram
     *
     * @param nono - nonogram
     */
    public void setNonogram(Nonogram nono) {
        _nono = nono;
    }

    /**
     * Method for solving nonogram
     *
     * @return true, if solution was founded
     *         false, otherwise
     */
    public boolean solve() {

    }

    /**
     * Method for initialising class fields before solution start
     */
    private void init() {

    }

    /**
     * Method for checking solution
     *
     * @return true, if solution is correct
     *         false, otherwise
     */
    private boolean check() {

    }

    /**
     * Implementation of simple boxes algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applySimpleBoxes(int ind, int type) {

    }

    /**
     * Implementation of simple spaces algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applySimpleSpaces(int ind, int type) {

    }

    /**
     * Implementation of forcing algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyForcing(int ind, int type) {

    }

    /**
     * Implementation of glue algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyGlue(int ind, int type) {

    }

    /**
     * Implementation of joining and splitting algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyJoiningAndSplitting(int ind, int type) {

    }

    /**
     * Implementation of punctuating algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyPunctuating(int ind, int type) {

    }

    /**
     * Implementation of mercury algorithm
     *
     * @param ind - index of row or column
     * @param type - row or column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyMercury(int ind, int type) {

    }

}
