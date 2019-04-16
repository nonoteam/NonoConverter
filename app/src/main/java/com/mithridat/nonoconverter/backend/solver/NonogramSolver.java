package com.mithridat.nonoconverter.backend.solver;

import android.os.AsyncTask;

import com.mithridat.nonoconverter.backend.nonogram.Field;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import java.util.HashSet;

/**
 * Nonogram solving class
 */
public class NonogramSolver {

    /**
     * Nonogram
     */
    Nonogram _nono;

    /**
     * Async task of image converting
     */
    AsyncTask<Void, Void, Nonogram> _asyncTask;

    /**
     * Show rows and columns in which there were changes during
     * the previous iteration of the nonogram solution
     */
    HashSet<Integer> _rows, _cols;

    /**
     * Set for storage numbers of states of state machine
     * for algorithm that uses finite-state machine
     */
    HashSet<Integer> _states;

    /**
     * Finite-state machines for nonogram (for rows and columns)
     */
    StateMachine[] _rowsFSM, _colsFSM;

    /**
     * Constructor without parameters
     */
    public NonogramSolver()
    {}

    /**
     * Method for setting nonogram
     *
     * @param nono - nonogram
     */
    public void setNonogram(Nonogram nono) {
        _nono = nono;
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
        boolean isChanged = true;
        init();
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        for (int i = 0; i < rows; i++) {
            applySimpleBoxes(i, Field.ROW);
        }
        for (int i = 0; i < cols; i++) {
            applySimpleBoxes(i, Field.COL);
        }
        while (!_asyncTask.isCancelled() && isChanged) {
            isChanged = applyAlgorithm(_rows, Field.ROW);
            _rows.clear();
            isChanged = applyAlgorithm(_cols, Field.COL) || isChanged;
            _cols.clear();
        }
        return false;
    }

    /**
     * Method for initialising class fields before solution start
     */
    void init() {
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        _rows = new HashSet<>();
        addIntervalHashSet(_rows, 0, rows);
        _cols = new HashSet<>();
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
    boolean applySimpleBoxes(int ind, int type) {
        return false;
    }

    /**
     * Implementation of nonogram solving algorithm using finite-state machine
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
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
     * @return true, if any cell was filled
     *         false, otherwise
     */
    private boolean applyAlgorithm(HashSet<Integer> set, int type) {
        boolean isFilled = false, isChanged;
        for (int i : set) {
            if (_asyncTask.isCancelled()) break;
            isChanged = true;
            while (isChanged) {
                isChanged = applyStateMachine(i, type);
                isFilled = isChanged || isFilled;
            }
        }
        return isFilled;
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

}
