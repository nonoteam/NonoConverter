package com.mithridat.nonoconverter.backend.nonogram;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;

import static com.mithridat.nonoconverter.Utils.drawGrid;
import static com.mithridat.nonoconverter.Utils.getHeightText;
import static com.mithridat.nonoconverter.Utils.getRectGrid;
import static com.mithridat.nonoconverter.Utils.getWidthText;
import static com.mithridat.nonoconverter.backend.nonogram.Field.BLACK;
import static com.mithridat.nonoconverter.backend.nonogram.Field.COL;
import static com.mithridat.nonoconverter.backend.nonogram.Field.EMPTY;
import static com.mithridat.nonoconverter.backend.nonogram.Field.ROW;
import static com.mithridat.nonoconverter.backend.nonogram.Field.WHITE;

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
                    field.setColor(i, j, field.getColorState(WHITE));
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
     * Method for getting maximum number in the nonogram
     *
     * @return maximum value in the nonogram
     */
    public int getMaxValue() {
        int max = _left[0][0];
        int[][][] nono = new int[][][]{ _left, _top};
        for (int[][] lines : nono) {
            for (int[] line : lines) {
                for (int value : line) {
                    if (value > max) max = value;
                }
            }
        }
        return max;
    }

    /**
     * Method for getting maximum length of the left rows or top columns
     *
     * @param type - ROW, if it needs left rows
     *               COL, if it needs top columns
     * @return maximum length of the _left or _top by the type
     */
    public int getMaxLength(int type) {
        int[][] values = type == ROW ? _left : _top;
        int max = values[0].length;
        for (int[] line : values) {
            if (line.length > max) max = line.length;
        }
        return max;
    }

    /**
     * Method for getting the crossword as image
     *
     * @return bitmap - the image that contains crossword
     */
    public Bitmap getBitmap() {
        final int max = getMaxValue(), font = 14, indent = 6;
        final int leftLength = getMaxLength(ROW), topLength = getMaxLength(COL);
        int size = 0;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        String smax = Integer.toString(max);
        Bitmap nonogram = null;
        Canvas canvas = null;
        p.setColor(BLACK);
        p.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        p.setTextSize(font);
        size =
                indent
                        + (int) Math.ceil(Math.max(getHeightText(p, smax),
                        getWidthText(p, smax)));
        nonogram =
                Bitmap.createBitmap(size * (_top.length + leftLength + 2),
                        size * (_left.length + topLength + 2),
                        Bitmap.Config.RGB_565);
        canvas = new Canvas(nonogram);
        canvas.drawColor(WHITE);
        drawGrid(canvas,
                new Point(size * (leftLength + 1), size),
                new Point(_top.length, topLength),
                size);
        drawGrid(canvas,
                new Point(size, size * (topLength + 1)),
                new Point(_top.length + leftLength, _left.length),
                size);
        drawGridNumbers(canvas,
                new Point(size, size * (topLength + 1)),
                p,
                size,
                ROW,
                leftLength);
        drawGridNumbers(canvas,
                new Point(size * (leftLength + 1), size),
                p,
                size,
                COL,
                topLength);
        return nonogram;
    }

    /**
     * Method for drawing numbers in the grid on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - position of the left-top corner of the grid on the canvas
     * @param p - the Paint for drawing numbers
     * @param cellSize - size of the one cell
     * @param type - ROW, if it needs to draw left rows
     *               COL, if it needs to draw top columns
     * @param maxLength - maximum length of the rows or columns
     */
    public void drawGridNumbers(
            Canvas canvas,
            Point pos,
            Paint p,
            int cellSize,
            int type,
            int maxLength) {
        final boolean isRow = type == ROW;
        final int dx = isRow ? cellSize : 0, dy = isRow ? 0 : cellSize;
        final int[][] numbers = isRow ? _left : _top;
        float w = 0f, h = 0f;
        int newLeft = 0, newTop = 0;
        String s = null;
        Rect grid = getRectGrid(cellSize);
        grid.offsetTo(pos.x, pos.y);
        for (int[] line : numbers) {
            newLeft = isRow ? pos.x + (maxLength - line.length) * cellSize
                    : grid.left;
            newTop = !isRow ? pos.y + (maxLength - line.length) * cellSize
                    : grid.top;
            grid.offsetTo(newLeft, newTop);
            for (int value : line) {
                s = Integer.toString(value);
                w = getWidthText(p, s);
                h = getHeightText(p, s);
                canvas.drawText(s,
                        grid.left + (cellSize - w) / 2,
                        grid.bottom - (cellSize - h) / 2,
                        p);
                grid.offset(dx, dy);
            }
            grid.offset(dy, dx);
        }
    }

    /**
     * Method for drawing numbers in the grid on the canvas
     *
     * @param canvas - the Canvas
     * @param pos - position of the left-top corner of the grid on the canvas
     * @param p - the Paint for drawing numbers
     * @param cellSize - size of the one cell
     * @param type - ROW, if it needs to draw left rows
     *               COL, if it needs to draw top columns
     */
    public void drawGridNumbers(
            Canvas canvas,
            Point pos,
            Paint p,
            int cellSize,
            int type) {
        drawGridNumbers(canvas,
                pos,
                p,
                cellSize,
                type,
                getMaxLength(type));
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
                if (_field.getColor(i, j, type)
                        == _field.getColorState(BLACK)) {
                    int[] tmp = Arrays.copyOf(grid[i], length + 1);
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
