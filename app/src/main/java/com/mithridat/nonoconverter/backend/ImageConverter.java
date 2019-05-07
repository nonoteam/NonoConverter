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
     * @param rows - number of nonogram field rows
     * @param cols - number of nonogram field columns
     * @param asyncTask - async task of image converting
     * @return nonogram field
     */
    public static Nonogram convertImage(
            Bitmap bmp,
            int rows,
            int cols,
            AsyncTask<Void, Void, Nonogram> asyncTask) {
        rows = min(rows, bmp.getHeight());
        cols = min(cols, bmp.getWidth());
        Bitmap bw = getBlackWhite(bmp, cols, rows);
        Nonogram nono = null;
        NonogramSolver solver = new NonogramSolver();
        solver.setAsyncTask(asyncTask);
        nono = new Nonogram(bw);
        solver.setNonogram(nono);
        if (solver.solve()) return nono.translateToColors();
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

}
