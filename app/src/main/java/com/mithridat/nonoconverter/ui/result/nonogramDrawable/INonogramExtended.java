package com.mithridat.nonoconverter.ui.result.nonogramDrawable;

/**
 * Interface for nonogram with main field and left and top fields with numbers.
 */
public interface INonogramExtended extends INonogram {

    /**
     * Function for getting number of rows in top field.
     *
     * @return number of rows in top field
     */
    int getTopFieldRows();

    /**
     * Function for getting number of columns in left field.
     *
     * @return number of columns in left field
     */
    int getLeftFieldColumns();

    /**
     * Function for getting length of the left row by it's index.
     *
     * @param row - index of row
     * @return length of the row
     */
    int getLeftRowLength(int row);

    /**
     * Function for getting length of the top column by it's index
     *
     * @param column - index of coumn
     * @return length of the column
     */
    int getTopColumnLength(int column);

    /**
     * Function for getting number in left field by number of it's row
     * and it's index in this row.
     *
     * @param row   - number of row
     * @param index - index in row
     * @return number in left field
     */
    int getLeftNumber(int row, int index);

    /**
     * Function for getting number in top field by number of it's column
     * and it's index in this column.
     *
     * @param column - number of column
     * @param index  - index in column
     * @return number in top field
     */
    int getTopNumber(int column, int index);

    /**
     * Function for getting maximal number in left and top fields.
     *
     * @return maximal number in left and top fields
     */
    int getMaxNumber();

    /**
     * Function for getting width of the left field.
     *
     * @return width of the left field
     */
    float getLeftFieldWidth();

    /**
     * Function for getting height of the left field.
     *
     * @return height of the left field
     */
    float getLeftFieldHeight();

    /**
     * Function for getting width of the top field.
     *
     * @return width of the top field
     */
    float getTopFieldWidth();

    /**
     * Function for getting height of the top field.
     *
     * @return height of the top field
     */
    float getTopFieldHeight();
}
