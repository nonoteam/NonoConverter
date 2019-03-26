package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;

/**
 * Image converting class
 */
class ImageConverter {

    /**
     * Method for converting image into nonogram field
     *
     * @param bmp - image
     * @param rows - number of nonogram field rows
     * @param cols - number of nonogram field columns
     * @return nonogram field
     */
    public static Field convertImage(Bitmap bmp, int rows, int cols) {

    }

    /**
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @return black-and-white image
     */
    public static  Bitmap getBlackWhite(Bitmap bmp) {

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
    private static Field getThumbnail(Bitmap bmp, int rows, int cols, int p) {

    }

    /**
     * Method for checking nonogram solvability
     *
     * @param thumb - thumbnail
     * @return true, if nonogram has solution
     *         false, otherwise
     */
    private static boolean isHaveSolution(Field thumb) {

    }

}
