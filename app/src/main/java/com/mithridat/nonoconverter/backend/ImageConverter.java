package com.mithridat.nonoconverter.backend;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.mithridat.nonoconverter.backend.nonogram.Field;
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
        Bitmap bw = getBlackWhite(bmp.copy(bmp.getConfig(), bmp.isMutable()));
        Nonogram nono = null;
        NonogramSolver solver = new NonogramSolver();
        solver.setAsyncTask(asyncTask);
        for (int p = 128; !asyncTask.isCancelled() && p <= 248; p += 5) {
            nono = new Nonogram(bw, rows, cols, p);
            solver.setNonogram(nono);
            if (solver.solve()) return nono;
        }
        /*
         *TODO: `nono` needs to be replaced by `null` when solver will be ready
         */
        return nono;
    }

    /**
     * Method for getting black-and-white image from color image
     *
     * @param bmp - color image
     * @return black-and-white image
     */
    public static Bitmap getBlackWhite(Bitmap bmp) {
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bmp, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
        Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);

        Utils.matToBitmap(imageMat, bmp);

        return bmp;
    }

}
