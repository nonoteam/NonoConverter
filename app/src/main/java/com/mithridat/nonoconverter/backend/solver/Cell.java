package com.mithridat.nonoconverter.backend.solver;

/**
 * Class of the cell of matrix that uses in the algorithm
 * with finite-state machine
 */
class Cell {

    /**
     * Array that contains all directions with their values
     * for concrete cell of the matrix.
     * One direction with value is int[2], where int[0] is direction
     * and int[1] is value (value = color or index of color).
     */
    private int[][] _dirs;

    /**
     * Constructor without parameters
     */
    Cell() {

    }

    /**
     * Method for adding new direction with value for it
     *
     * @param dir - direction
     * @param val - value
     */
    void addDirection(int dir, int val) {

    }

    /**
     * Method to check if all directions are equals
     *
     * @return true, if all directions are equals
     *         false, otherwise and if there is no directions
     */
    boolean isEqualVals() {
        return false;
    }

    /**
     * Method for getting array of directions with their values
     *
     * @return array of directions with their values
     */
    int[] getDirs() {
        return null;
    }

}
