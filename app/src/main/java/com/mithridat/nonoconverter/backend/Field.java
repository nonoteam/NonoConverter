package com.mithridat.nonoconverter.backend;

/**
 * Field storage class
 */
class Field {

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
     * @param rows - number of field rows
     * @param cols - number of field columns
     */
    Field(int rows, int cols) {
        _field = new int[rows][cols];
        _rows = rows;
        _cols = cols;
    }

    /**
     * Method for getting number of field rows
     * @return number of field rows
     */
    public int getRows() {
        return _rows;
    }

    /**
     * Method for getting number of field columns
     * @return number of field columns
     */
    public int getCols() {
        return _cols;
    }

    /**
     * Method for getting field cell color with coordinates (i,j)
     * @param i - field cell row number
     * @param j - field cell column number
     * @return field cell color with coordinates (i,j)
     */
    public int getColor(int i, int j) {
        return _field[i][j];
    }

    /**
     * Method for setting field cell color with coordinates (i,j)
     * @param i - field cell row number
     * @param j - field cell column number
     * @param col - color
     */
    public void setColor(int i, int j, int col) {
        _field[i][j] = col;
    }

}
