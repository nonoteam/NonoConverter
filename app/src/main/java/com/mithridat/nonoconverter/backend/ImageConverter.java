package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;

import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.backend.solver.NonogramSolver;
import com.mithridat.nonoconverter.Utils.AsyncTaskPublish;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static com.mithridat.nonoconverter.Utils.sleepMillisec;
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
            final int[] arrRows,
            final int[] arrColumns,
            int exactIndex,
            AsyncTaskPublish asyncTask) {
        int[] idxOrder = indexOrder(exactIndex, arrColumns.length);
        int rows, cols, count = 0;
        Bitmap bw = null;
        Nonogram nono = null, res = null;
        NonogramSolver solver = new NonogramSolver();
        solver.setAsyncTask(asyncTask);
        for (int i : idxOrder) {
            rows = min(arrRows[i], bmp.getHeight());
            cols = min(arrColumns[i], bmp.getWidth());
            bw = getBlackWhite(bmp, cols, rows);
            nono = new Nonogram(bw);
            solver.setNonogram(nono);
            if (solver.solve()) {
                asyncTask.publish(++count);
                res = nono.translateToColors();
                break;
            }
            asyncTask.publish(++count);
        }
        sleepMillisec(100);
        return res;
    }

    /**
     * Method for getting resized black-and-white image from color image
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
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @return black-and-white image
     */
    public static Bitmap getBlackWhite(Bitmap bmp) {
        return getBlackWhite(bmp, bmp.getWidth(), bmp.getHeight());
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
