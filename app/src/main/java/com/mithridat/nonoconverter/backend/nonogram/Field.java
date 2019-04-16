package com.mithridat.nonoconverter.backend.nonogram;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Field storage class
 */
public class Field {

    /**
     * White color
     */
    public static final int WHITE = 0xFFFFFFFF;

    /**
     * Black color
     */
    public static final int BLACK = 0xFF000000;

    /**
     * Empty cell
     */
    public static final int EMPTY = -1;

    /**
     * Constants for argument type - row or column
     */
    public static final int ROW = 0, COL = 1;

    /**
     * Constants for state type - indexes or colors
     */
    public static final int INDEX = 0, COLOR = 1;

    /**
     * Field сolor matrix
     */
    private int[][] _field;

    /**
     * Field sizes
     */
    private int _rows, _cols;

    /**
     * Array of colors that used in a puzzle. All cells of the field
     * contain values of colors. But now it will contain
     * indexes of these colors in the array _colors by default.
     */
    private int[] _colors;

    /**
     * Length of the array _colors
     */
    private int _colorsCount;

    /**
     * Identifier that indicates what is stored in the field's cells.
     * Both colors and indexes of these colors can be stored in the cells
     */
    private int _state;

    /**
     * Constructor by black-and-white image
     *
     * @param bmp - black-and-white image
     * @param rows - number of thumbnail rows
     * @param cols - number of thumbnail columns
     * @param p - fill parameter
     */
    public Field(Bitmap bmp, int rows, int cols, int p) {
        _colors = new int[]{ BLACK, WHITE };
        _colorsCount = _colors.length;
        _state = INDEX;
        _rows = rows;
        _cols = cols;
        _field = new int[rows][cols];
        int height = bmp.getHeight(), width = bmp.getWidth();
        int h = height / rows, w = width / cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sumColors = 0;
                for (int x = j * w; x < (j + 1) * w; x++) {
                    for (int y = i * h; y < (i + 1) * h; y++) {
                        int color = bmp.getPixel(x, y);
                        if (color == WHITE) {
                            sumColors += 255;
                        }
                    }
                }
                int avgColor = sumColors / (w * h);
                if (avgColor <= p) {
                    _field[i][j] = Arrays.binarySearch(_colors, BLACK);
                }
            }
        }
    }

    /**
     * Private constructor without parameters
     */
    private Field() {

    }

    /**
     * Method for getting number of field rows
     *
     * @return number of field rows
     */
    public int getRows() {
        return _rows;
    }

    /**
     * Method for getting number of field columns
     *
     * @return number of field columns
     */
    public int getCols() {
        return _cols;
    }

    /**
     * Method for getting length of _rows or _cols
     *
     * @param type - ROW, if _rows
     *               COL, if _cols
     * @return length of _rows or _cols
     */
    public int getLength(int type) {
        if (type == ROW) return getRows();
        return getCols();
    }

    /**
     * Method for getting field cell color with coordinates (i,j) or (j, i)
     *
     * @param i - i
     * @param j - j
     * @param type - ROW, if (i, j)
     *               COL, if (j, i)
     * @return field cell color with coordinates (i,j) or (j, i)
     */
    public int getColor(int i, int j, int type) {
        if (type == ROW) return _field[i][j];
        return _field[j][i];
    }

    /**
     * Method for setting field cell color with coordinates (i,j) or (j, i)
     *
     * @param i - i
     * @param j - j
     * @param color - color
     * @param type - ROW, if (i, j)
     *               COL, if (j, i)
     */
    public void setColor(int i, int j, int color, int type) {
        if (type == ROW) _field[i][j] = color;
        _field[j][i] = color;
    }

    /**
     * Method for getting count of colors
     *
     * @return count of colors
     */
    int getColors() {
        return _colorsCount;
    }

    /**
     * Method for getting thumbnail from Field
     * (cell of the field converts in the pixel of the bitmap)
     *
     * @return thumbnail
     */
    Bitmap getBitmap() {
        Field field = translateToColors();
        Bitmap bmp = Bitmap.createBitmap(_cols,
                _rows,
                Bitmap.Config.ARGB_8888);
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                bmp.setPixel(j, i, field._field[j][i]);
            }
        }
        return bmp;
    }

    /**
     * Method for clearing field - all cells lose colors
     */
    void clear() {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _field[i][j] = EMPTY;
            }
        }
    }

    /**
     * Method for getting new field with colors in cells
     *
     * @return new field
     */
    Field translateToColors() {
        Field field = new Field();
        field._colors = _colors;
        field._colorsCount = _colorsCount;
        field._state = COLOR;
        field._rows = _rows;
        field._cols = _cols;
        field._field = _field;
        if(_state == INDEX) {
            for (int i = 0; i < field._rows; i++) {
                for (int j = 0; j < field._cols; j++) {
                    field._field[i][j] = _colors[_field[i][j]];
                }
            }
        }
        return  field;
    }

}
