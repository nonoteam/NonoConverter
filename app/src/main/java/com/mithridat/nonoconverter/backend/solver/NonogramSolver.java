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
        init();
        applyAlgorithmSB(Field.ROW);
        applyAlgorithmSB(Field.COL);
        while (!_asyncTask.isCancelled()) {
            if (!applyAlgorithmFSM(_rows, Field.ROW)) return false;
            _rows.clear();
            if (!applyAlgorithmFSM(_cols, Field.COL)) return false;
            _cols.clear();
            if(_nono.checkCorrectness()) return true;
        }
        return _nono.checkCorrectness();
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
        _rowsFSM = new StateMachine[rows];
        initFSM(_rowsFSM, Field.ROW);
        _colsFSM = new StateMachine[cols];
        initFSM(_colsFSM, Field.COL);
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
     * @return false, if this nonogram hasn't solution
     *         true, otherwise
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
        for (int i = 0; i < _nono.getFirstLength(type); i++) {
            int length = _nono.getSecondLength(i, type);
            int[] line = new int[length];
            for (int j = 0; j < length; j++) {
                line[j] = _nono.getValue(i, j, type);
            }
            array[i] = new StateMachine(line);
        }
    }

}
