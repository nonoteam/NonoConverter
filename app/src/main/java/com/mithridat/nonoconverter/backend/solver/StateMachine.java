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
    private int _statesCount;

    /**
     * Constructor by the numbers from nonogram, count of possible input data,
     * delimiter value
     *
     * @param line - numbers from nonogram
     * @param count - count of possible input data
     * @param value - delimiter value
     */
    StateMachine(int[] line, int count, int value) {
        int len = line.length, sum = 0, k = 0;
        for (int i : line) {
            sum += i;
        }
        _statesCount = sum + len;
        if (_statesCount == 0) {
            _statesCount = 1;
            _table = new int[_statesCount][count];
            addState(0, new int[]{INVALID, 0});
        } else {
            _table = new int[_statesCount][count];
            for (int i = 0; i < len; i++) {
                k = addState(k, new int[]{k + 1, k});
                for (int j = 0; j + 1 < line[i]; j++) {
                    k = addState(k, new int[]{k + 1, INVALID});
                }
                if (i + 1 < len) {
                    k = addState(k, new int[]{INVALID, k + 1});
                } else {
                    k = addState(k, new int[]{INVALID, k});
                }
            }
        }
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
     * Method for adding state
     *
     * @param index - index of state
     * @param states - indexes of next states
     * @return index of state + 1
     */
    private int addState(int index, int[] states) {
        System.arraycopy(states, 0, _table[index], 0, states.length);
        return index + 1;
    }

}
