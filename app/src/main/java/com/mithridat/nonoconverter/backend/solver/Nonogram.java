package com.mithridat.nonoconverter.backend.solver;

import androidx.annotation.NonNull;
import com.mithridat.nonoconverter.backend.Colors;
import com.mithridat.nonoconverter.backend.Field;

import java.util.Arrays;

/**
 * Nonogram storage class
 */
public class Nonogram {

    /**
     * Left and top nonogram grids
     */
    private int[][] _left, _top;

    /**
     * Constructor by the nonogram field
     *
     * @param fieldNono - nonogram field
     */
    public Nonogram(@NonNull Field fieldNono) {
        int w = fieldNono.getCols(), h = fieldNono.getRows();

        _left = new int[h][];
        _top = new int[w][];

        fillNonogramGrid(_left, Field.ROW, h, w, fieldNono);
        fillNonogramGrid(_top, Field.COL, w, h, fieldNono);
    }

    /**
     * Method for comparing nonograms
     *
     * @param nono - another nonogram
     * @return true, if nonograms are equal
     *         false, otherwise
     */
    boolean isEqual(@NonNull Nonogram nono) {
        return Arrays.deepEquals(_left, nono._left)
                & Arrays.deepEquals(_top, nono._top);
    }

    /**
     * Method for getting number of left nonogram grid rows
     *
     * @return number of left nonogram grid rows
     */
    int getLeftRowsLength() {
        return _left.length;
    }

    /**
     * Method for getting number of top nonogram grid columns
     *
     * @return number of top nonogram grid columns
     */
    int getTopColsLength() {
        return _top.length;
    }

    /**
     * Method for getting number of columns
     * in left nonogram grid row with number i
     *
     * @param i - row number
     * @return number of columns
     */
    int getLeftRowsLength(int i) {
        return _left[i].length;
    }

    /**
     * Method for getting number of rows
     * in top nonogram grid column with number i
     *
     * @param i - column number
     * @return number of rows
     */
    int getTopColsLength(int i) {
        return _top[i].length;
    }

    /**
     * Method for getting left nonogram grid cell
     * with row number i and column number j
     *
     * @param i - row number
     * @param j - column number
     * @return left nonogram grid cell with row number i and column number j
     */
    int getLeftRowValue(int i, int j) {
        return _left[i][j];
    }

    /**
     * Method for getting top nonogram grid cell
     * with column number i and row number j
     *
     * @param i - column number
     * @param j - row number
     * @return top nonogram grid cell with column number i and row number j
     */
    int getTopColValue(int i, int j) {
        return _top[i][j];
    }

    /**
     * Method for filling one of the two grids from field
     *
     * @param grid - grid for filling
     * @param type - ROW, if _left
     *               COL, if _top
     * @param outLim - limit for counter in outer loop
     * @param inLim - limit for counter in inner loop
     * @param field - nonogram field
     */
    private static void fillNonogramGrid(
            int[][] grid,
            int type,
            int outLim,
            int inLim,
            Field field) {
        for (int i = 0; i < outLim; ++i) {
            grid[i] = new int[0];
            for (int j = 0, k, length = 0; j < inLim; ) {
                k = field.getAntoherColorIndex(i, j, 1, type);
                int color = type == Field.ROW ? field.getColor(i, j)
                        : field.getColor(j, i);
                if (color == Colors.BLACK) {
                    int[] tmp = new int[length + 1];
                    System.arraycopy(grid[i], 0, tmp, 0, length);
                    tmp[length] = k - j;
                    length++;
                    grid[i] = tmp;
                }
                j = k;
            }
        }
    }

}
