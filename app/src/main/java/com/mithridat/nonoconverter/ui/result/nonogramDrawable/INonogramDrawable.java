package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

/**
 * Interface for drawable nonogram.
 */
public interface INonogramDrawable extends  IDrawable {


    /**
     * Method for getting number of rows.
     *
     * @return number of rows.
     */
    int getRows();

    /**
     * Method for getting number of columns.
     *
     * @return number of columns.
     */
    int getColumns();

    /**
     * Method for getting color of cell.
     *
     * @param row    -  cell row number
     * @param column -  cell column number
     * @return color of cell
     */
    int getColor(int row, int column);

    /**
     * Method for getting size of the cell.
     *
     * @return size of the cell
     */
    float getCellSize();
}
