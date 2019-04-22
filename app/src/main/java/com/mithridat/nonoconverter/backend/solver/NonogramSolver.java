package com.mithridat.nonoconverter.backend.solver;

import android.os.AsyncTask;

import androidx.annotation.VisibleForTesting;
import com.mithridat.nonoconverter.backend.nonogram.Field;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import java.util.HashSet;

import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;
import static com.mithridat.nonoconverter.backend.nonogram.Field.EMPTY;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;
import static com.mithridat.nonoconverter.backend.nonogram.Field.COL;
import static com.mithridat.nonoconverter.backend.nonogram.Field.WHITE;
import static com.mithridat.nonoconverter.backend.solver.StateMachine.INVALID;
import static com.mithridat.nonoconverter.backend.solver.Cell.DIAG;
import static com.mithridat.nonoconverter.backend.solver.Cell.HOR;

/**
 * Nonogram solving class
 */
public class NonogramSolver {

    /**
     * Nonogram
     */
    private Nonogram _nono;

    /**
     * Async task of image converting
     */
    private AsyncTask<Void, Void, Nonogram> _asyncTask;

    /**
     * Show rows and columns in which there were changes during
     * the previous iteration of the nonogram solution
     */
    private HashSet<Integer> _rows, _cols;

    /**
     * Sets for storage numbers of states of state machine
     * for algorithm that uses finite-state machine
     */
    private HashSet<Integer> _statesPrev, _statesNext;

    /**
     * Finite-state machines for nonogram (for rows and columns)
     */
    private StateMachine[] _rowsFSM, _colsFSM;

    /**
     * Constructor without parameters
     */
    public NonogramSolver() {
        _rows = new HashSet<>();
        _cols = new HashSet<>();
        _statesPrev = new HashSet<>();
        _statesNext = new HashSet<>();
    }

    /**
     * Method for setting nonogram
     *
     * @param nono - nonogram
     */
    public void setNonogram(Nonogram nono) {
        _nono = nono;
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        _rowsFSM = new StateMachine[rows];
        initFSM(_rowsFSM, ROW);
        _colsFSM = new StateMachine[cols];
        initFSM(_colsFSM, COL);
    }

    /**
     * Method for setting async task of image converting
     *
     * @param asyncTask - async task of image converting
     */
    public void setAsyncTask(AsyncTask<Void, Void, Nonogram> asyncTask) {
        _asyncTask = asyncTask;
    }

    /**
     * Method for solving nonogram
     *
     * @return true, if solution was founded
     *         false, otherwise
     */
    public boolean solve() {
        init();
        applyAlgorithmSB(ROW);
        applyAlgorithmSB(COL);
        while (!_asyncTask.isCancelled()) {
            if (!applyAlgorithmFSM(_rows, ROW)) return false;
            _rows.clear();
            if (!applyAlgorithmFSM(_cols, COL)) return false;
            _cols.clear();
            if (_rows.isEmpty()) break;
        }
        if (_asyncTask.isCancelled()) return false;
        return _nono.checkCorrectness();
    }

    /**
     * Method for getting nonogram
     *
     * @return nonogram
     */
    @VisibleForTesting
    Nonogram getNonogram() {
        return _nono;
    }

    /**
     * Method for getting _rows or _cols
     *
     * @param type - ROW, if _rows
     *               COL, if _cols
     * @return _rows or _cols
     */
    @VisibleForTesting
    HashSet<Integer> getSetWithChanges(int type) {
        if (type == ROW) return _rows;
        return _cols;
    }

    /**
     * Method for getting _statesPrev
     *
     * @return _statesPrev
     */
    @VisibleForTesting
    HashSet<Integer> getStatesPrev() {
        return _statesPrev;
    }

    /**
     * Method for getting _statesNext
     *
     * @return _statesNext
     */
    @VisibleForTesting
    HashSet<Integer> getStatesNext() {
        return _statesNext;
    }

    /**
     * Method for getting finite-state machine for nonogram
     * (for rows or columns)
     *
     * @param ind - index of finite-state machine
     * @param type - ROW, if for rows
     *               COL, if for columns
     * @return finite-state machines for nonogram (for rows or columns)
     */
    @VisibleForTesting
    StateMachine getFSM(int ind, int type) {
        StateMachine[] machines = getFSM(type);
        if (ind < 0 || ind >= machines.length) return null;
        return machines[ind];
    }

    /**
     * Method for initialising class fields before solution start
     */
    @VisibleForTesting
    void init() {
        /*
         *TODO: add `_nono.clearField();` when solver will be ready
         */
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        _rows.clear();
        addIntervalHashSet(_rows, 0, rows);
        _cols.clear();
        addIntervalHashSet(_cols, 0, cols);
    }

    /**
     * Implementation of simple boxes algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    @VisibleForTesting
    boolean applySimpleBoxes(int ind, int type) {
        int max = 0, count = _nono.getSecondLength(ind, type), len = count - 1;
        int delta = 0, lineLength = _nono.getField().getLength(type);
        int value = 0;
        // HashSet<Integer> set = null;
        Field field = null;
        if (count == 0)
            return false;
        max = _nono.getValue(ind, 0, type);
        len += max;
        for (int i = 1; i < count; ++i) {
            value = _nono.getValue(ind, i, type);
            len += value;
            if (value > max) max = value;
        }
        delta = lineLength - len;
        if (delta < 0 || delta >= max) return false;
        // set = type == ROW ? _rows : _cols;
        field = _nono.getField();
        for (int i = 0, j = 0; i < count; ++i) {
            value = _nono.getValue(ind, i, type);
            // addIntervalHashSet(set, j + delta, j + value);
            for (int k = j + delta; k < j + value; ++k) {
                field.setColor(ind, k, field.getColorState(BLACK), type);
            }
            j += value + 1;
        }
        return true;
    }

    /**
     * Method for swapping _statesPrev and _statesNext
     */
    @VisibleForTesting
    void swapStateSets() {
        HashSet<Integer> tmp = _statesNext;
        _statesNext = _statesPrev;
        _statesPrev = tmp;
    }

    /**
     * Implementation of the one step (processing one cell) of the forward move
     * on the line of the solving algorithm using finite-state machine
     *
     * @param matrix - matrix for algorithm that uses finite-state machine
     * @param machine - finite-state machine of the line
     * @param color - index of color for cell of the line
     * @param step - index of the cell in the line
     */
    @VisibleForTesting
    void applyStateMachineForwardStep(
            Cell[][] matrix,
            StateMachine machine,
            int color,
            int step) {
        int stateNext = 0, dir = HOR;
        for (int state : _statesPrev) {
            stateNext = machine.getNextState(state, color);
            if (stateNext != INVALID) {
                if (matrix[stateNext][step] == null) {
                    matrix[stateNext][step] = new Cell();
                }
                dir = stateNext == state ? HOR : DIAG;
                matrix[stateNext][step].addDirection(dir, color);
                _statesNext.add(stateNext);
            }
        }
    }

    /**
     * Implementation of forward move on the line of the solving algorithm
     * using finite-state machine
     *
     * @param ind - index of the line
     * @param type - type of the line
     * @param matrix - matrix for algorithm that uses finite-state machine
     */
    @VisibleForTesting
    void applyStateMachineForward(int ind, int type, Cell[][] matrix) {
        StateMachine machine = getFSM(ind, type);
        Field field = _nono.getField();
        int length = field.getLength(type), color = EMPTY;
        _statesPrev.clear();
        _statesNext.clear();
        _statesPrev.add(0);
        for (int i = 0; i < length && !_statesPrev.isEmpty(); ++i) {
            color = field.getColor(ind, i, type);
            if (color == EMPTY) {
                for (int j = 0; j < field.getColorsCount(); ++j) {
                    applyStateMachineForwardStep(matrix, machine, j, i);
                }
            } else {
                applyStateMachineForwardStep(matrix, machine, color, i);
            }
            _statesPrev.clear();
            swapStateSets();
        }
    }

    /**
     * Implementation of nonogram solving algorithm using finite-state machine
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return false, if this nonogram hasn't solution
     *         true, otherwise
     */
    @VisibleForTesting
    boolean applyStateMachine(int ind, int type) {
        return false;
    }

    /**
     * For every row or column with index in set applies algorithm
     * using finite-state machine
     *
     * @param set - set of indexes
     * @param type - ROW, if row
     *               COL, if column
     * @return false, if this nonogram hasn't solution
     *         true, otherwise
     */
    private boolean applyAlgorithmFSM(HashSet<Integer> set, int type) {
        for (int i : set) {
            if (_asyncTask.isCancelled()) break;
            if (!applyStateMachine(i, type)) return false;
        }
        return true;
    }

    /**
     * For every row or column applies algorithm simple boxes
     *
     * @param type - ROW, if row
     *               COL, if column
     */
    private void applyAlgorithmSB(int type) {
        int length = _nono.getFirstLength(type);
        for (int i = 0; i < length; i++) {
            if (_asyncTask.isCancelled()) break;
            applySimpleBoxes(i, type);
        }
    }

    /**
     * Method for adding numbers from interval to hash set
     *
     * @param set - hash set
     * @param begin - first number from interval
     * @param end - next to last number from interval
     */
    private void addIntervalHashSet(HashSet<Integer> set, int begin, int end) {
        for (int i = begin; i < end; i++) {
            set.add(i);
        }
    }

    /**
     * Method for initialising _rowsFSM or _colsFSM
     *
     * @param array - _rowsFSM or _colsFSM
     * @param type - ROW, if _rowsFSM
     *               COL, if _colsFSM
     */
    private void initFSM(StateMachine[] array, int type) {
        int delim = _nono.getField().getColorState(WHITE);
        int length;
        for (int i = 0; i < _nono.getFirstLength(type); i++) {
            length = _nono.getSecondLength(i, type);
            int[][] line = new int[length][2];
            for (int j = 0; j < length; j++) {
                line[j][0] = _nono.getValue(i, j, type);
                line[j][1] = _nono.getField().getColorState(BLACK);
            }
            array[i] = new StateMachine(line, 2, delim);
        }
    }

    /**
     * Method for getting finite-state machines for nonogram
     * (for rows or columns)
     *
     * @param type - ROW, if for rows
     *               COL, if for columns
     * @return finite-state machines for nonogram (for rows or columns)
     */
    private StateMachine[] getFSM(int type) {
        if (type == ROW) return _rowsFSM;
        return _colsFSM;
    }

}
