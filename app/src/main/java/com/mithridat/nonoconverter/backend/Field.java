package com.mithridat.nonoconverter.backend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Field storage class
 */
public class Field implements Parcelable {

    /**
     * Constants for argument type - row or column
     */
    public static final int ROW = 0, COL = 1;

    /**
     * Field сolor matrix
     */
    private int[][] _field;

    /**
     * Field sizes
     */
    private int _rows, _cols;

    /**
     * Constructor by the field sizes
     *
     * @param rows - number of field rows
     * @param cols - number of field columns
     */
    public Field(int rows, int cols) {
        _field = new int[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                _field[i][j] = Colors.EMPTY;
            }
        }
        _rows = rows;
        _cols = cols;
    }

    /**
     * Constructor by the parcel
     *
     * @param source - parcel
     */
    private Field(Parcel source) {
        _rows = source.readInt();
        _cols = source.readInt();
        _field = new int[_rows][_cols];
        for(int i = 0; i < _rows; i++) {
            for(int j = 0; j < _cols; j++) {
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
        for(int i = 0; i < _rows; i++) {
            for(int j = 0; j < _cols; j++) {
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
     * Method for setting field cell color with coordinates (i,j)
     *
     * @param i - field cell row number
     * @param j - field cell column number
     * @param col - color
     */
    public void setColor(int i, int j, int col) {
        _field[i][j] = col;
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
        if (type == ROW) {
            int i = pos + 1;
            for(; i >= 0 && i < _cols; i += dir) {
                if(_field[ind][i] != _field[ind][pos]) break;
            }
            return i;
        } else {
            int i = pos + 1;
            for(; i >= 0 && i < _rows; i += dir) {
                if(_field[i][ind] != _field[pos][ind]) break;
            }
            return i;
        }
    }

}
