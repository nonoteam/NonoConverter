package com.mithridat.nonoconverter.backend.solver;

/**
 * Representation of the finite-state machine. It contains state table
 */
class StateMachine {

    /**
     * Invalid state
     */
    static final int INVALID = -1;

    /**
     * State table. Rows - states, columns - input data - indexes of colors.
     */
    private int[][] _table;

    /**
     * Count of states
     */
    private int _states;

    /**
     * Constructor by the numbers from nonogram
     *
     * @param line - numbers from nonogram
     */
    StateMachine(int[] line) {

    }

    /**
     * Method for getting next state we can go to from current state,
     * if input value is input
     *
     * @param state - current state
     * @param input - input value
     * @return - next state
     */
    int getNextState(int state, int input) {
        return INVALID;
    }

    /**
     * Method for getting count of states
     *
     * @return count of states
     */
    int getStates() {
        return _states;
    }

}
