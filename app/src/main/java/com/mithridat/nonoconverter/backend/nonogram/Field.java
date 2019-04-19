package com.mithridat.nonoconverter.backend.nonogram;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Field storage class
 */
public class Field implements Parcelable {

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
        Arrays.sort(_colors);
        _colorsCount = _colors.length;
        _state = INDEX;
        _rows = rows;
        _cols = cols;
        _field = new int[rows][cols];
        fillField(WHITE);
        int height = bmp.getHeight(), width = bmp.getWidth();
        int h = height / rows, w = width / cols;
        int sumColors;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sumColors = 0;
                for (int x = j * w; x < (j + 1) * w; x++) {
                    for (int y = i * h; y < (i + 1) * h; y++) {
                        if (bmp.getPixel(x, y) == WHITE) sumColors += 255;
                    }
                }
                if (sumColors <= p * w * h) {
                    _field[i][j] = Arrays.binarySearch(_colors, BLACK);
                }
            }
        }
    }

    /**
     * Method to copy object
     *
     * @return copy
     */
    public Field copy() {
        Field field = new Field();
        field._colors = _colors.clone();
        field._colorsCount = _colorsCount;
        field._state = _state;
        field._rows = _rows;
        field._cols = _cols;
        field._field = new int[_rows][];
        for (int i = 0; i < field._rows; i++) {
            field._field[i] = _field[i].clone();
        }
        return field;
    }

    /**
     * Private constructor without parameters
     */
    private Field() { }

    /**
     * Constructor by the parcel
     *
     * @param source - parcel
     */
    Field(Parcel source) {
        _rows = source.readInt();
        _cols = source.readInt();
        _field = new int[_rows][_cols];
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _field[i][j] = source.readInt();
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_rows);
        dest.writeInt(_cols);
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                dest.writeInt(_field[i][j]);
            }
        }
    }

    public static final Parcelable.Creator<Field> CREATOR =
            new Parcelable.Creator<Field>() {

                @Override
                public Field createFromParcel(Parcel source) {
                    return new Field(source);
                }

                @Override
                public Field[] newArray(int size) {
                    return new Field[size];
                }

            };

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
     * Method for getting length of row or сolumn
     *
     * @param type - ROW, if row
     *               COL, if column
     * @return length of row or сolumn
     */
    public int getLength(int type) {
        if (type == ROW) return getCols();
        return getRows();
    }

    /**
     * Method for getting field cell color with coordinates (i,j)
     *
     * @param i - field cell row number
     * @param j - field cell column number
     * @return field cell color with coordinates (i,j)
     */
    public int getColor(int i, int j) {
        return _field[i][j];
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
        if (type == ROW) return getColor(i, j);
        return getColor(j, i);
    }

    /**
     * Method for getting index of color or color depending on the _state
     *
     * @param color - сolor
     * @return index of color or color depending on the _state
     */
    public int getColorState(int color) {
        if (_state == COLOR) return color;
        return Arrays.binarySearch(_colors, color);
    }

    /**
     * Method for setting field cell color with coordinates (i,j)
     *
     * @param i - field cell row number
     * @param j - field cell column number
     * @param color - color
     */
    public void setColor(int i, int j, int color) {
        _field[i][j] = color;
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
        if (type == ROW) setColor(i, j, color);
        setColor(j, i, color);
    }

    /**
     * Method for getting count of colors
     *
     * @return count of colors
     */
    public int getColorsCount() {
        return _colorsCount;
    }

    /**
     * Method for getting index of first cell with another color
     * (current color - the color of cell with index = pos) in row or column
     *
     * @param ind - index of row or column
     * @param pos - position, the search begins with the next cell
     * @param dir - direction, +1 or -1
     * @param type - ROW, if row
     *               COL, if column
     * @return index of first cell with another color in row or column
     */
    public int getAnotherColorIndex(int ind, int pos, int dir, int type) {
        int i = pos + 1, len = getLength(type);
        for(; i >= 0 && i < len; i += dir) {
            if(getColor(ind, i, type) != getColor(ind, pos, type)) break;
        }
        return i;
    }

    /**
     * Method for getting thumbnail from Field
     * (cell of the field converts in the pixel of the bitmap)
     *
     * @return thumbnail
     */
    public Bitmap getBitmap() {
        Field field = this;
        if (_state == INDEX)
            field = translateToColors();
        Bitmap bmp = Bitmap.createBitmap(_cols, _rows, Bitmap.Config.RGB_565);
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                bmp.setPixel(j, i, field._field[i][j]);
            }
        }
        return bmp;
    }

    /**
     * Method for clearing field - all cells lose colors
     */
    void clear() {
        fillField(EMPTY);
    }

    /**
     * Method for getting new field with colors in cells
     *
     * @return new field
     */
    Field translateToColors() {
        Field field = copy();
        if(_state == INDEX) {
            field._state = COLOR;
            for (int i = 0; i < field._rows; i++) {
                for (int j = 0; j < field._cols; j++) {
                    if (_field[i][j] != EMPTY) {
                        field._field[i][j] = _colors[_field[i][j]];
                    } else {
                        field._field[i][j] = WHITE;
                    }
                }
            }
        }
        return field;
    }

    /**
     * Method for filling _field with color
     *
     * @param color - color
     */
    private void fillField(int color) {
        int value = getColorState(color);
        for (int i = 0; i < _rows; i++) {
            Arrays.fill(_field[i], value);
        }
    }

}
