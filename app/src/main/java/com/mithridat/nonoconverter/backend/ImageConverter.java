package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.backend.solver.NonogramSolver;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.min;

/**
 * Image converting class
 */
public class ImageConverter {

    /**
     * Method for converting image into nonogram field
     *
     * @param bmp - image
     * @param arrRows - array of possible rows
     * @param arrColumns - array of possible columns
     * @param exactIndex - index of exact sizes in arrays
     * @param asyncTask - async task of image converting
     * @return nonogram field
     */
    public static Nonogram convertImage(
            Bitmap bmp,
            int[] arrRows,
            int[] arrColumns,
            int exactIndex,
            AsyncTask<Void, Void, Nonogram> asyncTask) {
        int[] idxOrder = indexOrder(exactIndex, arrColumns.length);
        int rows, cols;
        Bitmap bw = null;
        Nonogram nono = null;
        NonogramSolver solver = new NonogramSolver();
        for (int i : idxOrder) {
            rows = min(arrRows[i], bmp.getHeight());
            cols = min(arrColumns[i], bmp.getWidth());
            bw = getBlackWhite(bmp, cols, rows);
            solver.setAsyncTask(asyncTask);
            nono = new Nonogram(bw);
            solver.setNonogram(nono);
            if (solver.solve()) return nono.translateToColors();
        }
        return null;
    }

    /**
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @param w - width of black-and-white image
     * @param h - height of black-and-white image
     * @return black-and-white image
     */
    public static Bitmap getBlackWhite(Bitmap bmp, int w, int h) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bmp, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.resize(imageMat,
                imageMat,
                new Size(w, h),
                0,
                0,
                Imgproc.INTER_AREA);
        Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);

        Bitmap nbmp = Bitmap.createBitmap(w, h, bmp.getConfig());
        Utils.matToBitmap(imageMat, nbmp);

        return nbmp;
    }

    /**
     * Method for resizing bitmap
     *
     * @param bmp - bitmap
     * @param width - new width
     * @param height - new height
     * @return new bitmap
     */
    public static Bitmap resize(Bitmap bmp, int width, int height) {
        Mat imageMat = new Mat();
        Bitmap resized = Bitmap.createBitmap(width, height, bmp.getConfig());
        Utils.bitmapToMat(bmp, imageMat);
        Imgproc.resize(imageMat, imageMat, new Size(width, height));
        Utils.matToBitmap(imageMat, resized);
        return resized;
    }

    /**
     * Method for getting order of indexes of sizes
     *
     * @param exactIndex - index of exact sizes in arrays
     * @param length - length of arrays of sizes
     * @return order of indexes of sizes
     */
    private static int[] indexOrder(int exactIndex, int length) {
        int[] indexOrder = new int[length];
        int cntLeft = exactIndex, cntRight = length - exactIndex - 1;
        int minCnt = Math.min(cntLeft, cntRight);
        int maxCnt = Math.max(cntLeft, cntRight);
        indexOrder[0] = exactIndex;
        int index = 1;
        for (int i = 1; i <= minCnt; i++, index += 2) {
            indexOrder[index] = exactIndex - i;
            indexOrder[index + 1] = exactIndex + i;
        }
        int dir = cntLeft == maxCnt ? -1 : 1;
        for (int i = minCnt + 1; i <= maxCnt; i++, index++) {
            indexOrder[index] = exactIndex + i * dir;
        }
        return indexOrder;
    }

}
