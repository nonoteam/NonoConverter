package com.mithridat.nonoconverter.backend.solver;

import static com.mithridat.nonoconverter.backend.nonogram.Field.EMPTY;

/**
 * Class of the cell of matrix that uses in the algorithm
 * with finite-state machine
 */
class Cell {

    /**
     * Diagonal direction
     */
    static final int DIAG = 0;

    /**
     * Horizontal direction
     */
    static final int HOR = 1;

    /**
     * Invalid direction
     */
    static final int INVALID = 1;

    /**
     * Array that contains all directions with their values
     * for concrete cell of the matrix.
     * One direction with value is int[2], where int[0] is direction
     * and int[1] is value (value = index of color).
     */
    private int[][] _dirs;

    /**
     * Constructor without parameters
     */
    Cell() {
        _dirs = new int[0][2];
    }

    /**
     * Method for adding new direction with value for it
     *
     * @param dir - direction
     * @param val - value
     */
    void addDirection(int dir, int val) {
        int len = _dirs.length;
        int[][] tmp = _dirs;
        _dirs = new int[len + 1][2];
        for (int i = 0; i < len; i++) {
            System.arraycopy(tmp[i], 0, _dirs[i], 0, 2);
        }
        _dirs[len][0] = dir;
        _dirs[len][1] = val;
    }

    /**
     * Method to check if values of all directions are equals
     *
     * @return true, if values of all directions are equals
     *         false, otherwise and if there is no directions
     */
    boolean isEqualVals() {
        if (_dirs.length == 0) return false;
        for (int i = 1; i < _dirs.length; i++) {
            if (_dirs[0][1] != _dirs[i][1]) return false;
        }
        return true;
    }

    /**
     * Method for getting count of directions
     *
     * @return count of the directions
     */
    int getDirectionsCount() {
        return _dirs.length;
    }

    /**
     * Method for getting direction by index
     *
     * @param ind - index of the direction
     * @return INVALID, if `ind` is less than zero or over than count of dirs
     *         direction with index `ind`, otherwise
     */
    int getDirection(int ind) {
        if (ind < 0 || ind >= _dirs.length) return INVALID;
        return _dirs[ind][0];
    }

    /**
     * Method for getting value of the direction by index
     *
     * @param ind - index of the direction
     * @return EMPTY, if `ind` is less than zero or over than count of dirs
     *         value of the direction with index `ind`, otherwise
     */
    int getValue(int ind) {
        if (ind < 0 || ind >= _dirs.length) return EMPTY;
        return _dirs[ind][1];
    }

}
