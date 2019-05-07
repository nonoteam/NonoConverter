package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

/**
 * Interface for nonogram with only main field (without numbers).
 */
public interface INonogram {

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
     * Function for getting width of main field.
     *
     * @return width of main field
     */
    float getMainFieldWidth();

    /**
     * Function for getting height of main field.
     *
     * @returnheight of main field
     */
    float getMainFieldHeight();

    /**
     * Function for getting X coordinate of top left corner of main field.
     *
     * @return X coordinate of top left corner of main field
     */
    float getMainFieldTopLeftX();

    /**
     * Function for getting Y coordinate of top left corner of main field.
     *
     * @return Y coordinate of top left corner of main field
     */
    float getMainFieldTopLeftY();

    /**
     * Method for getting size of the cell.
     *
     * @return size of the cell
     */
    float getCellSize();
}
