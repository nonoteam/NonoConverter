package com.mithridat.nonoconverter.backend.solver;

import android.os.AsyncTask;

import com.mithridat.nonoconverter.backend.Field;

import java.util.HashSet;

/**
 * Nonogram solving class
 */
public class NonogramSolver {

    /**
     * Show what numbers are ready
     */
    boolean[][] _left, _top;

    /**
     * Nonogram
     */
    Nonogram _nono;

    /**
     * Field
     */
    Field _field;

    /**
     * Async task of image converting
     */
    AsyncTask<Void, Void, Field> _asyncTask;

    /**
     * Show rows and columns in which there were changes during
     * the previous iteration of the nonogram solution
     */
    HashSet<Integer> _rows, _cols;

    /**
     * Constructor by the nonogram and async task of image converting
     *
     * @param nono - nonogram
     * @param asyncTask - async task of image converting
     */
    public NonogramSolver(
            Nonogram nono,
            AsyncTask<Void, Void, Field> asyncTask) {
        _nono = nono;
        _asyncTask = asyncTask;
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
     * Method for setting async task of image converting
     *
     * @param asyncTask - async task of image converting
     */
    public void setAsyncTask(AsyncTask<Void, Void, Field> asyncTask) {
        _asyncTask = asyncTask;
    }

    /**
     * Method for solving nonogram
     *
     * @return true, if solution was founded
     *         false, otherwise
     */
    public boolean solve() {
        if(!_asyncTask.isCancelled()) {
            return true;
        }
        return false;
    }

    /**
     * Method for initialising class fields before solution start
     */
    void init() {
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        int length;
        _field = new Field(rows, cols);
        _left = new boolean[rows][];
        for(int i = 0; i < rows; i++) {
            length = _nono.getLeftRowLength(i);
            _left[i] = new boolean[length];
            for(int j = 0; j < length; j++) {
                _left[i][j] = false;
            }
        }
        _top = new boolean[cols][];
        for(int i = 0; i < cols; i++) {
            length = _nono.getTopColLength(i);
            _top[i] = new boolean[length];
            for(int j = 0; j < length; j++) {
                _top[i][j] = false;
            }
        }
        _rows = new HashSet<>();
        for(int i = 0; i < rows; i++) {
            _rows.add(i);
        }
        _cols = new HashSet<>();
        for(int i = 0; i < cols; i++) {
            _cols.add(i);
        }
    }

    /**
     * Method for checking solution
     *
     * @return true, if solution is correct
     *         false, otherwise
     */
    boolean check() {
        return _nono.isEqual(new Nonogram(_field));
    }

    /**
     * Method for getting workspace in row or column:
     * - each row of result matrix contains start position and
     * next to last position
     * of cell and number blocks
     * - one row - one block
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return workspace
     */
    int[][] getWorkspace(int ind, int type) {
        return null;
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
     * Implementation of simple spaces algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applySimpleSpaces(int ind, int type) {
        return false;
    }

    /**
     * Implementation of forcing algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyForcing(int ind, int type) {
        return false;
    }

    /**
     * Implementation of glue algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyGlue(int ind, int type) {
        return false;
    }

    /**
     * Implementation of joining algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyJoining(int ind, int type) {
        return false;
    }

    /**
     * Implementation of splitting algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applySplitting(int ind, int type) {
        return false;
    }

    /**
     * Implementation of punctuating algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyPunctuating(int ind, int type) {
        return false;
    }

    /**
     * Implementation of dual position algorithm
     *
     * @param ind - index of row or column
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    boolean applyDualPosition(int ind, int type) {
        return false;
    }

}
