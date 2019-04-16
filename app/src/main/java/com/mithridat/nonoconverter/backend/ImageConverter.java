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
    public static Field convertImage(
            Bitmap bmp,
            int rows,
            int cols,
            AsyncTask<Void, Void, Field> asyncTask) {
        rows = min(rows, bmp.getHeight());
        cols = min(cols, bmp.getWidth());
        Bitmap bw = getBlackWhite(bmp.copy(bmp.getConfig(), bmp.isMutable()));
        Field field = null;
        NonogramSolver solver = new NonogramSolver(null, asyncTask);
        for (int p = 128; !asyncTask.isCancelled() && p <= 248; p += 5) {
            field = getThumbnail(bw, rows, cols, p);
            solver.setNonogram(new Nonogram(field));
            if (solver.solve()) return field;
        }
        /*
         *TODO: `field` needs to be replaced by `null` when solver will be ready
         */
        return field;
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
        Field field = new Field(rows, cols);
        int height = bmp.getHeight(), width = bmp.getWidth();
        int h = height / rows, w = width / cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sumColors = 0;
                for (int x = j * w; x < (j + 1) * w; x++) {
                    for (int y = i * h; y < (i + 1) * h; y++) {
                        int color = bmp.getPixel(x, y);
                        if (color == Field.WHITE) {
                            sumColors += 255;
                        }
                    }
                }
                int avgColor = sumColors / (w * h);
                if (avgColor <= p) {
                    field.setColor(i, j, Field.BLACK);
                }
            }
        }
        return field;
    }

}
