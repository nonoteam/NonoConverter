package com.mithridat.nonoconverter.backend.solver;

import java.util.Arrays;

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
    private int _statesCount;

    /**
     * Constructor by the numbers from nonogram, count of colors,
     * index of delimiter color
     *
     * @param line - numbers from nonogram
     *        line[][0] - length of block
     *        line[][1] - index of color
     * @param count - count of colors
     * @param delim - index of delimiter color
     */
    StateMachine(int[][] line, int count, int delim) {
        int len = line.length, state = 0;
        _statesCount = 0;
        for (int i = 0; i < len; i++) {
            _statesCount += i;
            if (i + 1 < len && line[i][1] == line[i + 1][1]) _statesCount++;
        }
        _statesCount++;
        _table = new int[_statesCount][count];
        for (int i = 0; i < len; i++) {
            state = addLoop(state, line[i][1], delim);
            for (int j = 0; j + 1 < line[i][0]; j++) {
                state = addColorArrow(state, line[i][1]);
            }
            if (i + 1 < len && line[i][1] == line[i + 1][1]) {
                state = addColorArrow(state, delim);
            }
        }
        state = addLastLoop(state, delim);
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
        if (state < 0 || state >= _statesCount) return INVALID;
        if (input < 0 || input >= _table[state].length) return INVALID;
        return _table[state][input];
    }

    /**
     * Method for getting count of states
     *
     * @return count of states
     */
    int getStatesCount() {
        return _statesCount;
    }

    /**
     * Method for adding loop (not last loop)
     *
     * @param state - current state
     * @param color - index of next block color
     * @param delim - index of delimiter color
     * @return state + 1
     */
    private int addLoop(int state, int color, int delim) {
        Arrays.fill(_table[state], INVALID);
        _table[state][color] = state + 1;
        _table[state][delim] = state;
        return state + 1;
    }

    /**
     * Method for adding color arrow
     *
     * @param state - current state
     * @param color - index of color
     * @return state + 1
     */
    private int addColorArrow(int state, int color) {
        Arrays.fill(_table[state], INVALID);
        _table[state][color] = state + 1;
        return state + 1;
    }

    /**
     * Method for adding last loop
     *
     * @param state - current state
     * @param delim - index of delimiter color
     * @return state + 1
     */
    private int addLastLoop(int state, int delim) {
        Arrays.fill(_table[state], INVALID);
        _table[state][delim] = state;
        return state + 1;
    }

}
