package com.mithridat.nonoconverter.backend.nonogram;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;

import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;
import static com.mithridat.nonoconverter.backend.nonogram.Field.COL;
import static com.mithridat.nonoconverter.backend.nonogram.Field.EMPTY;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;

/**
 * Nonogram storage class
 */
public class Nonogram implements Parcelable {

    /**
     * Left and top nonogram grids
     */
    private int[][] _left, _top;

    /**
     * Field of the puzzle
     */
    private Field _field;

    /**
     * Constructor by the nonogram field
     *
     * @param fieldNono - nonogram field
     */
    private Nonogram(@NonNull Field fieldNono) {
        _field = fieldNono;
        initGrid();
    }

    /**
     * Constructor by black-and-white image
     *
     * @param bmp - black-and-white image
     * @param rows - number of thumbnail rows
     * @param cols - number of thumbnail columns
     * @param p - fill parameter
     */
    public Nonogram(Bitmap bmp, int rows, int cols, int p) {
        _field = new Field(bmp, rows, cols, p);
        initGrid();
    }

    /**
     * Private constructor without parameters
     */
    private Nonogram() { }

    /**
     * Constructor by the parcel
     *
     * @param source - parcel
     */
    private Nonogram(Parcel source) {
        _field = new Field(source);
        initGrid();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        _field.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<Nonogram> CREATOR =

            new Parcelable.Creator<Nonogram>() {

                @Override
                public Nonogram createFromParcel(Parcel source) {
                    return new Nonogram(source);
                }

                @Override
                public Nonogram[] newArray(int size) {
                    return new Nonogram[size];
                }

            };

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
    public int getLeftRowsLength() {
        return _left.length;
    }

    /**
     * Method for getting number of top nonogram grid columns
     *
     * @return number of top nonogram grid columns
     */
    public int getTopColsLength() {
        return _top.length;
    }

    /**
     * Method for getting length of _left or _top
     *
     * @param type - ROW, if _left
     *               COL, if _top
     * @return length of _left or _top
     */
    public int getFirstLength(int type) {
        if (type == ROW) return getLeftRowsLength();
        return getTopColsLength();
    }

    /**
     * Method for getting number of columns
     * in left nonogram grid row with number i
     *
     * @param i - row number
     * @return number of columns
     */
    public int getLeftRowLength(int i) {
        return _left[i].length;
    }

    /**
     * Method for getting number of rows
     * in top nonogram grid column with number i
     *
     * @param i - column number
     * @return number of rows
     */
    public int getTopColLength(int i) {
        return _top[i].length;
    }

    /**
     * Method for getting length of _left[i] or _top[i]
     *
     * @param i - i
     * @param type - ROW, if _left
     *               COL, if _top
     * @return length of _left[i] or _top[i]
     */
    public int getSecondLength(int i, int type) {
        if (type == ROW) return getLeftRowLength(i);
        return getTopColLength(i);
    }

    /**
     * Method for getting left nonogram grid cell
     * with row number i and column number j
     *
     * @param i - row number
     * @param j - column number
     * @return left nonogram grid cell with row number i and column number j
     */
    public int getLeftRowValue(int i, int j) {
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
    public int getTopColValue(int i, int j) {
        return _top[i][j];
    }

    /**
     * Method for getting _left[i][j] or _top[i][j]
     *
     * @param i - i
     * @param j - j
     * @param type - ROW, if _left
     *               COL, if _top
     * @return _left[i][j] or _top[i][j]
     */
    public int getValue(int i, int j, int type) {
        if (type == ROW) return getLeftRowValue(i, j);
        return getTopColValue(i, j);
    }

    /**
     * Method for checking correctness of filling of the field
     *
     * @return true, if filling of the field is correct
     *         false, otherwise
     */
    public boolean checkCorrectness() {
        int rows = _field.getRows(), cols = _field.getCols();
        Field field = _field.copy();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (field.getColor(i, j) == EMPTY) {
                    field.setColor(i, j, 0);
                }
            }
        }
        return checkNonogramGrid(field, _left, ROW, rows, cols) &&
                checkNonogramGrid(field, _top, COL, cols, rows);
    }

    /**
     * Method for getting field of the puzzle
     *
     * @return field of the puzzle
     */
    public Field getField() {
        return _field;
    }

    /**
     * Method for clearing field of the puzzle
     */
    public void clearField() {
        _field.clear();
    }

    /**
     * Method for getting new nonogram that contains field with colors in cells
     *
     * @return new nonogram that contains field with colors in cells
     */
    public Nonogram translateToColors() {
        return new Nonogram(_field.translateToColors());
    }

    /**
     * Method for initialising grid
     */
    private void initGrid() {
        int w = _field.getCols(), h = _field.getRows();

        _left = new int[h][];
        _top = new int[w][];

        fillNonogramGrid(_left, ROW, h, w);
        fillNonogramGrid(_top, COL, w, h);
    }

    /**
     * Method for filling one of the two grids from field
     *
     * @param grid - grid for filling
     * @param type - ROW, if _left
     *               COL, if _top
     * @param outLim - limit for counter in outer loop
     * @param inLim - limit for counter in inner loop
     */
    private void fillNonogramGrid(
            int[][] grid,
            int type,
            int outLim,
            int inLim) {
        for (int i = 0; i < outLim; ++i) {
            grid[i] = new int[0];
            for (int j = 0, k, length = 0; j < inLim; ) {
                k = _field.getAnotherColorIndex(i, j, 1, type);
                if (_field.getColor(i, j, type) ==
                        _field.getColorState(BLACK)) {
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

    /**
     * Method for checking one of the two grids
     *
     * @param field - field
     * @param grid - grid
     * @param type - ROW, if _left
     *               COL, if _top
     * @param outLim - limit for counter in outer loop
     * @param inLim - limit for counter in inner loop
     * @return true, if grid is correct
     *         false, otherwise
     */
    private boolean checkNonogramGrid(
            Field field,
            int[][] grid,
            int type,
            int outLim,
            int inLim) {
        for (int i = 0; i < outLim; ++i) {
            int length = 0;
            for (int j = 0, k; j < inLim; ) {
                k = field.getAnotherColorIndex(i, j, 1, type);
                if (field.getColor(i, j, type) == field.getColorState(BLACK)) {
                    if (grid[i][length] != k - j) return false;
                    length++;
                }
                j = k;
            }
            if (grid[i].length != length) return false;
        }
        return true;
    }

}
