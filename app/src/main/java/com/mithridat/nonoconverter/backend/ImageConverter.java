package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;

/**
 * Image converting class
 */
class ImageConverter {

    /**
     * Method for getting field from image
     *
     * @param bmp - image
     * @param rows - number of field rows
     * @param cols - number of field columns
     * @return field
     */
    static public Field getField(Bitmap bmp, int rows, int cols) {

    }

    /**
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @return black-and-white image
     */
    static public Bitmap getBlackWhite(Bitmap bmp) {

    }

    /**
     * Method for getting thumbnail from black-and-white image
     *
     * @param bmp - black-and-white image
     * @param rows - number of thumbnail rows
     * @param cols - number of thumbnail columns
     * @param p - fill parameter
     * @return thumbnail
     */
    static private Field getThumbnail(Bitmap bmp, int rows, int cols, int p) {

    }

    /**
     * Method for checking nonogram solvability
     *
     * @param thu - thumbnail
     * @return true, if nonogram has solution
     *         false, otherwise
     */
    static private boolean isHaveSolution(Field thu) {

    }
}
