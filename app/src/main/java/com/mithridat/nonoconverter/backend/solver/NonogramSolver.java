package com.mithridat.nonoconverter.backend.solver;

import android.os.AsyncTask;

import com.mithridat.nonoconverter.backend.Field;

import java.util.Arrays;
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
        boolean isChanged = true;
        init();
        while (!_asyncTask.isCancelled() && isChanged) {
            isChanged = applyAlgorithms(_rows, Field.ROW);
            _rows.clear();
            isChanged = applyAlgorithms(_cols, Field.COL) || isChanged;
            _cols.clear();
        }
        return check();
    }

    /**
     * Method for initialising class fields before solution start
     */
    void init() {
        int rows = _nono.getLeftRowsLength(), cols = _nono.getTopColsLength();
        _field = new Field(rows, cols);
        _left = new boolean[rows][];
        initBooleanArray(_left, Field.ROW);
        _top = new boolean[cols][];
        initBooleanArray(_top, Field.COL);
        _rows = new HashSet<>();
        initHashSet(_rows, 0, rows);
        _cols = new HashSet<>();
        initHashSet(_cols, 0, cols);
    }

    /**
     * Method for checking solution
     *
     * @return true, if solution is correct
     *         false, otherwise
     */
    boolean check() {
        return checkIsAllNumbersReady(_left)
                && checkIsAllNumbersReady(_top)
                && _nono.isEqual(new Nonogram(_field));
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

    /**
     * For every row or column with index in set applies algorithms
     *
     * @param set - set of indexes
     * @param type - ROW, if row
     *               COL, if column
     * @return true, if any cell was filled
     *         false, otherwise
     */
    private boolean applyAlgorithms(HashSet<Integer> set, int type) {
        boolean isFilled = false, isChanged;
        for (int i : set) {
            if (_asyncTask.isCancelled()) break;
            isChanged = true;
            while (isChanged) {
                isChanged = applySimpleBoxes(i, type);
                isChanged = applySimpleSpaces(i, type) || isChanged;
                isChanged = applyForcing(i, type) || isChanged;
                isChanged = applyGlue(i, type) || isChanged;
                isChanged = applyJoining(i, type) || isChanged;
                isChanged = applySplitting(i, type) || isChanged;
                isChanged = applyPunctuating(i, type) || isChanged;
                isChanged = applyDualPosition(i, type) || isChanged;
                isFilled = isChanged || isFilled;
            }
        }
        return isFilled;
    }

    /**
     * Method for initialising one of the two boolean arrays
     *
     * @param array - array for initialising
     * @param type - ROW, if _left
     *               COL, if _top
     */
    private void initBooleanArray(boolean[][] array, int type) {
        int length;
        for (int i = 0; i < array.length; i++) {
            length = type == Field.ROW
                    ? _nono.getLeftRowLength(i) : _nono.getTopColLength(i);
            array[i] = new boolean[length];
            Arrays.fill(array[i], false);
        }
    }

    /**
     * Method for initialising one of the two hash sets
     * with numbers from interval
     *
     * @param set - hash set for initialising
     * @param begin - first number from interval
     * @param end - next to last number from interval
     */
    private void initHashSet(HashSet<Integer> set, int begin, int end) {
        for (int i = begin; i < end; i++) {
            set.add(i);
        }
    }

    /**
     * Method for checking is all numbers are ready
     *
     * @param array - boolean array
     * @return true, if all numbers are ready
     *         false, otherwise
     */
    private boolean checkIsAllNumbersReady(boolean[][] array) {
        for(boolean[] a : array) {
            for(boolean b : a) {
                if(!b) return false;
            }
        }
        return true;
    }

}
